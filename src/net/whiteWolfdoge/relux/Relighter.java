package net.whiteWolfdoge.relux;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

class Relighter{
	/**
	 * Use the following method to relight a radius of chunks.
	 * @param	cenChk		The center chunk to be relighted.
	 * @param	rad			The radius of the chunks to be relighted.
	 * @return				<b>true</b> if the task is successful.
	 */
	protected static boolean relightChunkRadius(Chunk cenChk, byte rad){
		return relightChunk(cenChk); //TODO actually do all chunks
	}
	
	/**
	 * Use the following method to relight a single chunk.
	 * @param chk		The chunk to be relighted
	 * @return			<b>true</b> if the task is successful.
	 */
	protected static boolean relightChunk(Chunk chk){
		// Collect all affected Chunks
		Chunk[] affectedChks = new Chunk[9];
		for(int xLoc = chk.getX() - 1; xLoc < chk.getX() + 2; xLoc++){ // Iterate 3 times on x, starting one chunk west
			for(int zLoc = chk.getZ() - 1; zLoc < chk.getZ() + 2; zLoc++){ // Iterate 3 times on y, starting one chunk north
				affectedChks[(xLoc - chk.getX() + 1) * 3 + (zLoc - chk.getZ() + 1)] = chk.getWorld().getChunkAt(xLoc, zLoc);
			}
		}
		
		// @formatter:off
		boolean loadIssue =		false;
		boolean relightIssue =	false;
		// @formatter:on
		for(int currChk = 0; currChk < affectedChks.length && !loadIssue; currChk++){ // For every chunk, while there is no loading issue
			if(!affectedChks[currChk].load()) loadIssue = true; // If chunk loading was NOT successful, flag loading issue
			else; // Else it's ready to be relighted
		}
		
		if(!loadIssue){ // If there was no loading issue with the affected chunks
			for(byte xLoc = 0; xLoc <= 15 && !relightIssue; xLoc++){ // For every block on the x axis
				for(int yLoc = 255; yLoc >= 0 && !relightIssue; yLoc--){ // For every block on the y axis, top to bottom
					for(byte zLoc = 0; zLoc <= 15 && !relightIssue; zLoc++){ // For every block on the z axis
						Block currBlk = chk.getBlock(xLoc, yLoc, zLoc); // Pick the block in the chunk at the relative position
						if(!relightBlock(currBlk)) relightIssue = true; // Relight the picked block, else flags relight issue
					}
				}
			}
		}
		
		if(!loadIssue && !relightIssue){
			// TODO Send the affected players the updated data
			Bukkit.getLogger().info(ChatColor.stripColor(String.format(ReluxPlugin.MSG_PREFIX + "Relit chunk (%d, %d) in world '%s'", chk.getX(), chk.getZ(), chk.getWorld().getName()))); // Log the relight
			
			return true;
		}
		else{
			// TODO Send the affected players the updated data
			Bukkit.getLogger().info(ChatColor.stripColor(String.format(ReluxPlugin.MSG_EX_PREFIX + "Exception relighting chunk (%d, %d) in world '%s'", chk.getX(), chk.getZ(), chk.getWorld().getName()))); // Log the attempt
			
			return false;
		}
	}
	
	/**
	 * Use the following method to relight a single block.
	 * @param blk	The block to be relighted<br />
	 * <b>Note:</b> The chunk must be loaded for this method to work!
	 * @return 		<b>true</b> if the task is successful.
	 */
	protected static boolean relightBlock(Block blk){
		int blockX = blk.getX();
		int blockY = blk.getY();
		int blockZ = blk.getZ();
		World blockWorld = blk.getWorld();
		
		return Native.relightBlock(blockX, blockY, blockZ, blockWorld);
	}
}
