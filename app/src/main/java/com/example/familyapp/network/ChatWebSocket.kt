package com.example.familyapp.network

import okhttp3.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class ChatWebSocket(private val familyId: Int) {

    private val client = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS) // Garder la connexion ouverte
        .build()

    private lateinit var webSocket: WebSocket

    fun connect() {
        val request = Request.Builder()
            .url("https://websocketfamilyapp.onrender.com")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                println("Connexion établie avec le serveur WebSocket")

                // Envoyer un message pour rejoindre une famille
                val joinMessage = JSONObject().apply {
                    put("event", "joinFamily")
                    put("familyId", familyId)
                }
                webSocket.send(joinMessage.toString())
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                println("Message reçu : $text")
                // TODO : Traiter le message reçu (par exemple, mettre à jour l'interface utilisateur)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                println("Erreur WebSocket : ${t.message}")
                println("Erreur WebSocket : ${webSocket.toString()}")
                println("Erreur WebSocket : ${response}")

                // TODO : Ajouter une logique de reconnexion si nécessaire
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                println("Connexion fermée : $reason")
            }
        })
    }

    fun sendMessage(senderId: Int, content: String) {
        val message = JSONObject().apply {
            put("familyId", familyId)
            put("senderId", senderId)
            put("content", content)
        }
        webSocket.send(message.toString())
    }

    fun disconnect() {
        webSocket.close(1000, "Déconnexion")
    }
}
