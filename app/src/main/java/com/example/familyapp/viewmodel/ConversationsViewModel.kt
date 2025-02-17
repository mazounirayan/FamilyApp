import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyapp.data.model.conversation.Conversation
import com.example.familyapp.repositories.ConversationRepository

class ConversationsViewModel(private val repository: ConversationRepository) : ViewModel() {
    private var currentUserId: Int = 1

     val conversations: LiveData<List<Conversation>> by lazy {
        repository.getChatsByUserId(currentUserId)
    }

    fun loadConversations(userId: Int) {
        currentUserId = userId
     }
}
