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
        get("/users/{email}/sessions") {
            val email = call.parameters["email"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing email")
            val sessions = getUserSessions(email)
            call.respond(sessions)
        }

        // Add session for user
        post("/users/{email}/sessions") {
            val email = call.parameters["email"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing email")
            val session = call.receive<Session>()
            if (session.userEmail != email) {
                return@post call.respond(HttpStatusCode.BadRequest, "Email mismatch")
            }
            insertSession(session)
            call.respond(HttpStatusCode.Created, session)
        }
    }
}

