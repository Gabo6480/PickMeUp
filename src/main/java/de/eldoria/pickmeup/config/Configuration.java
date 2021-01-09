package de.eldoria.pickmeup.config;

import de.eldoria.eldoutilities.configuration.EldoConfig;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class Configuration extends EldoConfig {
    public Configuration(Plugin plugin) {
        super(plugin);
    }

    public boolean isWorldActive(World world) {
        List<String> worlds = getConfig().getStringList("worlds");
        if (getConfig().getBoolean("blacklist", false)) {
            return !worlds.contains(world.getName());
        }
        return worlds.contains(world.getName());
    }

    public boolean canPickUpMob(EntityType type) {
        return getConfig().getStringList("allowedMobTypes").contains(type.name());
    }

    public String getLanguage() {
        return getConfig().getString("language", "en_US");
    }

    public boolean allowStacking() {
        return getConfig().getBoolean("allowStacking");
    }
}