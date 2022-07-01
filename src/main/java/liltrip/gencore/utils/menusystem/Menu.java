package liltrip.gencore.utils.menusystem;

import liltrip.gencore.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class Menu implements InventoryHolder {

    //- Protects values that can be accessed in the menus
    protected PlayerMenuUtility playerMenuUtility;
    protected Inventory inventory;
    protected ItemStack FILLER_GLASS = ItemBuilder.createItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ");

    //- Constructor for menu
    public Menu(PlayerMenuUtility playerMenuUtility) {
        this.playerMenuUtility = playerMenuUtility;
    }

    //- Decide their name
    public abstract String getMenuName();

    //- Decide their slot amount
    public abstract int getSlots();

    //- Decide how the items will be handled
    public abstract void handleMenu(InventoryClickEvent event);

    //- Decide what items will be placed in menu
    public abstract void setMenuItems();

    //- When called, inventory is created and opened for player
    public void open() {
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());
        this.setMenuItems();;
        playerMenuUtility.getOwner().openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    //Helpful utility method to fill all remaining slots with "filler glass"
    public void setFillerGlass(){
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null){
                inventory.setItem(i, FILLER_GLASS);
            }
        }
    }


}
