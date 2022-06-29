package liltrip.gencore.commands.gen;

import liltrip.gencore.commands.SubCommand;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GenHelp extends SubCommand {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getSyntax() {
        return "/gen help";
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
        player.sendMessage(getFormattedAHelpMessage(player));

    }

    public static String[] getFormattedAHelpMessage(OfflinePlayer player) {
        return new String[]{
                "§6/gen help: §eDisplays the /admin commands",
                "§6/gen setbooster <float>: §eSets the global XP Booster",
                "§6/gen setlevel <player> <int>: §eSet players Level",
                "§6/gen setxp <player> <long>: §eSet players XP",
                "§6/gen setmoney <player> <int>: §eSet players Balance",
                "§6/gen setblocks <player> <int>: §eSet players Blocks Mined",
        };
    }
}
