package tororo1066.man10itemmodifier

import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack
import tororo1066.commandapi.CommandArguments
import tororo1066.commandapi.SCommandV2Arg
import tororo1066.commandapi.SCommandV2Data
import tororo1066.commandapi.ToolTip

abstract class AbstractModify {

    abstract val name: String
    abstract val aliases: List<String>

    abstract fun SCommandV2Arg.createCommands(): List<SCommandV2Arg>

    abstract fun shortHelp(): Component
    abstract fun longHelp(): Component

    protected fun suggestsLegacyComponent(data: SCommandV2Data, component: (ItemStack) -> Component?): List<ToolTip> {
        val item = Utils.getItemInMainHand(data, message = false) ?: return emptyList()
        val comp = component(item) ?: return emptyList()
        val serialized = Man10ItemModifier.legacyComponentSerializer.serialize(comp).replace("§", "&")
            .replace("\n", "\\n")
        return listOf(
            ToolTip(serialized, "現在のアイテムの情報 (レガシー形式)")
        )
    }

    protected fun suggestsMiniMessageComponent(data: SCommandV2Data, component: (ItemStack) -> Component?): List<ToolTip> {
        val item = Utils.getItemInMainHand(data, message = false) ?: return emptyList()
        if (item.type.isAir) return emptyList()
        val comp = component(item) ?: return emptyList()
        val serialized = Man10ItemModifier.miniMessage.serialize(comp)
            .replace("<br>", "\\n")
        return listOf(
            ToolTip(serialized, "現在のアイテムの情報 (MiniMessage形式)")
        )
    }

    protected fun SCommandV2Data.getItemInMainHand(): ItemStack? {
        return Utils.getItemInMainHand(this)
    }
}