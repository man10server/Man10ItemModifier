package tororo1066.man10itemmodifier

import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack
import tororo1066.commandapi.CommandArguments
import tororo1066.commandapi.SCommandV2Arg
import tororo1066.commandapi.SCommandV2Data
import tororo1066.commandapi.argumentType.StringArg
import tororo1066.man10itemmodifier.Utils.sendPrefixMsg
import tororo1066.tororopluginapi.SStr

abstract class AbstractComponentableModify: AbstractModify() {

    companion object {
        const val COMPONENT = "コンポーネント"
        const val STRING = "文字列"
        const val MINI_MESSAGE_URL = "https://docs.papermc.io/adventure/minimessage/format/"
    }

    abstract fun modify(itemStack: ItemStack, args: CommandArguments)

    abstract fun suggests(itemStack: ItemStack): Component?

    override fun SCommandV2Arg.createCommands(): List<SCommandV2Arg> {

        fun modify(data: SCommandV2Data) {
            val itemStack = Utils.getItemInMainHand(data) ?: return
            modify(itemStack, data.args)
            data.sender.sendPrefixMsg(SStr("&aアイテムを変更しました"))
        }

        val legacyArg = argument(STRING, StringArg.greedyPhrase()) {
            suggest { data ->
                suggestsLegacyComponent(data) { itemStack ->
                    suggests(itemStack)
                }
            }
            setPlayerExecutor(::modify)
        }
        val miniMessageArg = literal("miniMessage") {
            argument(COMPONENT, StringArg.greedyPhrase()) {
                suggest { data ->
                    suggestsMiniMessageComponent(data) { itemStack ->
                        suggests(itemStack)
                    }
                }
                setPlayerExecutor(::modify)
            }
        }
        return listOf(legacyArg, miniMessageArg)
    }

    protected fun CommandArguments.getComponent(): Component {
        val miniMessageArg = getNullableArgument(COMPONENT, String::class.java)
        if (miniMessageArg != null) {
            return Man10ItemModifier.miniMessage.deserialize(miniMessageArg)
        }
        val legacyArg = getArgument(STRING, String::class.java).replace("&", "§")
        return Man10ItemModifier.legacyComponentSerializer.deserialize(legacyArg)
    }

    protected fun CommandArguments.getComponents(): List<Component> {
        val miniMessageArg = getNullableArgument(COMPONENT, String::class.java)
        if (miniMessageArg != null) {
            return miniMessageArg.split("\\n").map {
                Man10ItemModifier.miniMessage.deserialize(it)
            }
        }
        val legacyArg = getArgument(STRING, String::class.java).replace("&", "§")
        return legacyArg.split("\\n").map {
            Man10ItemModifier.legacyComponentSerializer.deserialize(it)
        }
    }
}