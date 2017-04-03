package net.whiteWolfdoge.relux.NativesProviders;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PlayerChunkMap;
import net.minecraft.server.v1_8_R3.WorldServer;

/*
 * NMS v1_8_R3
 * (Minecraft 1.8.7 / Minecraft 1.8.8)
 */
class V1_8_R3 implements NativesProvider{
	private final String alias;
	private final String name;
	private final boolean avail;
	
	public V1_8_R3(){
		alias = "V1_8_R3";
		name = "Minecraft 1.8.7 / Minecraft 1.8.8";
		
		boolean pass = false;
		try{
			Class.forName("net.minecraft.server.v1_8_R3.WorldServer");
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
