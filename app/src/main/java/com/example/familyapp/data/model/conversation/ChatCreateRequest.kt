package com.example.familyapp.data.model.conversation

data class ChatCreateRequest (
    val libelle: String,
    val participants: List<Int>
)
