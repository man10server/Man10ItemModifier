package tororo1066.man10itemmodifier.modifies

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.inventory.ItemStack
import tororo1066.commandapi.CommandArguments
import tororo1066.commandapi.SCommandV2Arg
import tororo1066.commandapi.argumentType.BooleanArg
import tororo1066.commandapi.argumentType.FloatArg
import tororo1066.commandapi.argumentType.StringArg
import tororo1066.man10itemmodifier.AbstractModify
import tororo1066.man10itemmodifier.Man10ItemModifier
import tororo1066.man10itemmodifier.Utils
import tororo1066.man10itemmodifier.Utils.sendPrefixMsg
import tororo1066.tororopluginapi.SStr
import tororo1066.tororopluginapi.utils.sendMessage

@Suppress("UnstableApiUsage")
class CustomModelDataModify: AbstractModify() {

    companion object {
        const val WIKI_URL = "https://ja.minecraft.wiki/w/%E3%83%87%E3%83%BC%E3%82%BF%E3%82%B3%E3%83%B3%E3%83%9D%E3%83%BC%E3%83%8D%E3%83%B3%E3%83%88#custom_model_data"
    }

    override val name = "customModelData"
    override val aliases = listOf<String>()

    override fun shortHelp(): Component {
        return Component.text("カスタムモデルデータを変更します")
    }

    override fun longHelp(): Component {
        return Man10ItemModifier.miniMessage.deserialize(
            """
            <green>アイテムのカスタムモデルデータを変更します。
            <green>floats, flags, strings, colorsの4種類のデータを管理できます。
            <green>詳細は<yellow><u><click:open_url:"$WIKI_URL">[Wikiページ]</click></u><green>を参照してください。
            <yellow>使用例：
            <gray>/mim customModelData info
            <gray>/mim customModelData floats add 1.5
            <gray>/mim customModelData flags remove true
            <gray>/mim customModelData strings clear
            <gray>/mim customModelData colors add #FF0000
        """.trimIndent()
        )
    }

