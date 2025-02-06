package com.example.familyapp.viewmodel

import UserRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.familyapp.data.model.user.AddUserRequest
import com.example.familyapp.data.model.user.User


class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    fun fetchUsers(familyId: Int) {
        userRepository.getMembers(familyId).observeForever { fetchedUsers ->
            _users.value = fetchedUsers ?: emptyList()
        }
    }


    fun addUser(addUserRequest: AddUserRequest) {
        userRepository.createUser(addUserRequest) { result ->
            result.onSuccess { user ->
                 fetchUsers(user.idFamille)
            }
            result.onFailure {
             }
        }
    }
}