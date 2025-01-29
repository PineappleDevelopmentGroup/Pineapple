package sh.miles.pineapple.command.internal.debug;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.PineappleLib;
import sh.miles.pineapple.chat.PineappleChat;
import sh.miles.pineapple.command.Command;
import sh.miles.pineapple.command.CommandLabel;

import java.util.HashMap;
import java.util.Map;

class DebugVersionCommand extends Command {

    private final Plugin plugin;

    public DebugVersionCommand(Plugin plugin) {
        super(new CommandLabel("version", "pineapple-lib.command.debug.version"));
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        Map<String, Object> replacements = new HashMap<>();

        replacements.put("runtime_version", Runtime.version().version().get(0));
        replacements.put("minecraft_version", Bukkit.getBukkitVersion());
        replacements.put("server_brand", Bukkit.getName());
        replacements.put("server_git_hash", Bukkit.getVersion());
        replacements.put("pineapple_version", PineappleLib.getVersion());
        replacements.put("plugin_version", this.plugin.getDescription().getVersion());
        replacements.put("plugin_name", this.plugin.getName());

        sender.spigot().sendMessage(PineappleChat.parse("""
                <color:#d6aa1a>Pineapple Versions
                <color:#d6aa1a>Server Info:
                <color:#f7ff19>- Java Version: <gray><$runtime_version>
                <color:#f7ff19>- Minecraft Version: <gray><$minecraft_version>
                <color:#f7ff19>- Server Brand: <gray><$server_brand>
                <color:#f7ff19>- Server GitHash: <gray><$server_git_hash>
                <color:#d6aa1a>Plugin Info:
                <color:#f7ff19>- Pineapple Version: <gray><$pineapple_version>
                <color:#f7ff19>- Plugin Version: <gray><$plugin_version>
                <color:#f7ff19>- Plugin Name: <gray><$plugin_name>
                """, replacements));
        return true;
    }
}
