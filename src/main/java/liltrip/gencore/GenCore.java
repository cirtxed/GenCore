package liltrip.gencore;

import de.tr7zw.nbtinjector.NBTInjector;
import liltrip.gencore.commands.CommandManager;
import liltrip.gencore.config.CoreConfig;
import liltrip.gencore.config.GenConfig;
import liltrip.gencore.data.PlayerEvents;
import liltrip.gencore.data.PlayerManager;
import liltrip.gencore.generators.GeneratorListener;
import liltrip.gencore.generators.GeneratorRunnable;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public final class GenCore extends JavaPlugin {

    @Getter private static GenCore instance;
    @Getter private static PlayerManager playerManager;
    @Getter private static final Random random = new Random();
    @Getter private static Economy econ = null;


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

        getServer().getPluginManager().registerEvents(new GeneratorListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

        //getServer().getPluginManager().registerEvents(new XpEarnEvents(), this);
        //Objects.requireNonNull(getCommand("Stats")).setExecutor(new StatsCmd());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        File genFile = new File(GenConfig.getGenerators().getCurrentPath());
        File coreFile = new File(CoreConfig.getLang().getCurrentPath());
        try {
            GenConfig.getGenerators().save(genFile);
            CoreConfig.getLang().save(coreFile);
        } catch (IOException e) {
            e.printStackTrace();
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
}
