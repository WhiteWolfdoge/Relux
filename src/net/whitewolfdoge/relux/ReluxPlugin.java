package net.whitewolfdoge.relux;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import util.ChunkRelighter;

public class ReluxPlugin extends JavaPlugin{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("relux")){ // If the command was "relux" or one of its aliases
			if(!sender.hasPermission("relux.use")){ // If the command sender does not have permission
				sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
				return true;
			}
			else if(args.length < 1){ // If there was no argument
				return false;
			}
			else if(args.length > 1){ // If there was more than one argument
				return false;
			}
			else{
				return processRequest(sender, args[0]);
			}
		}
		else{ // Command not recognized by this plugin
			return false;
		}
	}
	
	private boolean processRequest(CommandSender sender, String radRaw){
		if(sender.getName().equalsIgnoreCase("CONSOLE")){ // If the command is issued from the console
			sender.sendMessage(ChatColor.RED + "This command cannot be issued from the console.");
		}
		else{
			try{
				byte rad = Byte.parseByte(radRaw); // TODO Use this!
				
				Block loc = ((Player)sender).getLocation().getBlock();
				
				sender.sendMessage("Relighting...");
				
				ChunkRelighter.relightChunk(loc.getChunk());
				// TODO Re-light all chunks within radius
			}
			catch(NumberFormatException nfe){ // The radius wans't a valid number
				return false;
			}
		}
		
		return true;
	}
}
