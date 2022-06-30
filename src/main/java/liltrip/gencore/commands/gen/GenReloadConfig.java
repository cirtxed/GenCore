package liltrip.gencore.commands.gen;

import liltrip.gencore.commands.SubCommand;
import liltrip.gencore.config.CoreConfig;
import liltrip.gencore.config.GenConfig;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GenReloadConfig extends SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getSyntax() {
        return "/gen reload";
    }

    @Override
    public String getDescription() {
        return "help with the /gen commands";
    }

    @Override
    public int getMaxArgs() {
        return Integer.MAX_VALUE;
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
        File genFile = new File(GenConfig.getGenerators().getCurrentPath());
        File coreFile = new File(CoreConfig.getLang().getCurrentPath());
        try {
            GenConfig.getGenerators().load(genFile);
            CoreConfig.getLang().load(coreFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }
}
