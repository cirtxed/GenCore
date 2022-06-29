package liltrip.gencore;

import de.tr7zw.nbtinjector.NBTInjector;
import liltrip.gencore.commands.CommandManager;
import liltrip.gencore.data.PlayerEvents;
import liltrip.gencore.data.PlayerManager;
import liltrip.gencore.generators.GeneratorListener;
import liltrip.gencore.generators.GeneratorRunnable;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.Random;

public final class GenCore extends JavaPlugin {

    @Getter
    private static GenCore instance;

    @Getter
    private static PlayerManager playerManager;

    @Getter
    private static final Random random = new Random();

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        playerManager = new PlayerManager();
        GeneratorRunnable generatorRunnable = new GeneratorRunnable(this);
        NBTInjector.inject();

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
    }
}
