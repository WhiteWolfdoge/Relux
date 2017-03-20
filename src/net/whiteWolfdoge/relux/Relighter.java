package net.whiteWolfdoge.relux;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.sk89q.worldedit.bukkit.selections.Selection;

class Relighter{
	
	// Access restriction constructor
	protected Relighter(){
		// Do nothing
	}
	
	/**
	 * Use the following method to relight a WorldEdit selection
	 * @param rs	The WorldEdit selection to be relit
	 * @return		<b>true</b> if the operation was sucessful, <b>false</b> otherwise
	 */
	protected static boolean relightWESelection(Selection rs){
		Location min = rs.getMinimumPoint();
		Location max = rs.getMaximumPoint();
		
		int xMin, xMax;
		if(min.getBlockX() <= max.getBlockX()){
			xMin = min.getBlockX();
			xMax = max.getBlockX();
		}
		else{
			xMin = max.getBlockX();
			xMax = min.getBlockX();
		}
		
		int zMin, zMax;
		if(min.getBlockZ() <= max.getBlockZ()){
			zMin = min.getBlockZ();
			zMax = max.getBlockZ();
		}
		else{
			zMin = max.getBlockZ();
			zMax = min.getBlockZ();
		}
		
		Block nwBlock = rs.getWorld().getBlockAt(xMin, 127, zMin);
		Block seBlock = rs.getWorld().getBlockAt(xMax, 127, zMax);
		Chunk nwChunk = nwBlock.getChunk();
		Chunk seChunk = seBlock.getChunk();
		
		boolean success = true;
		int length = Math.abs(seChunk.getX() - nwChunk.getX());
		int height = Math.abs(seChunk.getZ() - nwChunk.getZ());
		for(int forX = 0; forX < length && success; forX++){ // For every x in length
			for(int forZ = 0; forZ < height && success; forZ++){ // For every z in width
				Chunk currChk = rs.getWorld().getChunkAt(nwChunk.getX() + forX, nwChunk.getZ() + forZ); // Pick chunk
				if(relightChunk(currChk)) ; // Continue if relight was sucessful
				else{ // Else abort
					success = false;
					break;
				}
			}
		}
		
		return success;
	}
	
	/**
	 * Use the following method to relight a radius of chunks.
	 * @param	cenChk		The center chunk to be relighted.
	 * @param	rad			The radius of the chunks to be relighted.
	 * @return				<b>true</b> if the task is successful.
	 */
	protected static boolean relightChunkRadius(Chunk cenChk, byte rad){
		int cenX = cenChk.getX(), cenZ = cenChk.getZ();
		World wld = cenChk.getWorld();
		int nwX = cenX - (rad - 1), nwZ = cenZ - (rad - 1);
		
		int edgeLength = (rad * 2) - 1;
		Chunk[] affectedChks = new Chunk[sq(edgeLength)];
		int insertIndex = 0;
		for(int forZ = 0; forZ < edgeLength; forZ++) {
			for(int forX = 0; forX < edgeLength; forX++){
				Chunk currChk = wld.getChunkAt(nwX + forX, nwZ + forZ);
				affectedChks[insertIndex++] = currChk;
			}
		}
		
		for(Chunk chk : affectedChks){
			if(!relightChunk(chk)) return false;
		}
		
		return true;
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
			// String op = String.format("chunks within %d of Chunk(%d, %d, %s)", rad, chX, chZ, chWld);
			Bukkit.getLogger().info(ChatColor.stripColor(String.format(ReluxPlugin.MSG_PREFIX + "Relit Chunk(%d, %d, %s)", chk.getX(), chk.getZ(), chk.getWorld().getName()))); // Log the relight
			
			return true;
		}
		else{
			// TODO Send the affected players the updated data
			Bukkit.getLogger().info(ChatColor.stripColor(String.format(ReluxPlugin.MSG_EX_PREFIX + "Exception relighting Chunk(%d, %d, %s)", chk.getX(), chk.getZ(), chk.getWorld().getName()))); // Log the attempt
			
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
	
	/**
	 * The following method squares an integer.
	 * @param int	The integer to be squared
	 * @return		The squared integer
	 */
	private static int sq(int num){
		return num * num;
	}
}
