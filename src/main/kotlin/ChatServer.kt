import io.javalin.Javalin
import io.javalin.websocket.WsContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.time.Instant

@Serializable
data class Message(val sender: String, val text: String, val timestamp: Long = Instant.now().toEpochMilli())

fun main() {
    val app = Javalin.create { config ->
        config.staticFiles.add("/public") // Serve static files from the 'public' directory.
    }

    val messages = mutableListOf<Message>()
    val sessions = mutableSetOf<WsContext>()
    val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    app.ws("/chat") { ws ->
        ws.onConnect { ctx ->
            println("New client connected: ${ctx.hashCode()}")
            sessions.add(ctx)
            // Optionally send message history to the new client:
            // ctx.send(json.encodeToString(messages))
        }
        ws.onMessage { ctx ->
            val messageString = ctx.message()
            try {
                val received = json.decodeFromString<Message>(messageString)
                println("Received message from ${received.sender}: ${received.text}")

                // Assign default sender if blank.
                val message = if (received.sender.isBlank()) {
                    received.copy(sender = "User")
                } else {
                    received
                }
                messages.add(message)
                // Broadcast message to all connected sessions.
                sessions.forEach { session ->
                    if (session.session.isOpen) {
                        session.send(json.encodeToString(message))
                    }
                }
            } catch (ex: Exception) {
                println("Failed to decode message: $messageString")
                ex.printStackTrace()
            }
        }
        ws.onClose { ctx ->
            println("Client disconnected: ${ctx.hashCode()}")
            sessions.remove(ctx)
        }
    }

    app.get("/messages") { ctx ->
        ctx.json(messages)
    }

    app.start(7000) // Start server on port 7000.

}