package com.example.familyapp.websocket
import com.example.familyapp.views.fragments.message.ChatFragment
import com.example.instaclone.app_utils.URL_WEBSOCKET
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class SocketIOClient(private val chatFragment: ChatFragment) {

    private lateinit var socket: Socket

    fun connect(userId: Int) {
        try {
            val options = IO.Options()
            options.transports = arrayOf("websocket")
            options.reconnection = true

            socket = IO.socket(URL_WEBSOCKET, options)

            socket.on(Socket.EVENT_CONNECT) {
                println("Connexion réussie")
                socket.emit("joinFamily", userId)
            }

            socket.on("message") { args ->
                if (args.isNotEmpty()) {
                    val data = args[0] as JSONObject
                    val messageReceived = data.getString("content")
                    val idUser = data.getInt("senderId")

                    // Appelez la méthode messageReceived sur le fragment existant
                    chatFragment.messageReceived(messageReceived, idUser)
                    println("Message reçu : $data")
                }
            }

            socket.on(Socket.EVENT_DISCONNECT) {
                println("Déconnecté du serveur")
            }

            socket.connect()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendMessage(message: String) {
        if (::socket.isInitialized && socket.connected()) { socket.emit("sendMessage", message)
        } else {
            println("Socket non connecté")
        }
    }
}
