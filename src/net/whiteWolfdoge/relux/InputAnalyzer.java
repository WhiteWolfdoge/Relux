package net.whiteWolfdoge.relux;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

/**
 * This class handles all direct input from the user.
 */
class InputAnalyzer implements TabExecutor{
	private final ReluxPlugin rp;
	
	/**
	 * Constructs a new InputAnalyzer
	 */
	protected InputAnalyzer(ReluxPlugin newRP){
		rp = newRP;
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
		else if(args[0].equals("w")){ // For WorldEdit selections
			if(!(sender instanceof Player)){
				sender.sendMessage(ReluxPlugin.MSG_EX_INVALID_SOURCE);
				return true;
			}
			else{
				Player pl = (Player)sender;
				Selection sel = WorldEditPlugin.getPlugin(WorldEditPlugin.class).getSelection(pl);
				if(sel == null){ // If the player has nothing selected
					sender.sendMessage(ReluxPlugin.MSG_EX_SELECTION_REQUIRED);
					return true;
				}
				else{ // Valid selection
					sender.sendMessage(ReluxPlugin.MSG_RELIGHTING_STARTED_SEL);
					
					
					String enName = pl.getName();
					Location plLoc = pl.getLocation();
					String source = String.format("%s(%d, %d, %d, %s)", enName, plLoc.getBlockX(), plLoc.getBlockY(), plLoc.getBlockZ(), pl.getWorld().getName());
					

					Location min = sel.getMinimumPoint();
					Location max = sel.getMaximumPoint();
					
					int xMin, xMax;
					if(min.getBlockX() <= max.getBlockX()){
						xMin = min.getBlockX();
						xMax = max.getBlockX();
					}
					else{
						xMin = max.getBlockX();
						xMax = min.getBlockX();
					}
					
					int zMin, zMax;
					if(min.getBlockZ() <= max.getBlockZ()){
						zMin = min.getBlockZ();
						zMax = max.getBlockZ();
					}
					else{
						zMin = max.getBlockZ();
						zMax = min.getBlockZ();
					}
					
					Block nwBlock = sel.getWorld().getBlockAt(xMin, 127, zMin);
					Block seBlock = sel.getWorld().getBlockAt(xMax, 127, zMax);
					Chunk nwChunk = nwBlock.getChunk();
					Chunk seChunk = seBlock.getChunk();
					String op = String.format("WorldEdit selection starting at Chunk(%d, %d, %s) and ending at Chunk(%d, %d, %s)", nwChunk.getX(), nwChunk.getZ(), nwChunk.getWorld().getName(), seChunk.getX(), seChunk.getZ(), seChunk.getWorld().getName());
					
					
					String logMsg = ChatColor.stripColor(ReluxPlugin.MSG_PREFIX + "Player " + source + " issued relight of " + op);
					Bukkit.getLogger().info(logMsg);
					
					
					rp.rl.relightWESelection(sel);
					sender.sendMessage(ReluxPlugin.MSG_RELIGHTING_FINISHED);
					return true;
				}
			}
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
				sender.sendMessage(String.format(ReluxPlugin.MSG_RELIGHTING_STARTED, Byte.parseByte(args[0]), Byte.parseByte(args[0]) == 1 ? "chunk" : "chunks"));
				Entity en =	(Entity)sender;
				Chunk chk = en.getLocation().getChunk();
				
				
				String enName = en.getClass().getSimpleName();
				if(en instanceof Player) enName = ((Player)en).getName();
				Location enLoc = en.getLocation();
				String source = String.format("%s(%d, %d, %d, %s)", enName, enLoc.getBlockX(), enLoc.getBlockY(), enLoc.getBlockZ(), en.getWorld().getName());
				
				int rad = Byte.parseByte(args[0]);
				Chunk cen = en.getLocation().getChunk();
				String op = String.format("chunks within %d of Chunk(%d, %d, %s)", rad, cen.getX(), cen.getZ(), cen.getWorld().getName());
				
				String logMsg = ChatColor.stripColor(ReluxPlugin.MSG_PREFIX + "Entity " + source + " issued relight of " + op);
				Bukkit.getLogger().info(logMsg);
				
				
				rp.rl.relightChunkRadius(chk, Byte.parseByte(args[0]));
				sender.sendMessage(ReluxPlugin.MSG_RELIGHTING_FINISHED);
				return true;
			}
			else if(sender instanceof BlockCommandSender){ // If sender is a block
				Block blk = ((BlockCommandSender)sender).getBlock();
				
				
				String blkName = blk.getType().name();
				String source = String.format("%s(%d, %d, %d, %s)", blkName, blk.getX(), blk.getY(), blk.getZ(), blk.getWorld().getName());
				
				byte rad = Byte.parseByte(args[0]);
				Chunk cen = blk.getChunk();
				String op = String.format("chunks within %d of Chunk(%d, %d, %s)", rad, cen.getX(), cen.getZ(), cen.getWorld().getName());
				
				String logMsg = ChatColor.stripColor(ReluxPlugin.MSG_PREFIX + "Block " + source + " issued relight of " + op);
				Bukkit.getLogger().info(logMsg);
				
				
				rp.rl.relightChunkRadius(cen, rad);
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
