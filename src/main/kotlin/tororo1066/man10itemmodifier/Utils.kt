package tororo1066.man10itemmodifier

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import tororo1066.commandapi.SCommandV2Data
import tororo1066.tororopluginapi.SStr
import tororo1066.tororopluginapi.utils.sendMessage
import java.io.File
import java.net.JarURLConnection
import java.net.URISyntaxException
import java.net.URL
import java.util.jar.JarFile

object Utils {

    fun CommandSender.sendPrefixMsg(message: SStr) {
        this.sendMessage(Man10ItemModifier.prefix + message)
    }

    fun getClasses(url: URL, packageName: String): List<Class<*>> {
        val classes = ArrayList<Class<*>>()
        val src = ArrayList<File>()
        val srcFile = try {
            File(url.toURI())
        } catch (_: IllegalArgumentException) {
            File((url.openConnection() as JarURLConnection).jarFileURL.toURI())
        } catch (_: URISyntaxException) {
            File(url.path)
        }

        src += srcFile

        src.forEach { s ->
            JarFile(s).stream().filter { it.name.endsWith(".class") }.forEach second@ {
                val name = it.name.replace('/', '.').substring(0, it.name.length - 6)
                if (!name.contains(packageName)) return@second

                kotlin.runCatching {
                    classes.add(Class.forName(name, false, Man10ItemModifier::class.java.classLoader))
                }
            }
        }

        return classes
    }

    fun getItemInMainHand(data: SCommandV2Data, message: Boolean = true): ItemStack? {
        val player = data.sender as? Player ?: return null
        val item = player.inventory.itemInMainHand
        if (item.type.isAir) {
            if (message) {
                player.sendPrefixMsg(SStr("&cアイテムを手に持ってください"))
            }
            return null
        }
        return item
    }
}