package com.example.familyapp.websocket
import com.example.familyapp.views.fragments.message.ChatFragment
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class SocketIOClient(private val chatFragment: ChatFragment) {

    private lateinit var socket: Socket

    fun connect(userId: Int) {
        try {
            // Configuration du client
            val options = IO.Options()
            options.transports = arrayOf("websocket") // Forcer l'utilisation de WebSocket
            options.reconnection = true // Activer la reconnexion automatique

            // URL du serveur
            socket = IO.socket("http://10.0.2.2:3000", options)

            // Écouter les événements
            socket.on(Socket.EVENT_CONNECT) {
                println("Connexion réussie")
                socket.emit("joinFamily", userId)
            }

            socket.on("message") { args ->
                if (args.isNotEmpty()) {
                    val data = args[0] as JSONObject
                    val messageReceived = data.get("content")
                    val idUser = data.get("senderId") as Int

                    // Appelez la méthode messageReceived sur le fragment existant
                    chatFragment.messageReceived(messageReceived.toString(), idUser)
                    println("Message reçu : $data")
                }
            }

            socket.on(Socket.EVENT_DISCONNECT) {
                println("Déconnecté du serveur")
            }

            // Connecter au serveur
            socket.connect()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendMessage(message: String) {
        if (::socket.isInitialized && socket.connected()) {
            socket.emit("sendMessage", message)
        } else {
            println("Socket non connecté")
        }
    }


}

