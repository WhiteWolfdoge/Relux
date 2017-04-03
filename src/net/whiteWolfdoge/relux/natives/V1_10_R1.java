package net.whiteWolfdoge.relux.natives;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;

import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.PlayerChunkMap;
import net.minecraft.server.v1_10_R1.WorldServer;

/*
 * NMS v1_10_R1
 * (Minecraft 1.10.2)
 */
class V1_10_R1 implements NativesProvider{
	private final String alias;
	private final String name;
	private final boolean avail;
	
	public V1_10_R1(){
		alias = "V1_10_R1";
		name = "Minecraft 1.10.2";
		
		boolean pass = false;
		try{
			Class.forName("net.minecraft.server.v1_10_R1.WorldServer");
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
		
		return worldSrv.w(blkPos); // Magic!
	}
}
