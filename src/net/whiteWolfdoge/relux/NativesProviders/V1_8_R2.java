package net.whiteWolfdoge.relux.NativesProviders;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;

import net.minecraft.server.v1_8_R2.BlockPosition;
import net.minecraft.server.v1_8_R2.PlayerChunkMap;
import net.minecraft.server.v1_8_R2.WorldServer;

/*
 * NMS v1_8_R2
 * (Minecraft 1.8.3)
 */
class V1_8_R2 implements NativesProvider{
	private final String alias;
	private final String name;
	private final boolean avail;
	
	public V1_8_R2(){
		alias = "V1_8_R2";
		name = "Minecraft 1.8.3";
		
		boolean pass = false;
		try{
			Class.forName("net.minecraft.server.v1_8_R2.WorldServer");
			pass = true;
		}
		catch(ClassNotFoundException cnfex){
			// Continue
		}
		
		avail = pass;
	}
	
	@Override
	public String getAlias(){
		return alias;
	}
	
	@Override
	public String getName(){
		return name;
	}
	
	@Override
	public boolean isAvailable(){
		return avail;
	}
	
	@Override
	public boolean relightBlock(Block blk){
		BlockPosition blkPos = new BlockPosition(blk.getX(), blk.getY(), blk.getZ());
		
		CraftWorld craftWld = (CraftWorld)blk.getWorld();
		WorldServer worldSrv = craftWld.getHandle();
		
		PlayerChunkMap pcm = worldSrv.getPlayerChunkMap();
		pcm.flagDirty(blkPos);
		
		return worldSrv.x(blkPos); // Magic!
	}
}
