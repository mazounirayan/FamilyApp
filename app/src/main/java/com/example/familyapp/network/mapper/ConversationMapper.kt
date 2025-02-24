package com.example.familyapp.network.mapper

import com.example.familyapp.data.model.chat.Chat
import com.example.familyapp.data.model.conversation.Conversation
import com.example.familyapp.network.dto.chatDto.ChatDto
import com.example.familyapp.network.dto.chatDto.ConversationDto

fun mapConversationDtoToConversation(conversationDto: ConversationDto): Conversation {
    return Conversation(
        id = conversationDto.id,
        name = conversationDto.name,
        lastMessage = conversationDto.lastMessage,
        messageTime = conversationDto.messageTime,
        profileImage = conversationDto.profileImage  ?: ""
    )
}
fun mapChatDtoToChat(chatDto: ChatDto): Chat {
    return Chat(
        idChat = chatDto.idChat,
        libelle = chatDto.libelle,
        participants = chatDto.participants
    )
}

