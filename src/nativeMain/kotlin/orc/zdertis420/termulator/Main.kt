package orc.zdertis420.termulator

import org.gtkkn.bindings.gio.ApplicationFlags
import org.gtkkn.bindings.gtk.Application
import org.gtkkn.bindings.gtk.ApplicationWindow
import org.gtkkn.bindings.gtk.Box
import org.gtkkn.bindings.gtk.Entry
import org.gtkkn.bindings.gtk.Orientation
import org.gtkkn.extensions.gio.runApplication
import org.gtkkn.extensions.glib.util.log.Log
import org.gtkkn.extensions.glib.util.log.writer.installConsoleLogWriter
import org.gtkkn.extensions.gtk.setMargins

fun main() {
    Log.installConsoleLogWriter()

    val app = Application("orc.zdertis420.termulator", ApplicationFlags.FLAGS_NONE)
    app.onActivate {
        val window = ApplicationWindow(app)
        window.title = "GTK: Ввод и Консоль (Только Код)"
        window.setDefaultSize(350, 150)

        val box = Box(Orientation.VERTICAL, 12)

        box.setMargins(20)

        val entry = Entry()
        entry.placeholderText = "Введите текст и нажмите Enter"

        box.append(entry)

        window.child = box

        val handleInput = {
            val text: String = entry.getText()
            println("=== Введенный текст: $text ===")
            entry.setText("")
        }

        entry.onActivate { handleInput() }

        window.present()
    }

    app.runApplication()
}