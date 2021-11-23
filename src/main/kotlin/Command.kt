import com.petersamokhin.notionsdk.data.model.result.NotionDatabase
import com.petersamokhin.notionsdk.data.model.result.NotionDatabaseProperty
import com.petersamokhin.notionsdk.data.model.result.NotionDatabaseSchema
import io.ktor.util.*
import java.lang.IllegalStateException
import java.util.*

abstract class Command(_db: NotionDatabase, command: String) {
    val db: NotionDatabase
    init {
        println("Init with $command")
        db = _db
        verifyCommand(command)
    }

    public abstract fun performAction(): String?

    abstract fun verifyCommand(command: String)

    fun hasRow(): Boolean {
        return true
    }

    companion object Factory {
        fun create(db: NotionDatabase, dbScheme: NotionDatabaseSchema, command: String): Command {
            return if (command.startsWith("!update"))
                UpdateCommand(db, command)
            else if (command.startsWith("!list")) {
                ListCommand(db, dbScheme, command)
            } else {
                throw IllegalStateException("Didn't recognize command: $command. Must start with !list or !update.")
            }
        }
    }
}

class ListCommand(db: NotionDatabase, dbScheme: NotionDatabaseSchema, command: String) : Command(db, command) {
    private var schema = dbScheme
    private lateinit var columns: List<String>  // lateinit so that we don't need to assign a value here when it has been already set from verifyCommands!

    override fun verifyCommand(command: String) {
        columns = command.split(" ").drop(1)
        // if split is empty after dropping the first !list then we'll just print all the headers
    }

    override fun performAction(): String {
        return if (columns.isEmpty()) {
            "Available columns: " + schema.schema.keys.joinToString()
        } else {
            db.rows.joinToString("\n", transform = { row ->
                "Row " + propertyToString(row.columns.values.first { col -> col.value is NotionDatabaseProperty.Title }.value) +
                        ": " + columns.joinToString { col -> propertyToString(row.columns.getValue(col).value) as CharSequence }
            })
        }
    }

    private fun propertyToString(property: NotionDatabaseProperty) =
        when (property) {
            is NotionDatabaseProperty.MultiSelect -> property.selected.joinToString { sel -> sel.name }
            is NotionDatabaseProperty.Checkbox -> "Selected? " + property.selected
            is NotionDatabaseProperty.CreatedBy -> property.createdBy.toString()
            is NotionDatabaseProperty.CreatedTime -> "Was created some time ago."
            is NotionDatabaseProperty.Date -> "Who cares really"
            is NotionDatabaseProperty.Email -> property.email
            is NotionDatabaseProperty.Files -> "Got " + property.files.size + " files"
            is NotionDatabaseProperty.Formula -> property.formula.toString()
            is NotionDatabaseProperty.LastEditedBy -> property.toString()
            is NotionDatabaseProperty.LastEditedTime -> property.toString()
            is NotionDatabaseProperty.Number -> property.number.toString()
            is NotionDatabaseProperty.People -> property.people.joinToString()
            is NotionDatabaseProperty.PhoneNumber -> property.phoneNumber
            is NotionDatabaseProperty.Relation -> property.id
            is NotionDatabaseProperty.Rollup -> property.toString()
            is NotionDatabaseProperty.Select -> property.selected?.name
            is NotionDatabaseProperty.Text -> property.text
            is NotionDatabaseProperty.Title -> property.text
            is NotionDatabaseProperty.Url -> property.url
        }
}

class UpdateCommand(db: NotionDatabase, command: String) : Command(db, command) {
    override fun performAction(): String? {
        TODO("Not yet implemented")
    }

    override fun verifyCommand(command: String) {
        TODO("Not yet implemented")
    }
}
