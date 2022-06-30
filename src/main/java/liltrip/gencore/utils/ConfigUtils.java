package liltrip.gencore.utils;

import liltrip.gencore.GenCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigUtils {

    public static File getFile(String name) { return new File(GenCore.getInstance().getDataFolder(), name); }

    public static File getFile(String path, String name) {
        return new File(GenCore.getInstance().getDataFolder()  + File.separator + path, name);
    }

    public static FileConfiguration getConfigFile(String name) {
        GenCore.getInstance().getDataFolder().mkdirs();
        File file = new File(GenCore.getInstance().getDataFolder(), name);
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            GenCore.getInstance().saveResource(name, false);
        }
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    public static FileConfiguration getConfigFile(String path, String name) {
        File file = new File(GenCore.getInstance().getDataFolder() + File.separator + path, name);
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            GenCore.getInstance().saveResource(path + File.separator + name, false);
        }
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }
}
