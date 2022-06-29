package liltrip.gencore.data;

import liltrip.gencore.GenCore;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

public class PlayerConfig {

    UUID uuid;
    File userFile;
    FileConfiguration userConfig;

    public PlayerConfig(UUID uuid) {
        this.uuid = uuid;
        this.userFile = new File(GenCore.getInstance().getDataFolder(), uuid + ".yml");
        this.userConfig = YamlConfiguration.loadConfiguration(this.userFile);
    }

    public void createUser(UUID uuid) {
        try {
            YamlConfiguration uc = YamlConfiguration.loadConfiguration(this.userFile);
            if(!uc.contains("gen.slots"))
                uc.set("gen.slots", 10);
            if(!uc.contains("gen.locations"))
                uc.createSection("gen.locations");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public FileConfiguration getUserFile() { return this.userConfig; }

    public void saveUserFile() {
        try {
            getUserFile().save(this.userFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
