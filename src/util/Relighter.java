package util;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.World;

import org.bukkit.Chunk;


public class Relighter{
	/**
	 * Use the following method to relight a chunk
	 * @param Chunk chk		The chunk to be relighted
	 */
	public static void relightChunk(Chunk chk){
		for(byte xLoc = 0; xLoc <= 15; xLoc++){ // X axis
			for(byte yLoc = (byte)255; yLoc >= 0; yLoc--){ // Y axis
				for(byte zLoc = 0; zLoc <= 15; zLoc++){ // Z axis
					BlockPosition bp = new BlockPosition(xLoc, yLoc, zLoc);
					World wld = (World)chk.getWorld();
					wld.x(bp); // Magic!
				}
			}
		}
		
		// TODO all
	}
}
