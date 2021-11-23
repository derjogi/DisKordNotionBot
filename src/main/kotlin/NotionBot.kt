import com.petersamokhin.notionsdk.Notion
import com.sksamuel.hoplite.ConfigLoader
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import io.ktor.client.*
import io.ktor.client.engine.cio.*

class NotionBot {

    val config = ConfigLoader().loadConfigOrThrow<Config>("/config.yaml")
    val notion = Notion.fromToken(config.notion.auth, httpClient = HttpClient(CIO))

    suspend fun run() {
        val kord = Kord(config.discord.auth)
        val dbScheme = notion.retrieveDatabase(config.notion.db)
        val db = notion.queryDatabase(config.notion.db)

        println(dbScheme)

        println(db)

        kord.on<MessageCreateEvent> {
            if(message.author?.isBot == true) return@on
//            if(message.content != "!list something") return@on

            val command = Command.create(db, dbScheme, message.content)
            val result = command.performAction()
            message.channel.createMessage("$result")

            /*
            Command would define 'performAction' and store the db as field,
            and implementations would implement performAction(), which can be the steps
            to check whether that info is in the db, then update it; or just to return that info (if the command is e.g. !list [columnName])

            Examples of commands:

            !list [columnName] : ListCommand:   Lists all the values in a specified column (possibly multiple),
                                                including the 'default' column (Name). (Probably?!?!)

            !update [row] [columnName] [value] : UpdateCommand: Updates a specific field of a specific row (by Name)
                                                                with the given value (possibly creating it if not present?)
                                                                Maybe this could be a two-step operation where it first
                                                                returns the current status and prompts for whether to proceed?


             */


            // Todo: filter rows for a column with specific value or so?
            //
//            db.rows.first { row -> row.columns.getValue() }

//            val response: HttpResponse = client.post(url) {
//                contentType(ContentType.Application.Json)
//                header("Notion-Version", "2021-05-13")
//            }
//
//            if (response.status.value >= 400) {
//                message.channel.createMessage("The request was invalid: " + response.status.description)
//                return@on
//            }

//            response = requests.post(url, headers=config.notion.)
//            response.raise_for_status()   # just in case we have 404 or so
//            result = response.json()["results"]
//
        }

        kord.login()
    }
}

suspend fun main() {
    NotionBot().run()
}

// Config data classes:
data class NotionConf(val db: String, val auth: String)
data class DiscordConf(val auth: String)
data class Config(val notion: NotionConf, val discord: DiscordConf)