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
import org.tetram26.controller.Controller;
import org.tetram26.world.SpeakerManager;

import java.util.List;

public class SpeakerCommand extends MusicCommand {

    private final Controller controller;

    public SpeakerCommand(Controller controller) {
        this.controller = controller;
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

        ItemStack item = controller.getSpeakerManager().getItem(args[0]);
        ((Player) sender).getInventory().addItem(item);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NonNull @NotNull String[] args) {
        return switch (args.length) {
            case 0 -> controller.getSpeakerManager().getSpeakerTypes();
            case 1 -> controller.getSpeakerManager().getSpeakerTypes().stream().filter(speaker -> speaker.startsWith(args[0])).toList();
            default -> List.of();
        };
    }
}
