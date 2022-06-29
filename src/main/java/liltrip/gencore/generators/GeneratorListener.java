package liltrip.gencore.generators;

import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.NBTItem;
import liltrip.gencore.GenCore;
import liltrip.gencore.data.GenPlayer;
import liltrip.gencore.data.PlayerManager;
import liltrip.gencore.utils.chat.ColorUtils;
import liltrip.gencore.utils.item.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GeneratorListener implements Listener {

    PlayerManager manager = GenCore.getPlayerManager();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void placeGenerator(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        NBTBlock nbtBlock = new NBTBlock(event.getBlock());
        NBTItem handNBT = new NBTItem(event.getItemInHand());

        GenPlayer genPlayer = manager.getUser(player.getUniqueId());
        if(genPlayer.getGenerators().size()>= genPlayer.getGenSlots()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText("§aGenerator slots full! §7(" + genPlayer.getGenerators().size() + ":" + genPlayer.getGenSlots() + ")"));
            event.setCancelled(true);
            return;
        }

        nbtBlock.getData().setBoolean("GENERATOR", true);
        nbtBlock.getData().setInteger("TIER", handNBT.getInteger("TIER"));
        Bukkit.broadcastMessage(nbtBlock.getData().getInteger("TIER").toString());
        //Bukkit.broadcastMessage(genPlayer.getGenSlots() + " : " + genPlayer.getGenerators());
        genPlayer.addGenerator(event.getBlock().getLocation());
        //Bukkit.broadcastMessage(genPlayer.getGenerators().toString());
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText("§aGenerator Placed! §7(" + genPlayer.getGenerators().size() + ":" + genPlayer.getGenSlots() + ")"));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);

    }

    @EventHandler
    public void breakGenerator(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK)
            return;
        if(event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.AIR)
            return;
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        NBTBlock nbtBlock = new NBTBlock(block);
        if(!nbtBlock.getData().hasKey("GENERATOR"))
            return;
        GenPlayer genPlayer = manager.getUser(player.getUniqueId());
        if (!genPlayer.getGenerators().contains(block.getLocation()))
            return;

        player.getInventory().addItem(GiveGenerator(block));
        player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
        genPlayer.removeGenerator(block.getLocation());
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText("§aGenerator Broken! §7(" + genPlayer.getGenerators().size() + ":" + genPlayer.getGenSlots() + ")"));
        block.setType(Material.AIR);
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent event) {
        NBTBlock nbtBlock = new NBTBlock(event.getBlock());
        if(nbtBlock.getData().hasKey("GENERATOR")) {
            event.setCancelled(true);
            event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText("§aYou cannot break generators!"));
        }
    }

    //@EventHandler
    //public void upgradeGenerator(PlayerInteractEvent event) {
    //    Player player = event.getPlayer();
    //    if(!(event.getAction() == Action.RIGHT_CLICK_BLOCK && player.isSneaking() && event.getHand() == EquipmentSlot.HAND))
    //        return;
    //    Block block = event.getClickedBlock();
    //    assert block != null;
    //    if(block.getType() == Material.AIR)
    //        return;
    //    NBTBlock nbtBlock = new NBTBlock(block);
    //    //- Check if generator is max!
    //    if(nbtBlock.getData().getInteger("TIER") == 10) {
    //        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
    //                TextComponent.fromLegacyText("§aGenerator is already max level!"));
    //        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
    //        return;
    //    }
//
//
    //}


    //MAKE INTO A UTIL THING LATER
    public ItemStack GiveGenerator(Block block) {
        NBTBlock nbtBlock = new NBTBlock(block);
        ItemStack generator = ItemBuilder.createItem(new ItemStack(block.getType()),
                ColorUtils.translateAlternateColorCodes('&', "&a&l" + block.getType().name() + " GENERATOR"),
                new String[]{"TIER: " + nbtBlock.getData().getInteger("TIER"),
                        "PLACE GENERATOR TO USE"});
        //ItemMeta meta = generator.getItemMeta();
        //assert meta != null;
        //meta.setDisplayName(ChatColor.GREEN + block.getType().getData().getName() + " GENERATOR");
        //meta.setLore(Arrays.asList(ColorUtils.translateAlternateColorCodes('&', "&7TIER:&6 " + nbtBlock.getData().getInteger("TIER")), "PLACE GENERATOR TO USE!"));
        //generator.setItemMeta(meta);
        NBTItem nbtItem = new NBTItem(generator);
        nbtItem.setInteger("TIER", nbtBlock.getData().getInteger("TIER"));
        nbtItem.setBoolean("GENERATOR", true);
        nbtItem.applyNBT(generator);
        return generator;
    }
}
