package liltrip.gencore.commands;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public abstract class SubCommand {

    public abstract String getName();
    public abstract ArrayList<String> getAliases();
    public abstract String getSyntax();
    public abstract String getDescription();
    public abstract int getMaxArgs();
    public abstract int getMinArgs();
    public abstract boolean requireAdmin();
    public abstract void perform(Player player, String args[]);
}