    override fun SCommandV2Arg.createCommands(): List<SCommandV2Arg> {
        val infoArg = literal("info") {
            setPlayerExecutor { data ->
                val sender = data.sender
                val item = Utils.getItemInMainHand(data) ?: return@setPlayerExecutor
                val component = item.getData(DataComponentTypes.CUSTOM_MODEL_DATA) ?: run {
                    sender.sendPrefixMsg(SStr("&cこのアイテムにはカスタムモデルデータが設定されていません"))
                    return@setPlayerExecutor
                }
                val floats = component.floats()
                sender.sendMessage(SStr("&bfloats: ${if (floats.isEmpty()) "なし" else floats.joinToString(", ")}"))
                val flags = component.flags()
                sender.sendMessage(SStr("&cflags: ${if (flags.isEmpty()) "なし" else flags.joinToString(", ")}"))
                val strings = component.strings()
                sender.sendMessage(SStr("&astrings: ${if (strings.isEmpty()) "なし" else strings.joinToString(", ")}"))
                val colors = component.colors()
                sender.sendMessage(SStr("&6colors: ${if (colors.isEmpty()) "なし" else colors.joinToString(", ")}"))
            }
        }

        val floatsArg = literal("floats") {
            literal("add") {
                argument("値", FloatArg()) {
                    setPlayerExecutor { data ->
                        val item = Utils.getItemInMainHand(data) ?: return@setPlayerExecutor
                        val value = data.args.getArgument("値", Float::class.java)
                        item.modifyCustomModelData {
                            floats.add(value)
                        }
                        data.sender.sendPrefixMsg(SStr("&aカスタムモデルデータのfloat値を追加しました"))
                    }
                }
            }

            literal("remove") {
                argument("値", FloatArg()) {
                    setPlayerExecutor { data ->
                        val item = Utils.getItemInMainHand(data) ?: return@setPlayerExecutor
                        val value = data.args.getArgument("値", Float::class.java)
                        item.modifyCustomModelData {
                            floats.remove(value)
                        }
                        data.sender.sendPrefixMsg(SStr("&aカスタムモデルデータのfloat値を削除しました"))
                    }
                }
            }

            literal("clear") {
                setPlayerExecutor { data ->
                    val item = Utils.getItemInMainHand(data) ?: return@setPlayerExecutor
                    item.modifyCustomModelData {
                        floats.clear()
                    }
                    data.sender.sendPrefixMsg(SStr("&aカスタムモデルデータのfloat値をクリアしました"))
                }
            }
        }

        val flagsArg = literal("flags") {
            literal("add") {
                argument("値", BooleanArg()) {
                    setPlayerExecutor { data ->
                        val item = Utils.getItemInMainHand(data) ?: return@setPlayerExecutor
                        val value = data.args.getArgument("値", Boolean::class.java)
                        item.modifyCustomModelData {
                            flags.add(value)
                        }
                        data.sender.sendPrefixMsg(SStr("&aカスタムモデルデータのflag値を追加しました"))
                    }
                }
            }

            literal("remove") {
                argument("値", BooleanArg()) {
                    setPlayerExecutor { data ->
                        val item = Utils.getItemInMainHand(data) ?: return@setPlayerExecutor
                        val value = data.args.getArgument("値", Boolean::class.java)
                        item.modifyCustomModelData {
                            flags.remove(value)
                        }
                        data.sender.sendPrefixMsg(SStr("&aカスタムモデルデータのflag値を削除しました"))
                    }
                }
            }

            literal("clear") {
                setPlayerExecutor { data ->
                    val item = Utils.getItemInMainHand(data) ?: return@setPlayerExecutor
                    item.modifyCustomModelData {
                        flags.clear()
                    }
                    data.sender.sendPrefixMsg(SStr("&aカスタムモデルデータのflag値をクリアしました"))
                }
            }
        }

        val stringsArg = literal("strings") {
            literal("add") {
                argument("値", StringArg.greedyPhrase()) {
                    setPlayerExecutor { data ->
                        val item = Utils.getItemInMainHand(data) ?: return@setPlayerExecutor
                        val value = data.args.getArgument("値", String::class.java)
                        item.modifyCustomModelData {
                            strings.add(value)
                        }
                        data.sender.sendPrefixMsg(SStr("&aカスタムモデルデータのstring値を追加しました"))
                    }
                }
            }

            literal("remove") {
                argument("値", StringArg.greedyPhrase()) {
                    setPlayerExecutor { data ->
                        val item = Utils.getItemInMainHand(data) ?: return@setPlayerExecutor
                        val value = data.args.getArgument("値", String::class.java)
                        item.modifyCustomModelData {
                            strings.remove(value)
                        }
                        data.sender.sendPrefixMsg(SStr("&aカスタムモデルデータのstring値を削除しました"))
                    }
                }
            }

            literal("clear") {
                setPlayerExecutor { data ->
                    val item = Utils.getItemInMainHand(data) ?: return@setPlayerExecutor
                    item.modifyCustomModelData {
                        strings.clear()
                    }
                    data.sender.sendPrefixMsg(SStr("&aカスタムモデルデータのstring値をクリアしました"))
                }
            }
        }

        val colorsArg = literal("colors") {
            literal("add") {
                argument("値", StringArg.greedyPhrase()) {
                    setPlayerExecutor { data ->
                        val item = Utils.getItemInMainHand(data) ?: return@setPlayerExecutor
                        val value = data.args.getArgument("値", String::class.java)
                        val color = Color.fromRGB(Integer.parseInt(value.replace("#", ""), 16))
                        item.modifyCustomModelData {
                            colors.add(color)
                        }
                        data.sender.sendPrefixMsg(SStr("&aカスタムモデルデータのcolor値を追加しました"))
                    }
                }
            }

            literal("remove") {
                argument("値", StringArg.greedyPhrase()) {
                    setPlayerExecutor { data ->
                        val item = Utils.getItemInMainHand(data) ?: return@setPlayerExecutor
                        val value = data.args.getArgument("値", String::class.java)
                        val color = Color.fromRGB(Integer.parseInt(value.replace("#", ""), 16))
                        item.modifyCustomModelData {
                            colors.remove(color)
                        }
                        data.sender.sendPrefixMsg(SStr("&aカスタムモデルデータのcolor値を削除しました"))
                    }
                }
            }

            literal("clear") {
                setPlayerExecutor { data ->
                    val item = Utils.getItemInMainHand(data) ?: return@setPlayerExecutor
                    item.modifyCustomModelData {
                        colors.clear()
                    }
                    data.sender.sendPrefixMsg(SStr("&aカスタムモデルデータのcolor値をクリアしました"))
                }
            }
        }

        return listOf(infoArg, floatsArg, flagsArg, stringsArg, colorsArg)
    }

    private data class CustomModelDataValues(
        val floats: MutableList<Float> = mutableListOf(),
        val flags: MutableList<Boolean> = mutableListOf(),
        val strings: MutableList<String> = mutableListOf(),
        val colors: MutableList<Color> = mutableListOf()
    )

    private fun ItemStack.modifyCustomModelData(modify: CustomModelDataValues.() -> Unit) {
        val current = this.getData(DataComponentTypes.CUSTOM_MODEL_DATA)
        val values = CustomModelDataValues()
        if (current != null) {
            values.floats.addAll(current.floats())
            values.flags.addAll(current.flags())
            values.strings.addAll(current.strings())
            values.colors.addAll(current.colors())
        }
        values.modify()
        val builder = CustomModelData.customModelData()
        builder.addFloats(values.floats)
        builder.addFlags(values.flags)
        builder.addStrings(values.strings)
        builder.addColors(values.colors)
        this.setData(DataComponentTypes.CUSTOM_MODEL_DATA, builder.build())
    }
}