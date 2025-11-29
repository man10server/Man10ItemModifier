package tororo1066.man10itemmodifier

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import tororo1066.commandapi.SCommandV2Arg
import tororo1066.commandapi.SCommandV2Data
import tororo1066.tororopluginapi.annotation.SCommandV2Body
import tororo1066.tororopluginapi.sCommand.v2.SCommandV2
import java.lang.reflect.Modifier

class Command: SCommandV2("mim") {

    companion object {
        val HELP_MESSAGE = Man10ItemModifier.miniMessage.deserialize(
            """
                <gold>================= Man10ItemModifier =================
                <red>※プレイヤーのみ実行できます
                <aqua>引数なしでコマンドを実行するとそのコマンドのヘルプが表示されます。
                
            """.trimIndent()
        )
    }

    val modifies = hashMapOf<String, AbstractModify>()
    val commandCache = mutableMapOf<String, List<SCommandV2Arg>>()

    private fun showHelp(data: SCommandV2Data) {
        val message = HELP_MESSAGE
        val append = Component.join(
            JoinConfiguration.separator(Component.text("\n")),
            modifies.values.map { Component.text("/mim ${it.name} ").append(it.shortHelp()) }
        )
        data.sender.sendMessage(message.append(append))
    }

    private fun showModifyHelp(data: SCommandV2Data, modify: AbstractModify) {
        val message = Man10ItemModifier.miniMessage.deserialize(
            "<red>代用可能: ${modify.aliases.joinToString(", ")}\n"
        ).append(modify.longHelp())
        data.sender.sendMessage(message)
    }

    init {
        root.setPermission("man10itemmodifier.op")

        root.setExecutor(::showHelp)

        val classes = Utils.getClasses(this::class.java.protectionDomain.codeSource.location, "tororo1066.man10itemmodifier.modifies")
        classes.forEach { clazz ->
            if (AbstractModify::class.java.isAssignableFrom(clazz) && !clazz.isInterface && !Modifier.isAbstract(clazz.modifiers)) {
                val modify = clazz.getDeclaredConstructor().newInstance() as AbstractModify
                modifies[modify.name] = modify
            }
        }
    }

    @SCommandV2Body
    val mimCommands = command {
        setPermission("man10itemmodifier.op")

        literal("help") {
            setExecutor(::showHelp)
        }

        modifies.forEach { (name, modify) ->
            literal(name) {
                setExecutor { data ->
                    showModifyHelp(data, modify)
                }

                modify.run {
                    commandCache.getOrPut(name) {
                        createCommands()
                    }
                }
            }
        }
    }

    @SCommandV2Body(asRoot = true)
    val rootCommands = command {
        setPermission("man10itemmodifier.op")

        modifies.forEach { (name, modify) ->
            modify.aliases.forEach { alias ->
                literal(alias) {
                    setExecutor { data ->
                        showModifyHelp(data, modify)
                    }

                    commandCache[name]?.forEach {
                        arg(it)
                    }
                }
            }
        }
    }
}