package tororo1066.man10itemmodifier.modifies

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import org.bukkit.inventory.ItemStack
import tororo1066.commandapi.CommandArguments
import tororo1066.man10itemmodifier.AbstractComponentableModify
import tororo1066.man10itemmodifier.Man10ItemModifier

class LoreModify: AbstractComponentableModify() {
    override val name = "lore"
    override val aliases = listOf("relore")

    override fun shortHelp(): Component {
        return Component.text("アイテムの説明文を変更します")
    }

    override fun longHelp(): Component {
        return Man10ItemModifier.miniMessage.deserialize(
            """
            <green>アイテムの説明文を変更します。
            <green>\nで改行も可能です。
            <aqua>レガシー形式(&記号使用)<green>と<yellow><u><click:open_url:"$MINI_MESSAGE_URL">[MiniMessage形式]</click></u><green>に対応しています。
            <green>(MiniMessageの場合でも\nで改行してください)
            <yellow>使用例：
            <gray>/mim lore &71行目\n&72行目
            <gray>/mim lore miniMessage \<gray>1行目\n\<gray>2行目 
            """.trimIndent()
        )
    }

    override fun modify(itemStack: ItemStack, args: CommandArguments) {
        val components = args.getComponents()
        itemStack.editMeta {
            it.lore(components)
        }
    }

    override fun suggests(itemStack: ItemStack): Component? {
        val meta = itemStack.itemMeta ?: return null
        val lore = meta.lore() ?: return null
        return Component.join(JoinConfiguration.separator(Component.text("\n")), lore)
    }
}