package util;

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
		for(byte xLoc = 0; xLoc <= 15; xLoc++){ // X axis
			for(byte yLoc = (byte)255; yLoc >= 0; yLoc--){ // Y axis
				for(byte zLoc = 0; zLoc <= 15; zLoc++){ // Z axis
					Block blk = 	chk.getBlock(xLoc, yLoc, zLoc); // The block at the current xyz
					relightBlock(blk);
				}
			}
		}
	}
	
	/**
	 * Use the following method to relight a single block
	 * @param Block blk		The block to be relighted
	 */
	public static void relightBlock(Block blk){
		World wld =			(World)blk.getWorld();
		int xLoc =			blk.getX();
		int yLoc =			blk.getY();
		int zLoc = 			blk.getZ();
		BlockPosition bp =	new BlockPosition(xLoc, yLoc, zLoc);
		wld.x(bp); // Magic!
	}
}
