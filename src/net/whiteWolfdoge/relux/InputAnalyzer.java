package net.whiteWolfdoge.relux;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * This class handles all direct input from the user.
 */
class InputAnalyzer implements TabExecutor{
	
	/**
	 * Constructs a new InputAnalyzer
	 */
	protected InputAnalyzer(){
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
		// @formatter:off
		sender.sendMessage(new String[]{
			ReluxPlugin.MSG_INFO,
			ReluxPlugin.MSG_USAGE}
		);
		// @formatter:on
		
		LinkedList<String> sug = new LinkedList<String>();
		for(int forA = ReluxPlugin.MIN_RADIUS; forA <= ReluxPlugin.MAX_RADIUS; forA++){
			sug.add(Integer.toString(forA));
		}
		
		return sug;
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
				ReluxPlugin.MSG_USAGE}); // @formatter:on
			return true;
		}
		else if(!radiusIsValid(args[0])){ // Else if the radius is invalid
			sender.sendMessage(ReluxPlugin.MSG_EX_ARGS_INVALID_RADIUS);
			return true;
		}
		else if(!(sender instanceof Entity) && !(sender instanceof BlockCommandSender)){ // Else if the location cannot be determined
			sender.sendMessage(ReluxPlugin.MSG_EX_INVALID_SOURCE);
			return true;
		}
		else{ // Else it can be processed
			if(sender instanceof Entity){ // If sender is an entity
				Entity en =	(Entity)sender;
				Chunk chk = en.getLocation().getChunk();
				
				
				String enName = en.getClass().getSimpleName();
				if(en instanceof Player) enName = ((Player)en).getName();
				int enX = en.getLocation().getBlockX(), enY = en.getLocation().getBlockY(), enZ = en.getLocation().getBlockZ();
				String enWld = en.getWorld().getName();
				String source = String.format("%s(%d, %d, %d, %s)", enName, enX, enY, enZ, enWld);
				
				int rad = Byte.parseByte(args[0]);
				Chunk cen = en.getLocation().getChunk();
				int chX = cen.getX(), chZ = cen.getZ();
				String chWld = cen.getWorld().getName();
				String op = String.format("chunks within %d of Chunk(%d, %d, %s)", rad, chX, chZ, chWld);
				
				String logMsg = ChatColor.stripColor(ReluxPlugin.MSG_PREFIX + "Entity " + source + " issued relight of " + op);
				Bukkit.getLogger().info(logMsg);
				
				
				Relighter.relightChunkRadius(chk, Byte.parseByte(args[0]));
				return true;
			}
			else if(sender instanceof BlockCommandSender){ // If sender is a block
				Block blk = ((BlockCommandSender)sender).getBlock();
				
				
				// TODO LOGGING Block $BLOCK_NAME($BLOCK_X, $BLOCK_Y, $BLOCK_Z, $BLOCK_WORLD) issued relight of chunks within $(RADIUS - 1) of Chunk($CENTER_CHUNK_X, $CENTER_CHUNK_Z, $CHUNK_WORLD)
				String blkName = blk.getClass().getSimpleName();
				int blkX = blk.getX(), blkY = blk.getY(), blkZ = blk.getZ();
				String blkWld = blk.getWorld().getName();
				String source = String.format("%s(%d, %d, %d, %s)", blkName, blkX, blkY, blkZ, blkWld);
				
				int rad = Byte.parseByte(args[0]);
				Chunk cen = blk.getChunk();
				int chX = cen.getX(), chZ = cen.getZ();
				String chWld = cen.getWorld().getName();
				String op = String.format("chunks within %d of Chunk(%d, %d, %s)", rad, chX, chZ, chWld);
				
				String logMsg = ChatColor.stripColor(ReluxPlugin.MSG_PREFIX + "Block " + source + " issued relight of " + op);
				Bukkit.getLogger().info(logMsg);
				
				Relighter.relightChunkRadius(cen, Byte.parseByte(args[0]));
				return true;
			}
			else{ // This shouldn't ever happen.
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
