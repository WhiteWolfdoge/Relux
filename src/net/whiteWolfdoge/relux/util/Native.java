package net.whiteWolfdoge.relux.util;

import org.bukkit.Bukkit;
import org.bukkit.World;

import net.whiteWolfdoge.relux.ReluxPlugin;

class Native{
	
	/**
	 * <b>Note:</b> This method makes native calls and will need to be updated when the NMS version changes.
	 * @param inBlockX
	 * @param inBlockY
	 * @param inBlockZ
	 * @param inBlockWorld
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
		try{ // NMS v1_9_R1
			Class.forName("net.minecraft.server.v1_9_R1.WorldServer", false, Class.class.getClassLoader()); // Probe an NMS native
			net.minecraft.server.v1_9_R1.BlockPosition blkPos = new net.minecraft.server.v1_9_R1.BlockPosition(inBlockX, inBlockY, inBlockZ);
			
			org.bukkit.craftbukkit.v1_9_R1.CraftWorld craftWld = (org.bukkit.craftbukkit.v1_9_R1.CraftWorld)inBlockWorld;
			net.minecraft.server.v1_9_R1.WorldServer worldSrv = craftWld.getHandle();
			return worldSrv.w(blkPos); // Magic!
		}
		catch(ClassNotFoundException cnfex){
			Bukkit.getLogger().warning(ReluxPlugin.MSG_EX_PREFIX + "Natives were not found, try updating");
		}
		
		return false;
	}
}
