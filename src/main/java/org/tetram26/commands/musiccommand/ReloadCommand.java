package org.tetram26.commands.musiccommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.tetram26.commands.basecommand.MusicCommand;
import org.tetram26.plugin.MusicPlayerPlugin;

import java.util.List;

public class ReloadCommand extends MusicCommand {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        MusicPlayerPlugin plugin = MusicPlayerPlugin.getInstance();
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        sender.sendMessage(plugin.getConfig().getRichMessage("reloadConfig", Component.text("Reloaded config", NamedTextColor.GREEN)));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
        return List.of();
    }
}
