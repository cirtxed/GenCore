package liltrip.gencore.data;

import liltrip.gencore.GenCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerEvents implements Listener {
    PlayerManager playerManager = GenCore.getPlayerManager();

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.playerManager.savePlayer(event.getPlayer().getUniqueId());
        this.playerManager.removePlayer(event.getPlayer().getUniqueId());
        event.setQuitMessage("");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = Bukkit.getPlayer(event.getPlayer().getUniqueId());
        UUID uuid = event.getPlayer().getUniqueId();
        PlayerConfig playerConfig = new PlayerConfig(uuid);
        GenPlayer genPlayer = new GenPlayer(uuid);
        playerConfig.createUser(uuid);
        this.playerManager.addPlayer(genPlayer);
        this.playerManager.loadPlayer(uuid);
    }
}
