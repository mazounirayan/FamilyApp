import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyapp.data.model.chat.Chat
import com.example.familyapp.data.model.chat.CreateChat
import com.example.familyapp.data.model.conversation.Conversation
import com.example.familyapp.repositories.ConversationRepository
import com.example.familyapp.utils.SessionManager

class ConversationsViewModel(private val repository: ConversationRepository) : ViewModel() {

    private var currentUserId: Int = SessionManager.currentUser!!.id

    val conversations: LiveData<List<Conversation>> by lazy {
        repository.getChatsByUserId(currentUserId)
    }

    private val _chatCreationStatus = MutableLiveData<Boolean>()
    val chatCreationStatus: LiveData<Boolean> get() = _chatCreationStatus
    private val _usersOfChat = MutableLiveData<List<Int>>()
    val usersOfChat: LiveData<List<Int>> get() = _usersOfChat
    fun addChat(chat: CreateChat) {
        repository.chatCreationStatus.observeForever { status ->
            _chatCreationStatus.value = status
        }
        repository.addChat(chat)
    }

    private val _chatQuitStatus = MutableLiveData<Boolean>()
    val chatQuitStatus: LiveData<Boolean> get() = _chatQuitStatus
    fun loadConversations(userId: Int) {
        currentUserId = userId
    }

    fun createChat(libelle: String, participants: List<Int>, onResult: (Chat?) -> Unit) {
        repository.createChat(libelle, participants) { chat ->
            onResult(chat)
        }
    }

    fun quitChat(userId: Int, chatId: Int) {
        repository.chatQuitStatus.observeForever { status ->
            _chatQuitStatus.value = status
        }
        repository.quitChat(userId, chatId)
    }

    fun fetchUsersOfChat(chatId: Int) {
        repository.getUsersOfChat(chatId) { result ->
            result.fold(
                onSuccess = { userIds ->
                    _usersOfChat.postValue(userIds)
                },
                onFailure = { error ->
                    Log.e("ConversationsViewModel", "Erreur lors de la récupération des users du chat : ${error.message}")
                }
            )
        }
    }

}

