package liltrip.gencore.commands.gen;

import liltrip.gencore.GenCore;
import liltrip.gencore.commands.SubCommand;
import liltrip.gencore.utils.menusystem.menus.GenListMenu;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GenList extends SubCommand {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getSyntax() {
        return "/gen list";
    }

    @Override
    public String getDescription() {
        return "Shows all of the generators in gui";
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
        return false;
    }

    @Override
    public void perform(Player player, String[] args) {
        new GenListMenu((GenCore.getPlayerMenuUtility(player))).open();
    }

}
