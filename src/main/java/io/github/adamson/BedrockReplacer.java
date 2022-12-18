package io.github.adamson;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BedrockReplacer extends JavaPlugin implements Listener {
    public void onEnable() {
        getLogger().info("hello");

        getServer().getPluginManager().registerEvents(new BedrockBreakEventListener(), this);
    }

    public void onDisable() {
    }
}
