package tororo1066.man10itemmodifier.modifies

import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack
import tororo1066.commandapi.CommandArguments
import tororo1066.man10itemmodifier.AbstractComponentableModify
import tororo1066.man10itemmodifier.Man10ItemModifier

class CustomNameModify: AbstractComponentableModify() {
    override val name = "customName"
    override val aliases = listOf("rename")

    override fun shortHelp(): Component {
        return Component.text("アイテムの表示名を変更します")
    }

    override fun longHelp(): Component {
        return Man10ItemModifier.miniMessage.deserialize(
            """
            <green>アイテムの表示名を変更します。
            <aqua>レガシー形式(&記号使用)<green>と<yellow><u><click:open_url:"$MINI_MESSAGE_URL">[MiniMessage形式]</click></u><green>に対応しています。
            <yellow>使用例：
            <gray>/mim customName &6黄金の剣
            <gray>/mim customName miniMessage \<gold>黄金の剣
            """.trimIndent()
        )
    }

    override fun modify(itemStack: ItemStack, args: CommandArguments) {
        val component = args.getComponent()
        itemStack.editMeta {
            it.displayName(component)
        }
    }

    override fun suggests(itemStack: ItemStack): Component? {
        val itemMeta = if (itemStack.hasItemMeta()) itemStack.itemMeta else return null
        return itemMeta.displayName()
    }
}