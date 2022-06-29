package liltrip.gencore.generators;

import de.tr7zw.nbtapi.NBTBlock;
import liltrip.gencore.GenCore;
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

public class GeneratorRunnable implements Runnable {

    private final int id;
    PlayerManager manager = GenCore.getPlayerManager();

    public GeneratorRunnable(GenCore plugin) {
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 40, 40);
    }

    @Override
    public void run() {
        for(Player online : Bukkit.getOnlinePlayers()) {
            GenPlayer genPlayer = manager.getUser(online.getUniqueId());
            if(genPlayer == null)
                return;
            for(Location location : genPlayer.getGenerators()) {
                Block block = location.getBlock();
                NBTBlock nbtBlock = new NBTBlock(block);
                location.add(0.5, 1.25,0.5);
                location.getWorld().dropItem(location, ItemBuilder.createItem(new ItemStack(Material.EMERALD), "GENERATOR DROP",
                        new String[]{ChatColor.WHITE + "[/sell]"})).setVelocity(new Vector());
                location.subtract(0.5, 1.25, 0.5);
            }
        }
    }
}
