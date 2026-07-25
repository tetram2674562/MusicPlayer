package org.tetram26.world;

import org.bukkit.Location;
import org.tetram26.controller.Controller;

public class Speaker {
    private final String threadName;
    private final Location location;
    private final Controller controller;

    public Speaker(String threadName, Location location, Controller controller) {
        this.threadName = threadName;
        this.location = location;
        this.controller = controller;
    }

    public void unload() {
        controller.stop(threadName);
        controller.removeThread(threadName);
    }

    public Location getLoc() {
        return location;
    }
}
