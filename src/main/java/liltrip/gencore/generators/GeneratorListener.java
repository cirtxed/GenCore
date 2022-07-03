package liltrip.gencore.generators;

import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.NBTItem;
import liltrip.gencore.GenCore;
import liltrip.gencore.config.GenConfig;
import liltrip.gencore.data.GenPlayer;
import liltrip.gencore.data.PlayerManager;
import liltrip.gencore.utils.chat.ColorUtils;
import liltrip.gencore.utils.item.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
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

import java.util.*;

public class GeneratorListener implements Listener {

    PlayerManager manager = GenCore.getPlayerManager();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void placeGenerator(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        NBTBlock nbtBlock = new NBTBlock(event.getBlock());
        NBTItem handNBT = new NBTItem(event.getItemInHand());

        if(!handNBT.hasKey("GENERATOR")) {
            return;
        }

        GenPlayer genPlayer = manager.getUser(player.getUniqueId());
        if(genPlayer.getGenerators().size()>= genPlayer.getGenSlots()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText("§aGenerator slots full! §7(" + genPlayer.getGenerators().size() + ":" + genPlayer.getGenSlots() + ")"));
            event.setCancelled(true);
            return;
        }

        nbtBlock.getData().setString("NAME", handNBT.getString("NAME"));
        nbtBlock.getData().setBoolean("GENERATOR", true);
        nbtBlock.getData().setInteger("TIER", handNBT.getInteger("TIER"));
        nbtBlock.getData().setItemStack("DROP", handNBT.getItemStack("DROP"));
        nbtBlock.getData().setInteger("UPGRADECOST", handNBT.getInteger("UPGRADECOST"));
        nbtBlock.getData().setUUID("OWNER", player.getUniqueId());
        genPlayer.addGenerator(event.getBlock().getLocation());
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText("§aGenerator Placed! §7(" + genPlayer.getGenerators().size() + ":" + genPlayer.getGenSlots() + ")"));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.MASTER, 1, 1);

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
        player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, SoundCategory.MASTER, 1, 1);
        genPlayer.removeGenerator(block.getLocation());
        nbtBlock.getData().getKeys().clear();
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

    @EventHandler
    public void upgradeGenerator(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        if(!player.isSneaking())
            return;
        if(Objects.requireNonNull(event.getHand()).name().equals("OFF_HAND"))
            return;
        Block block = event.getClickedBlock();
        if((block != null ? block.getType() : null) == Material.AIR)
            return;
        int tiers = Objects.requireNonNull(GenConfig.getGenerators().getConfigurationSection("generators.")).getKeys(false).size();
        assert block != null;
        NBTBlock nbtBlock = new NBTBlock(block);

        if(!nbtBlock.getData().hasKey("GENERATOR"))
            return;
        if(!nbtBlock.getData().getUUID("OWNER").equals(player.getUniqueId())) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText("§cYou do not own this generator!"));
            return;
        }

        //- Check if generator is max!
        if(nbtBlock.getData().getInteger("TIER") == tiers) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText("§cGenerator is already max level!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, SoundCategory.MASTER, 1, 15);
            return;
        }
        //- Check if money > data.getInteger("UPGRADE COST")
        if(!(GenCore.getEcon().getBalance(player) > nbtBlock.getData().getInteger("UPGRADECOST"))) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText("§cYou do not have enough Money!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, SoundCategory.MASTER, 1, 1);
            return;
        }
        for(String key : Objects.requireNonNull(GenConfig.getGenerators().getConfigurationSection("generators.")).getKeys(false))  {
            Integer tier = (Integer) Objects.requireNonNull(GenConfig.getGenerators().get("generators." + key + ".generator.tier"));
            if (nbtBlock.getData().getInteger("TIER") >= tier) {
                continue;
            }
            String genType = String.valueOf(GenConfig.getGenerators().get("generators." + key + ".generator.type"));
            String genDrop = String.valueOf(GenConfig.getGenerators().get("generators." + key + ".drops.type"));
            String genName = String.valueOf(GenConfig.getGenerators().get("generators." + key + ".generator.name"));
            Integer upcost = (Integer) GenConfig.getGenerators().get("generators." + key + ".generator.upgradecost");
            GenCore.getEcon().withdrawPlayer(player, nbtBlock.getData().getInteger("UPGRADECOST"));
            nbtBlock.getData().setInteger("UPGRADECOST", upcost);
            block.setType(Objects.requireNonNull(Material.matchMaterial(genType)));
            nbtBlock.getData().setString("NAME", genName);
            nbtBlock.getData().setBoolean("GENERATOR", true);
            nbtBlock.getData().setInteger("TIER", tier);
            nbtBlock.getData().setItemStack("DROP", new ItemStack(Objects.requireNonNull(Material.matchMaterial(genDrop))));
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText("§aGenerator upgraded!"));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER,1, 1);
            return;
        }
    }


    //MAKE INTO A UTIL THING LATER
    public ItemStack GiveGenerator(Block block) {
        NBTBlock nbtBlock = new NBTBlock(block);
        String genName = nbtBlock.getData().getString("NAME");
        ItemStack generator = ItemBuilder.createItem(new ItemStack(block.getType()),
                ColorUtils.translateAlternateColorCodes('&', genName), new String[]{"§e§lTIER: §f" + nbtBlock.getData().getInteger("TIER"), "§e§lPLACE THIS in §f(/plot)"});
        NBTItem nbtItem = new NBTItem(generator);
        Integer upcost = nbtBlock.getData().getInteger("UPGRADECOST");
        nbtItem.setInteger("UPGRADECOST", upcost);
        nbtItem.setString("NAME", genName);
        nbtItem.setBoolean("GENERATOR", true);
        nbtItem.setInteger("TIER", nbtBlock.getData().getInteger("TIER"));
        nbtItem.setItemStack("DROP", nbtBlock.getData().getItemStack("DROP"));
        nbtItem.applyNBT(generator);
        return generator;
    }

}
