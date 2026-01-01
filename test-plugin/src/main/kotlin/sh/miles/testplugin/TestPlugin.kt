package sh.miles.testplugin

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.plugin.java.JavaPlugin
import sh.miles.pineapple.PineappleLib
import sh.miles.pineapple.command.AdvancedCommand
import sh.miles.pineapple.command.CommandLabel

class TestPlugin: JavaPlugin() {

    @Suppress("UnstableApiUsage")
    override fun onEnable() {
        PineappleLib.initialize(this)
        PineappleLib.registerCommand(TestCommand)

        val arguments = listOf(
            Commands.argument("first", StringArgumentType.word()),
            Commands.argument("second", StringArgumentType.word()),
        )


        var chainedArguments: RequiredArgumentBuilder<CommandSourceStack, *>? = null
        for (argument in arguments.reversed()) {
            chainedArguments = if (chainedArguments == null) {
                argument.requires { source -> source.sender.isOp }.executes { ctx ->
                        val first = StringArgumentType.getString(ctx, "first")
                        val second = StringArgumentType.getString(ctx, "second")
                        ctx.source.sender.sendRichMessage(
                            "<gold>Test $first $second"
                        )
                        return@executes 1
                    }
            } else {
                argument.then(chainedArguments)
            }
        }

        val finalCommand = Commands.literal("egg").then(chainedArguments)



        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { commands ->
            commands.registrar().register(finalCommand.build())
            commands.registrar().register(
                Commands.literal("testcmd").then(
                    Commands.argument("first", StringArgumentType.word()).then(
                        Commands.argument("second", StringArgumentType.word()).requires { source -> source.sender.isOp }
                            .executes { ctx ->
                                val first = StringArgumentType.getString(ctx, "first")
                                val second = StringArgumentType.getString(ctx, "second")
                                ctx.source.executor.sendRichMessage(
                                    "<gold>Test $first $second"
                                )
                                return@executes 1
                            })
                ).build()
            )
        }


    }
}

object TestCommand: AdvancedCommand(CommandLabel("testcommand", "test-plugin.command.test")) {

    init {
        registerSubcommand(FirstCommand)
        registerSubcommand(SecondCommand)
    }

    object FirstCommand: AdvancedCommand(CommandLabel("first", super.commandLabel.permission + ".first")) {

        init {
            registerArgument("third", StringArgumentType.word())
            registerArgument("fourth", StringArgumentType.word())
            registerArgument("fifth", StringArgumentType.word())
        }

        override fun execute(context: CommandContext<CommandSourceStack>) {
            val sender = context.source.executor!!

            sender.sendRichMessage(
                "<green>Executed first command! with ${
                    StringArgumentType.getString(
                        context, "third"
                    )
                } ${StringArgumentType.getString(context, "fourth")}"
            )
        }

    }

    object SecondCommand: AdvancedCommand(CommandLabel("second", super.commandLabel.permission + ".second")) {
        override fun execute(context: CommandContext<CommandSourceStack>) {
            val sender = context.source.executor!!

            sender.sendRichMessage("<green>Executed second command!")
        }
    }
}
