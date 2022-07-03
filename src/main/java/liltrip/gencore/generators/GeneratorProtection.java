package liltrip.gencore.generators;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class GeneratorProtection implements Listener {

    @EventHandler
    public void PistonPush(BlockPistonExtendEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void PistonPull(BlockPistonRetractEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void dragonEgg(BlockFromToEvent event) {
        if(event.getBlock().getType() == Material.DRAGON_EGG)
            event.setCancelled(true);
    }

    @EventHandler
    public void DragonFall(BlockPhysicsEvent event) {
        if(event.getSourceBlock().getType() == Material.DRAGON_EGG)
            event.setCancelled(true);
        if(event.getBlock().getType() == Material.DRAGON_EGG)
            event.setCancelled(true);
    }

    @EventHandler
    public void flalingblock(EntityChangeBlockEvent event) {
        event.setCancelled(true);
    }

}
