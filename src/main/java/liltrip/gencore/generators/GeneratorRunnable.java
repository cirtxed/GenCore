package liltrip.gencore.generators;

import com.google.common.collect.Iterables;
import de.tr7zw.nbtapi.NBTBlock;
import liltrip.gencore.GenCore;
import liltrip.gencore.config.CoreConfig;
import liltrip.gencore.data.GenPlayer;
import liltrip.gencore.data.PlayerManager;
import liltrip.gencore.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Objects;

public class GeneratorRunnable implements Runnable {

    private final int id;
    PlayerManager manager = GenCore.getPlayerManager();

    public GeneratorRunnable(GenCore plugin) {
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 20*10L, 20*10L);
    }

    Boolean spawnType = CoreConfig.getLang().getBoolean("spawnLastBlock");

    @Override
    public void run() {
        for(Player online : Bukkit.getOnlinePlayers()) {
            GenPlayer genPlayer = manager.getUser(online.getUniqueId());

            if(genPlayer == null)
                return;
            if(genPlayer.getGenerators().size() <= 0)
                return;

            Location lastLocation = Iterables.getLast(genPlayer.getGenerators());
            for(Location location : genPlayer.getGenerators()) {
                Block block = location.getBlock();
                NBTBlock nbtBlock = new NBTBlock(block);
                if(spawnType) {
                    lastLocation.add(0.5, 1.25,0.5);
                    Objects.requireNonNull(lastLocation.getWorld()).dropItem(lastLocation, nbtBlock.getData().getItemStack("DROP")).setVelocity(new Vector());
                    lastLocation.subtract(0.5, 1.25, 0.5);
                }
                if(!spawnType) {
                    location.add(0.5, 1.25,0.5);
                    Objects.requireNonNull(location.getWorld()).dropItem(location, nbtBlock.getData().getItemStack("DROP")).setVelocity(new Vector());
                    location.subtract(0.5, 1.25, 0.5);
                }
            }
        }
    }
}
