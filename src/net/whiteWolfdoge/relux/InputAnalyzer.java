package net.whiteWolfdoge.relux;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;

import net.whiteWolfdoge.relux.util.Relighter;

/**
 * This class handles all direct input from the user.
 */
class InputAnalyzer implements TabExecutor{
	
	/**
	 * Constructs a new InputAnalyzer
	 */
	InputAnalyzer(){
		// Do nothing
	}
	
	/**
	 * Returns a list of possible completions for a command argument.<br />
	 * <b>NOTE:</b> For internal use only!
	 * @return			A list of possible completions for the command argument
	 * @param sender	The issuer of the command
	 * @param cmd		The command issued
	 * @param alias		The alias of the command issued
	 * @param args		The arguments of the command issued
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args){
		// TODO
		return null;
	}
	
	/**
	 * Executes an issued command.<br />
	 * <b>NOTE:</b> For internal use only!
	 * @return			True if the command was executed successfully
	 * @param sender	The issuer of the command
	 * @param cmd		The command issued
	 * @param alias		The alias of the command issued
	 * @param args		The arguments of the command issued
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args){
		if(args.length == 0){ // If there's no argument, tell info
			sender.sendMessage(ReluxPlugin.MSG_INFO);
			if(sender.hasPermission(ReluxPlugin.PERMISSION_USE)) // If the sender has permission
				sender.sendMessage(ReluxPlugin.MSG_USAGE);
			else // Else the sender is denied
				sender.sendMessage(ReluxPlugin.MSG_EX_PERMISSION_DENIED);
			return true;
		}
		else if(!sender.hasPermission(ReluxPlugin.PERMISSION_USE)){ // Else if permission is denied
			sender.sendMessage(ReluxPlugin.MSG_EX_PERMISSION_DENIED);
			return true;
		}
		else if(args.length != 1){ // Else if there's an invalid quantity of arguments
			sender.sendMessage(new String[]{ // @formatter:off
				ReluxPlugin.MSG_EX_ARGS_INVALID_QTY,
				ReluxPlugin.MSG_USAGE}); // @formatter:of
			return true;
		}
		else if(!radiusIsValid(args[0])){ // Else if the radius is invalid
			sender.sendMessage(ReluxPlugin.MSG_EX_ARGS_INVALID_RADIUS);
			return true;
		}
		else if(!(sender instanceof Entity) && !(sender instanceof Block)){ // Else if the location cannot be determined
			sender.sendMessage(ReluxPlugin.MSG_EX_INVALID_SOURCE);
			return true;
		}
		else{ // Else it can be processed
			Chunk chk;
			if(sender instanceof Entity){ // If sender is an entity
				chk = ((Entity)sender).getLocation().getChunk();
				// TODO LOGGING Entity $ENTITY_NAME($ENTITY_X, $ENTITY_Y, $ENTITY_Z, $ENTITY_WORLD) issued relight of chunks within $(RADIUS - 1) of Chunk($CENTER_CHUNK_X, $CENTER_CHUNK_Z, $CHUNK_WORLD)
				Relighter.relightChunkRadius(chk, Byte.parseByte(args[0]));
				return true;
			}
			else if(sender instanceof Block){ // If sender is a block
				chk = ((Block)sender).getLocation().getChunk();
				// TODO LOGGING Block $BLOCK_NAME($BLOCK_X, $BLOCK_Y, $BLOCK_Z, $BLOCK_WORLD) issued relight of chunks within $(RADIUS - 1) of Chunk($CENTER_CHUNK_X, $CENTER_CHUNK_Z, $CHUNK_WORLD)
				Relighter.relightChunkRadius(chk, Byte.parseByte(args[0]));
				return true;
			}
			else{ // This won't ever happen!
				sender.getServer().getLogger().warning("Something terrible has happened, get the pitchforks!");
				return false;
			}
		}
	}
	
	/**
	 * The following method verifies that a radius is valid
	 */
	private static boolean radiusIsValid(String rawRadius){
		try{
			int rad = Integer.parseInt(rawRadius);
			if(rad >= ReluxPlugin.MIN_RADIUS && rad <= ReluxPlugin.MAX_RADIUS){ // If the parsed radius is within range
				return true;
			}
			else // Else the parsed radius is outside range
				return false;
		}
		catch(NumberFormatException nfex){ // Catch an invalid number
			return false;
		}
	}
}
