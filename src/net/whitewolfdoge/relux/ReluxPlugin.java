package net.whiteWolfdoge.relux;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import net.whiteWolfdoge.relux.natives.NativesProvider;

public class ReluxPlugin extends JavaPlugin{
	// @formatter:off
	private InputAnalyzer inAn = null;
	protected WorldEditPlugin wep = null;
	protected NativesProvider npv = null;
	protected Relighter rl = null;
	
	public static final int
		MIN_RADIUS =	1,
		MAX_RADIUS = 	15;
	public static final String
		CMD_MAIN =						"relux",
		PERMISSION_USE =				"relux.use",
		MSG_PREFIX =					ChatColor.LIGHT_PURPLE + "[Relux] " + ChatColor.GRAY,
		MSG_INFO =						MSG_PREFIX + "Relux is a plugin and API that allows relighting of the world. Author: " + ChatColor.ITALIC + "WhiteWolfdoge (Emily J .White)",
		MSG_USAGE =						MSG_PREFIX + "Usage: " + ChatColor.ITALIC + "/relux <radius | w>",
		MSG_RELIGHTING_STARTED = 		MSG_PREFIX + "Relighting chunks within %d %s of you...",
		MSG_RELIGHTING_STARTED_SEL = 	MSG_PREFIX + "Relighting selection...",
		MSG_RELIGHTING_FINISHED =		MSG_PREFIX + "Relighting finished!",
		
		MSG_EX_PREFIX =					MSG_PREFIX + ChatColor.RED,
		MSG_EX_PERMISSION_DENIED =		MSG_EX_PREFIX + "You were denied permission to use this plugin.",
		MSG_EX_ARGS_INVALID_QTY =		MSG_EX_PREFIX + "Invalid quantity of arguments!",
		MSG_EX_ARGS_INVALID_RADIUS =	MSG_EX_PREFIX + "The radius must be an integer in the range of " + MIN_RADIUS + " through " + MAX_RADIUS,
		MSG_EX_INVALID_SOURCE =			MSG_EX_PREFIX + "You must use this command from within a world.",
		MSG_EX_SELECTION_REQUIRED =		MSG_EX_PREFIX + "You must make a WorldEdit selection first!";
	// @formatter:on
	
	/**
	 * This method is called by the PluginManager after the Plugin is constructed.
	 * @see org.bukkit.plugin.java.JavaPlugin#onLoad()
	 */
	@Override
	public void onLoad(){
		inAn = new InputAnalyzer(this);
		
		Plugin[] pls = getServer().getPluginManager().getPlugins();
		for(Plugin p : pls){
			if(p instanceof WorldEditPlugin){
				wep = (WorldEditPlugin)p;
				break;
			}
		}
	}
	
	/**
	 * This method is called by the PluginManager when the plugin is to be prepared for usage.
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
	@Override
	public void onEnable(){
		getCommand(CMD_MAIN).setExecutor(inAn);
		getCommand(CMD_MAIN).setTabCompleter(inAn);
		
		
		// Ensure that natives are available before continuing.
		for(NativesProvider p : NativesProvider.providers){
			if(p.isAvailable()){
				npv = p;
				break;
			}
		}
		
		if(npv == null){
			getServer().getLogger().warning(ChatColor.stripColor(MSG_EX_PREFIX + "Natives were not found, we can't live like this!."));
			getPluginLoader().disablePlugin(this);
		}
		else{
			getServer().getLogger().info(ChatColor.stripColor(MSG_EX_PREFIX + "Found natives: [" + npv.getName() + ']'));
			rl = new Relighter(this);
		}
		
		
		if(wep != null){
			getServer().getLogger().info(ChatColor.stripColor(MSG_PREFIX + "WorldEdit found, you can use the wand to select blocks!"));
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
			return rl.relightChunkRadius(centerChunk, radius);
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
			return rl.relightChunk(chunk);
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
			return rl.relightBlock(block);
		}
		else return false;
	}
}
