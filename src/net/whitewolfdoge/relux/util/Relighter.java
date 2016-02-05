package net.whiteWolfdoge.relux.util;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.World;

import org.bukkit.Chunk;
import org.bukkit.block.Block;


public class Relighter{
	
	/**
	 * Use the following method to relight a chunk
	 * @param Chunk chk		The chunk to be relighted
	 */
	public static void relightChunk(Chunk chk){
		if(!chk.isLoaded()){ // IF the chunk is NOT loaded
			chk.load(); // Try to load it
		}
		if(chk.isLoaded()){ // If the chunk is loaded now
			for(byte xLoc = 0; xLoc <= 15; xLoc++){ // For every block on the X axis
				for(byte yLoc = 0; yLoc <= 255; yLoc++){ // For every block on the Y axis
					for(byte zLoc = 0; zLoc <= 15; zLoc++){ // For every block on the Z axis
						relightBlock(chk.getBlock(xLoc, yLoc, xLoc)); // Relight the block
					}
				}
			}
		}
	}
	
	/**
	 * Use the following method to relight a single block
	 * @param Block blk		The block to be relighted
	 */
	public static void relightBlock(Block blk){
		//blk.getState().update(true);
		
		World wld =			(World)blk.getWorld();
		int xLoc =			blk.getX();
		int yLoc =			blk.getY();
		int zLoc = 			blk.getZ();
		BlockPosition bp =	new BlockPosition(xLoc, yLoc, zLoc);
		wld.x(bp); // Magic!
	}
}
