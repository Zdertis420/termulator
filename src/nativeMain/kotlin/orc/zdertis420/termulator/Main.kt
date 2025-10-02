package orc.zdertis420.termulator

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import org.gtkkn.bindings.gdk.Display
import org.gtkkn.bindings.gio.ApplicationFlags
import org.gtkkn.bindings.gtk.Application
import org.gtkkn.bindings.gtk.ApplicationWindow
import org.gtkkn.bindings.gtk.Box
import org.gtkkn.bindings.gtk.CssProvider
import org.gtkkn.bindings.gtk.Entry
import org.gtkkn.bindings.gtk.Orientation
import org.gtkkn.bindings.gtk.StyleContext
import org.gtkkn.extensions.gio.runApplication
import org.gtkkn.extensions.glib.util.log.Log
import org.gtkkn.extensions.glib.util.log.writer.installConsoleLogWriter
import org.gtkkn.native.gtk.GTK_STYLE_PROVIDER_PRIORITY_APPLICATION
import platform.posix.getenv
import platform.posix.gethostname
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    Log.installConsoleLogWriter()

    val app = Application("orc.zdertis420.termulator", ApplicationFlags.FLAGS_NONE)
    app.onActivate {
        val window = ApplicationWindow(app)
        window.title = (getHostname() + getenv("USER")?.toKString())
        window.setDefaultSize(350, 150)

        val cssProvider = CssProvider()
        cssProvider.loadFromPath("src/nativeMain/resources/style.css")

        val display = Display.getDefault()
        StyleContext.addProviderForDisplay(
            display!!,
            cssProvider,
            GTK_STYLE_PROVIDER_PRIORITY_APPLICATION.toUInt()
        )

        val mainBox = Box(Orientation.VERTICAL, 0)

        val margin = 12

        val contentArea = Box(Orientation.VERTICAL, 0)
        contentArea.vexpand = true
        contentArea.marginTop = margin
        contentArea.marginStart = margin
        contentArea.marginEnd = margin

        val inputBox = Box(Orientation.VERTICAL, 0)

        inputBox.marginStart = margin
        inputBox.marginEnd = margin
        inputBox.marginBottom = margin

        val entry = Entry()
        entry.placeholderText = "Введите команду и нажмите Enter..."

        inputBox.append(entry)

        mainBox.append(contentArea)
        mainBox.append(inputBox)

        window.child = mainBox

        if (args.isNotEmpty()) {

        }

        val handleInput = {
            val text: String = entry.getText()
            parseCommand(text)
            entry.setText("")
        }

        entry.onActivate { handleInput() }

        window.present()
    }

    app.runApplication()
}

fun getHostname(): String {
    memScoped {
        val bufSize = 256
        val cName = allocArray<ByteVar>(bufSize)

        if (gethostname(cName, bufSize.toULong()) == 0) {
            return cName.toKString()
        } else {
            return ""
        }
    }
}

fun parseCommand(command: String) {
    println("=== Введенный текст: $command ===")

    when {
        command =="exit" -> exitProcess(0)
        command.take(2) == "cd" -> println("Change Directry")
        command.take(2) == "ls" -> println("List")
    }
}