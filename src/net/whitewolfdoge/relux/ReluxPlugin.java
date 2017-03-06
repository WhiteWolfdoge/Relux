package net.whiteWolfdoge.relux;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class ReluxPlugin extends JavaPlugin{
	// @formatter:off
	private TabExecutor tex;
	
	public static final int
		MIN_RADIUS =	1,
		MAX_RADIUS = 	15;
	public static final String
		CMD_MAIN =						"relux",
		PERMISSION_USE =				"relux.use",
		MSG_PREFIX =					ChatColor.LIGHT_PURPLE + "[Relux] " + ChatColor.YELLOW,
		MSG_INFO =						MSG_PREFIX + "Relux allows manual chunk relighting. Written by WhiteWolfdoge (Emily White)",
		MSG_USAGE =						MSG_PREFIX + "Usage: " + ChatColor.ITALIC + "/relux <radius> [<xPos> <zPos>]",
		
		MSG_EX_PREFIX =					MSG_PREFIX + ChatColor.RED,
		MSG_EX_PERMISSION_DENIED =		MSG_EX_PREFIX + "You were denied permission to use this plugin.",
		MSG_EX_ARGS_INVALID_QTY =		MSG_EX_PREFIX + "Invalid quantity of arguments!",
		MSG_EX_ARGS_INVALID_RADIUS =	MSG_EX_PREFIX + "The radius must be an integer in the range of " + MIN_RADIUS + " through " + MAX_RADIUS,
		MSG_EX_INVALID_SOURCE =			MSG_EX_PREFIX + "You must use this command from within a world.";
	// @formatter:on
	
	/**
	 * This method is called by the PluginManager after the Plugin is constructed.
	 * @see org.bukkit.plugin.java.JavaPlugin#onLoad()
	 */
	@Override
	public void onLoad(){
		tex = new InputAnalyzer();
	}
	
	/**
	 * This method is called by the PluginManager when the plugin is to be prepared for usage.
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
	@Override
	public void onEnable(){
		getCommand(CMD_MAIN).setExecutor(tex);
		getCommand(CMD_MAIN).setTabCompleter(tex);
		
		// Ensure that natives are available before continuing.	
		String nativesFound = Native.checkNatives();
		if(nativesFound == null){
			getServer().getLogger().warning(ChatColor.stripColor(ReluxPlugin.MSG_EX_PREFIX + "Natives were not found, we can't live like this!."));
			getPluginLoader().disablePlugin(this);
		}
		else{
			getServer().getLogger().info(ChatColor.stripColor(ReluxPlugin.MSG_EX_PREFIX + "Found natives: [" + nativesFound + ']'));
		}
	}
	
	/**
	 * This method is called by the Plugin or PluginManager when the plugin is no longer to be used.
	 * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
	 */
	@Override
	public void onDisable(){
		// Do nothing
	}
	
	/**
	 * This is an API method that allows software to relight a radius of chunks using Relux's code
	 * @param centerChunk	The chunk in the center of the area
	 * @param radius		The range
	 * @return			<b>true</b> if the task is successful, <b>false</b> otherwise
	 */
	public boolean relightChunkRadius(Chunk centerChunk, byte radius){
		if(this.isEnabled()){
			return Relighter.relightChunkRadius(centerChunk, radius);
		}
		else return false;
		
	}
	
	/**
	 * This is an API method that allows software to relight a chunk using Relux's code
	 * @param chunk		The chunk that is to be relighted
	 * @return			<b>true</b> if the task is successful, <b>false</b> otherwise
	 */
	public boolean relightChunk(Chunk chunk){
		if(this.isEnabled()){
			return Relighter.relightChunk(chunk);
		}
		else return false;
	}
	
	/**
	 * This is an API method that allows software to relight an individual block using Relux's code<br />
	 * <b>Note: </b>Adjacent blocks may not update when using this method, use only if you know exactly what you are doing!
	 * @param block		The block that is to be relighted
	 * @return			<b>true</b> if the task is successful, <b>false</b> otherwise
	 */
	public boolean relightBlock(Block block){
		if(this.isEnabled()){
			return Relighter.relightBlock(block);
		}
		else return false;
	}
}
