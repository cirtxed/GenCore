package liltrip.gencore;

import de.tr7zw.nbtinjector.NBTInjector;
import liltrip.gencore.commands.CommandManager;
import liltrip.gencore.commands.GenListCmd;
import liltrip.gencore.config.CoreConfig;
import liltrip.gencore.config.GenConfig;
import liltrip.gencore.data.PlayerEvents;
import liltrip.gencore.data.PlayerManager;
import liltrip.gencore.generators.GeneratorListener;
import liltrip.gencore.generators.GeneratorProtection;
import liltrip.gencore.generators.GeneratorRunnable;
import liltrip.gencore.utils.menusystem.MenuListener;
import liltrip.gencore.utils.menusystem.PlayerMenuUtility;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class GenCore extends JavaPlugin {

    @Getter private static GenCore instance;
    @Getter private static PlayerManager playerManager;
    @Getter private static final Random random = new Random();
    @Getter private static Economy econ = null;

    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        playerManager = new PlayerManager();
        GeneratorRunnable generatorRunnable = new GeneratorRunnable(this);
        NBTInjector.inject();

        if(!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage("[GenCore] VAULT IS REQUIRED TO RUN!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        CoreConfig.getLang();
        GenConfig.getGenerators();

        this.getDataFolder().mkdirs();
        Objects.requireNonNull(getCommand("gen")).setExecutor(new CommandManager());
        Objects.requireNonNull(getCommand("genlist")).setExecutor(new GenListCmd());


        getServer().getPluginManager().registerEvents(new GeneratorListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new GeneratorProtection(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for(Player player : Bukkit.getOnlinePlayers()) {
            playerManager.savePlayer(player.getUniqueId());
            player.kickPlayer("Reboot");
        }
    }

    public boolean setupEconomy() {
        if(getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    //Provide a player and return a menu system for that player
    //create one if they don't already have one
    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p))) { //See if the player has a playermenuutility "saved" for them

            //This player doesn't. Make one for them add add it to the hashmap
            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p); //Return the object by using the provided player
        }
    }


}
