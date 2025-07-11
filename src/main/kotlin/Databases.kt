package appdev.studdybuddy


import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*


fun Application.configureDatabases() {
    val config = loadConfig()
    Database.connect(
        url = config.postgres.url,
        driver = "org.postgresql.Driver",
        user = config.postgres.user,
        password = config.postgres.password
    )
}

