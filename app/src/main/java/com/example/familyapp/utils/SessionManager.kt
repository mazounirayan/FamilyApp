package com.example.familyapp.utils

import com.example.familyapp.data.model.user.User

object SessionManager {
    var currentUser: User? = null
    var isChatActive: Boolean = false

}