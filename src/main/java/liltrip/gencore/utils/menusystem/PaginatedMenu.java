package liltrip.gencore.utils.menusystem;

import liltrip.gencore.utils.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class PaginatedMenu extends Menu{

    //- Keep track of what page the menu is on
    protected int page = 0;
    //- 28 is max items because with the border set below,
    //- 28 empty slots are remaining.
    protected int maxItemsPerPage = 28;
    //- the index represents the index of the slot
    //- that the loop is on
    protected int index = 0;

    public PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    public void addMenuBorder() {
        inventory.setItem(48, ItemBuilder.createItem(new ItemStack(Material.RED_DYE), ChatColor.RED + "Left"));
        inventory.setItem(49, ItemBuilder.createItem(new ItemStack(Material.BARRIER), ChatColor.DARK_RED + "Close"));
        inventory.setItem(50, ItemBuilder.createItem(new ItemStack(Material.LIME_DYE), ChatColor.GREEN + "Right"));

        inventory.setItem(17, super.FILLER_GLASS);
        inventory.setItem(18, super.FILLER_GLASS);
        inventory.setItem(26, super.FILLER_GLASS);
        inventory.setItem(27, super.FILLER_GLASS);
        inventory.setItem(35, super.FILLER_GLASS);
        inventory.setItem(36, super.FILLER_GLASS);
        for(int i = 44; i<54; i++) {
            if(inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }
        for(int i = 0; i<10; i++) {
            if(inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }
}
