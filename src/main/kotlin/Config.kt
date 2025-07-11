package appdev.studdybuddy

import io.ktor.server.application.*
import org.yaml.snakeyaml.Yaml
import java.io.InputStream


fun loadConfig(): AppConfig {
    val yaml = Yaml()
    val inputStream: InputStream = Application::class.java.classLoader.getResourceAsStream("application.yaml")
        ?: error("Config file not found")
    val data = yaml.loadAs(inputStream, Map::class.java) as Map<String, Map<String, String>>

    val pg = data["postgres"] ?: error("Postgres config not found")
    return AppConfig(
        postgres = PostgresConfig(
            url = pg["url"] ?: error("url missing"),
            user = pg["user"] ?: error("user missing"),
            password = pg["password"] ?: ""
        )
    )
}

data class PostgresConfig(
    val url: String,
    val user: String,
    val password: String
)

data class AppConfig(
    val postgres: PostgresConfig
)