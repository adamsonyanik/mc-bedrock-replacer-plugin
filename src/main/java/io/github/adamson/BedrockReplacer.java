package io.github.adamson;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BedrockReplacer extends JavaPlugin implements Listener {
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new BedrockBreakEventListener(), this);
    }
}
