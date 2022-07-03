package liltrip.gencore.utils.menusystem.menus;

import liltrip.gencore.config.GenConfig;
import liltrip.gencore.utils.chat.ColorUtils;
import liltrip.gencore.utils.item.ItemBuilder;
import liltrip.gencore.utils.menusystem.PaginatedMenu;
import liltrip.gencore.utils.menusystem.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class GenListMenu extends PaginatedMenu {

    public GenListMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Generator Lists";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);
        if(e.getCurrentItem().getType() == Material.BARRIER) {
            Player player = (Player) e.getWhoClicked();
            player.closeInventory();
        }
    }

    @Override
    public void setMenuItems() {
        addMenuBorder();

        for(String key : Objects.requireNonNull(GenConfig.getGenerators().getConfigurationSection("generators.")).getKeys(false)) {
            String genType = String.valueOf(GenConfig.getGenerators().get("generators." + key + ".generator.type"));
            Integer tier = (Integer) Objects.requireNonNull(GenConfig.getGenerators().get("generators." + key + ".generator.tier"));
            String genName = String.valueOf(GenConfig.getGenerators().get("generators." + key + ".generator.name"));
            ItemStack generator = ItemBuilder.createItem(new ItemStack(Objects.requireNonNull(Material.matchMaterial(genType))),
                    ColorUtils.translateAlternateColorCodes('&', genName), new String[]{"§e§lTIER: §f" + tier, "§e§lPLACE THIS in §f(/plot)"});

            inventory.addItem(generator);
        }
        inventory.setItem(48, super.FILLER_GLASS);
        inventory.setItem(50, super.FILLER_GLASS);
        setFillerGlass();

    }

}
