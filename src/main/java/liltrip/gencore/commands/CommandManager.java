package liltrip.gencore.commands;

import liltrip.gencore.commands.gen.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CommandManager implements CommandExecutor, TabExecutor {

    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public CommandManager() {
        subCommands.add(new GenHelp());
        subCommands.add(new GenSetGenSlots());
        subCommands.add(new GenRemGenSlots());
        subCommands.add(new GenAddGenSlots());
        subCommands.add(new GenGiveGenerators());
        subCommands.add(new GenReloadConfig());
        subCommands.add(new GenList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (args.length != 0) {
            for (SubCommand subCommand : getSubCommands()) {
                if (!(args[0].equalsIgnoreCase(subCommand.getName()) || subCommand.getAliases().contains(args[0]))) continue;
                if (subCommand.requireAdmin() && !player.isOp()) {
                    player.sendMessage("&cThis command requires administrator permissions!");
                    return true;
                }
                if (!(args.length-1 <= subCommand.getMaxArgs() && args.length-1 >= subCommand.getMinArgs())) {
                    player.sendMessage("&cIncorrect Usage: " + subCommand.getSyntax());
                    return true;
                }

                subCommand.perform(player, args);
                return true;
            }
        }

        //player.sendMessage(AdminHelp.getFormattedAHelpMessage(player));
        //Bukkit.broadcastMessage(LangConfig.getLang().toString() + "");

        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1, 1);
        return true;
    }

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender  Source of the command.  For players tab-completing a
     *                command inside a command block, this will be the player, not
     *                the command block.
     * @param command Command which was executed
     * @param alias   The alias used
     * @param args    The arguments passed to the command, including final
     *                partial argument to be completed and command label
     * @return A List of possible completions for the final argument, or null
     * to default to the command executor
     */
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) return null;
        Player player = (Player) sender;
        if(!player.isOp()) return null;

        if(args.length == 1) {
            List<String> arguments = new ArrayList<>();
            for(int i = 0; i <getSubCommands().size(); i++) {
                arguments.add(getSubCommands().get(i).getName());
            }
            return arguments;
        }
        return null;
    }
}
