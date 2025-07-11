package appdev.studdybuddy

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Connection
import java.sql.DriverManager
import org.jetbrains.exposed.sql.*
import org.slf4j.event.*

fun Application.configureRouting() {
    routing {
        // List all users
        get("/users") {
            val users = getAllUsers()
            call.respond(users)
        }

        // Add new user
        post("/users") {
            val user = call.receive<User>()
            insertUser(user)
            call.respond(HttpStatusCode.Created, user)
        }

        // Get sessions for a user
        post("/sessions/user") {
            val request = call.receive<Map<String, String>>()
            val email = request["email"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing email")
            val sessions = getUserSessions(email)
            call.respond(sessions)
        }


        // Add session for user
        post("/sessions") {
            val session = call.receive<Session>()
            insertSession(session)
            call.respond(HttpStatusCode.Created, session)
        }
    }
}

