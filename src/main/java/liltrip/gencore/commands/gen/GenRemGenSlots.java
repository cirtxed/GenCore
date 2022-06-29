package liltrip.gencore.commands.gen;

import liltrip.gencore.GenCore;
import liltrip.gencore.commands.SubCommand;
import liltrip.gencore.data.GenPlayer;
import liltrip.gencore.data.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GenRemGenSlots extends SubCommand {
    PlayerManager manager = GenCore.getPlayerManager();

    @Override
    public String getName() {
        return "remslots";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getSyntax() {
        return "/gen remslots <player> <int>";
    }

    @Override
    public String getDescription() {
        return "help with the /gen remslots";
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public boolean requireAdmin() {
        return true;
    }

    @Override
    public void perform(Player player, String[] args) {

        Player argPlayer = Bukkit.getPlayer(args[1]);
        int newSlots;

        if(argPlayer == null) {
            player.sendMessage(getSyntax());
            return;
        }

        try {
            newSlots = Integer.parseInt(args[2]);
        } catch (NumberFormatException numberFormatException) {
            player.sendMessage("Invalid Number");
            return;
        }

        GenPlayer genPlayer = manager.getUser(argPlayer.getUniqueId());
        genPlayer.removeGenSlots(newSlots);
        player.sendMessage(argPlayer.getDisplayName() + " new slots set to " + genPlayer.getGenSlots());
        argPlayer.sendMessage(player.getDisplayName() + " has set your gen slots to " + genPlayer.getGenSlots());
    }
}
