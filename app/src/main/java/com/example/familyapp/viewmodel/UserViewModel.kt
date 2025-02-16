package com.example.familyapp.viewmodel

import UserRepository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyapp.data.model.user.AddUserRequest
import com.example.familyapp.data.model.user.User


class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users
    private val _errorMessages = MutableLiveData<String>()
    val errorMessages: LiveData<String>
        get() = _errorMessages
    fun fetchUsers(familyId: Int) {
        userRepository.getMembers(familyId).observeForever { fetchedUsers ->
            _users.value = fetchedUsers ?: emptyList()
        }
    }

    fun deleteUser(userId: Int,familyId: Int) {
        userRepository.deleteUser(userId) { result ->
            result.onSuccess {
                fetchUsers(familyId)
            }
            result.onFailure { error ->

            }
        }
    }

    fun addUser(addUserRequest: AddUserRequest) {
        Log.d("UserViewModel", "Attempting to add user: $addUserRequest")
        userRepository.createUser(addUserRequest,1) { result ->
            result.onSuccess { user ->
                Log.d("UserViewModel", "User added successfully: ${user.id}")
                fetchUsers(1)
            }
            result.onFailure { error ->
                Log.e("UserViewModel", "Failed to add user: ${error.message}")
            }
        }
    }

}