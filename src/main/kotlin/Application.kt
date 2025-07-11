package appdev.studdybuddy

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import org.yaml.snakeyaml.Yaml
import java.io.InputStream

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    loadConfig()
    configureMonitoring()
    configureDatabases()
    configureRouting()
}
