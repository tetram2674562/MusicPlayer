package org.tetram26.commands.musiccommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.tetram26.commands.basecommand.MusicCommand;
import org.tetram26.world.SpeakerManager;

import java.util.List;

public class SpeakerCommand extends MusicCommand {

    private SpeakerManager manager;

    public SpeakerCommand(SpeakerManager speakerManager) {
        this.manager = speakerManager;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("You must be a player to execute this command", NamedTextColor.DARK_RED));
            return true;
        }

        if (!sender.hasPermission("musicplayer.speaker")) {
            sender.sendMessage(Component.text("Invalid permission", NamedTextColor.DARK_RED));
        }

        if (args.length < 1) {
            sender.sendMessage(Component.text("Invalid argument", NamedTextColor.DARK_RED));
            return true;
        }

        ItemStack item = manager.getItem(args[0]);
        ((Player) sender).getInventory().addItem(item);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
        return switch (args.length) {
            case 0 -> manager.getSpeakerTypes();
            case 1 -> manager.getSpeakerTypes().stream().filter(speaker -> speaker.startsWith(args[0])).toList();
            default -> List.of();
        };
    }
}
