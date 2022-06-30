package liltrip.gencore.commands.gen;

import de.tr7zw.nbtapi.NBTItem;
import liltrip.gencore.GenCore;
import liltrip.gencore.commands.SubCommand;
import liltrip.gencore.config.GenConfig;
import liltrip.gencore.utils.chat.ColorUtils;
import liltrip.gencore.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Objects;

public class GenGiveGenerators extends SubCommand {
    @Override
    public String getName() {
        return "give";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getSyntax() {
        return "/gen give";
    }

    @Override
    public String getDescription() {
        return "gives player generators";
    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public boolean requireAdmin() {
        return true;
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length >= 1) {
            Player argPlayer = Bukkit.getPlayer(args[1]);
            if(argPlayer == null) {
                player.sendMessage(getSyntax());
                return;
            }
            //Bukkit.broadcastMessage(Objects.requireNonNull(GenConfig.getGenerators().getDefaults()).toString());
            for(String key : Objects.requireNonNull(GenConfig.getGenerators().getConfigurationSection("generators.")).getKeys(false)) {

                String genType = String.valueOf(GenConfig.getGenerators().get("generators." + key + ".generator.type"));
                Integer tier = (Integer) Objects.requireNonNull(GenConfig.getGenerators().get("generators." + key + ".generator.tier"));
                String genDrop = String.valueOf(GenConfig.getGenerators().get("generators." + key + ".drops.type"));
                String genName = String.valueOf(GenConfig.getGenerators().get("generators." + key + ".generator.name"));
                ItemStack generator = ItemBuilder.createItem(new ItemStack(Objects.requireNonNull(Material.matchMaterial(genType))),
                        ColorUtils.translateAlternateColorCodes('&', genName), new String[]{"§e§lTIER: §f" + tier, "§e§lPLACE THIS in §f(/plot)"});
                Integer upcost = (Integer) GenConfig.getGenerators().get("generators." + key + ".generator.upgradecost");
                NBTItem genNBT = new NBTItem(generator);
                genNBT.setString("NAME", genName);
                genNBT.setBoolean("GENERATOR", true);
                genNBT.setInteger("TIER", tier);
                genNBT.setItemStack("DROP", new ItemStack(Objects.requireNonNull(Material.matchMaterial(genDrop))));
                genNBT.setInteger("UPGRADECOST", upcost);
                genNBT.applyNBT(generator);
                argPlayer.getInventory().addItem(generator);

            }
        }

    }
}
