package com.example.familyapp.network.mapper

import com.example.familyapp.data.model.message.Message
import com.example.familyapp.network.dto.messageDto.MessageDto

fun mapMessageDtoToMessage(messageDto: MessageDto): Message {
    return Message(
        idMessage = 0.toString(),
        contenu = messageDto.contenu,
        dateEnvoie = messageDto.dateEnvoie,
        isVue = messageDto.isVue,
        user =  messageDto.user,
        idChat = messageDto.idChat,
    )
}
