package net.whiteWolfdoge.relux.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;

import net.whiteWolfdoge.relux.ReluxPlugin;

class Native{
	
	/**
	 * <b>Note:</b> This method makes native calls and will need to be updated when the NMS version changes.
	 * @param inBlockX		The X coordomate of the block
	 * @param inBlockY		The Y coordomate of the block
	 * @param inBlockZ		The Y coordomate of the block
	 * @param inBlockWorld	The world the block resides in
	 * @return The result from the native method.
	 **/
	/*This method calls the following native method:
	 * @formatter:off
		public boolean ?(BlockPosition blockposition){
			boolean flag = false;
			
			if(!this.worldProvider.o()){
				flag |= this.c(EnumSkyBlock.SKY, blockposition);
			}
			
			flag |= this.c(EnumSkyBlock.BLOCK, blockposition);
			return flag;
		}
		@formatter:on
	 */
	protected static boolean relightBlock(int inBlockX, int inBlockY, int inBlockZ, World inBlockWorld){
		ClassLoader cl = Bukkit.getServer().getClass().getClassLoader();
		
		/*
		 * NMS_v1_8_R3
		 * (Minecraft 1.8.7 / CraftBukkit 1.8.7 / Spigot 1.8.7)
		 * (Minecraft 1.8.8 / CraftBukkit 1.8.8 / Spigot 1.8.8)
		 */
		try{
			Class.forName("net.minecraft.server.v1_8_R3.WorldServer", false, cl); // Probe an NMS native
			
			net.minecraft.server.v1_8_R3.BlockPosition blkPos = new net.minecraft.server.v1_8_R3.BlockPosition(inBlockX, inBlockY, inBlockZ);
			
			org.bukkit.craftbukkit.v1_8_R3.CraftWorld craftWld = (org.bukkit.craftbukkit.v1_8_R3.CraftWorld)inBlockWorld;
			net.minecraft.server.v1_8_R3.WorldServer worldSrv = craftWld.getHandle();
			
			return worldSrv.x(blkPos); // Magic!
		}
		catch(ClassNotFoundException cnfex){
			// Continue
		}
		
		/*
		 * NMS v1_9_R1
		 * Minecraft 1.9.2 / CraftBukkit 1.9.2 / Spigot 1.9.2)
		 */
		try{
			Class.forName("net.minecraft.server.v1_9_R1.WorldServer", false, cl); // Probe an NMS native
			
			net.minecraft.server.v1_9_R1.BlockPosition blkPos = new net.minecraft.server.v1_9_R1.BlockPosition(inBlockX, inBlockY, inBlockZ);
			
			org.bukkit.craftbukkit.v1_9_R1.CraftWorld craftWld = (org.bukkit.craftbukkit.v1_9_R1.CraftWorld)inBlockWorld;
			net.minecraft.server.v1_9_R1.WorldServer worldSrv = craftWld.getHandle();
			
			return worldSrv.w(blkPos); // Magic!
		}
		catch(ClassNotFoundException cnfex){
			// Continue
		}
		
		Bukkit.getLogger().warning(ChatColor.stripColor(ReluxPlugin.MSG_EX_PREFIX + "Natives were not found, try updating."));
		
		return false;
	}
}
