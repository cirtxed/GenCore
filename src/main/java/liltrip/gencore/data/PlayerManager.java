package liltrip.gencore.data;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class PlayerManager {

    public Map<UUID, GenPlayer> players = new HashMap<>();

    public GenPlayer getUser(UUID uuid) {
        for(GenPlayer player : players.values()) {
            if(player.getUser().equals(uuid))
                return player;
        }
        return null;
    }

    public void addPlayer(GenPlayer player) { players.put(player.getUser(), player); }

    public void removePlayer(UUID uuid) { players.remove(uuid); }

    public void savePlayer(UUID uuid) {
        PlayerConfig playerConfig = new PlayerConfig(uuid);
        FileConfiguration file = playerConfig.getUserFile();
        GenPlayer player = getUser(uuid);
        file.set("gen.slots", player.getGenSlots());
        int index = 0;
        if(player.getGenerators() != null) {
            for(Location location : player.getGenerators()) {
                file.set("gen.locations." + index, location);
                index++;
            }
        }
        playerConfig.saveUserFile();
    }

    public void loadPlayer(UUID uuid) {
        PlayerConfig playerConfig = new PlayerConfig(uuid);
        FileConfiguration file = playerConfig.getUserFile();
        GenPlayer player = getUser(uuid);
        List<Location> gens = new ArrayList<>();
        if(file.getConfigurationSection("gen.locations") != null) {
            for(String key : Objects.requireNonNull(file.getConfigurationSection("gen.locations")).getKeys(false)) {
                gens.add(file.getLocation("gen.locations." + key));
            }
        }

        player.setGenSlots(file.getLong("gen.slots"));
        player.setGenerators(gens);
    }
}
