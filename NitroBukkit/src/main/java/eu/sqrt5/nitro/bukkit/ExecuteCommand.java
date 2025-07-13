package eu.sqrt5.nitro.bukkit;

import eu.sqrt5.nitro.core.SyntaxError;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ExecuteCommand implements CommandExecutor {
    NitroBukkit nitro;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            nitro.nitro.run(String.join(" ", args));
        } catch (SyntaxError error) {
            sender.sendMessage("&c" + error.getMessage());
        }

        return true;
    }
}
