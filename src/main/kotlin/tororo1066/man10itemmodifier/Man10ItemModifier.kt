package tororo1066.man10itemmodifier

import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import tororo1066.tororopluginapi.SJavaPlugin
import tororo1066.tororopluginapi.SStr

class Man10ItemModifier: SJavaPlugin() {

    companion object {
        val prefix = SStr("&e[&dMan10&bItemModifier&e]&r")

        val legacyComponentSerializer = LegacyComponentSerializer.builder().hexColors().build()
        val miniMessage = MiniMessage.miniMessage()
    }

    override fun onStart() {
        Command()
    }

    override fun onEnd() {

    }
}