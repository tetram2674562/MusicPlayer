package org.tetram26.commands.basecommand;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MusicCommand implements CommandExecutor, TabCompleter {

    protected ConcurrentHashMap<String,MusicCommand> subCommands = new ConcurrentHashMap<>();


    public MusicCommand() {}

    public MusicCommand(Map<String, MusicCommand> commands) {
        subCommands.putAll(commands);
    }
}
