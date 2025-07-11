package appdev.studdybuddy
import org.jetbrains.exposed.sql.Table
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

object Users : Table("users") {
    val username = varchar("username", 50)
    val email = varchar("email", 100)
    val password = varchar("password", 100)
    override val primaryKey = PrimaryKey(email, name = "PK_Users_Email")
}

object Sessions : Table("sessions") {
    val id = integer("id").autoIncrement()
    val userEmail = varchar("user_email", 100).references(Users.email)
    val date = varchar("date", 10) // enough for "YYYY-MM-DD"
    val duration = integer("duration") // in minutes or seconds, your choice
    val points = integer("points")
    val description = varchar("description", 255).nullable()
    override val primaryKey = PrimaryKey(id, name = "PK_Session_id")
}


@Serializable
data class User(
    val username: String,
    val email: String,
    val password: String
)

@Serializable
data class Session(
    val id: Int? = null,
    val userEmail: String,
    val date: String, // store date as ISO String (e.g. "2025-07-11")
    val duration: Int,
    val points: Int,
    val description: String? = null
)



fun getAllUsers(): List<User> = transaction {
    Users.selectAll().map {
        User(
            username = it[Users.username],
            email = it[Users.email],
            password = it[Users.password]
        )
    }
}

fun getUserSessions(email: String): List<Session> = transaction {
    Sessions.selectAll().where(Sessions.userEmail eq email).map {
        Session(
            id = it[Sessions.id],
            userEmail = it[Sessions.userEmail],
            date = it[Sessions.date],
            duration = it[Sessions.duration],
            points = it[Sessions.points],
            description = it[Sessions.description]
        )
    }
}


fun insertUser(user: User) = transaction {
    Users.insert {
        it[username] = user.username
        it[email] = user.email
        it[password] = user.password
    }
}

fun insertSession(session: Session) = transaction {
    Sessions.insert {
        it[userEmail] = session.userEmail
        it[date] = session.date
        it[duration] = session.duration
        it[points] = session.points
        it[description] = session.description
    }
}
