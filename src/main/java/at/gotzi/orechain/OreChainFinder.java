package at.gotzi.orechain;

import at.gotzi.orechain.listener.BreakBlockListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class OreChainFinder extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new BreakBlockListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
