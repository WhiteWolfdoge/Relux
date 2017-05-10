package net.whiteWolfdoge.relux;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.sk89q.worldedit.bukkit.selections.Selection;

class Relighter{
	private final ReluxPlugin rp;
	
	// Access restriction constructor
	protected Relighter(ReluxPlugin newRP){
		rp = newRP;
	}
	
	/**
	 * Use the following method to relight a WorldEdit selection
	 * @param rs	The WorldEdit selection to be relit
	 * @return		<b>true</b> if the operation was sucessful, <b>false</b> otherwise
	 */
	protected boolean relightWESelection(Selection rs){
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
		
		int yMin, yMax;
		if(min.getBlockY() <= max.getBlockY()){
			yMin = min.getBlockY();
			yMax = max.getBlockY();
		}
		else{
			yMin = max.getBlockY();
			yMax = min.getBlockY();
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
		
		boolean success = true;
		for(int forX = xMin; forX <= xMax; forX++){ // For every selected x axis, west to east
			for(int forY = yMax; forX >= yMin; forY--){ // For every selected y axis, top to bottom
				for(int forZ = zMin; forZ <= zMax; forZ++){ // For every selected z axis, north to south
					if(relightBlock(rs.getWorld().getBlockAt(forX, forY, forZ))) ; // If the relight was successful
					else{ // Else failure, abort
						success = false;
						break;
					}
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
	protected boolean relightChunkRadius(Chunk cenChk, byte rad){
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
	protected boolean relightChunk(Chunk chk){
		boolean relightIssue = false;
		
		if(!chk.load()) return false; // The chunk could not be loaded!
		
		for(byte xLoc = 0; xLoc <= 15 && !relightIssue; xLoc++){ // For every block on the x axis
			for(int yLoc = 255; yLoc >= 0 && !relightIssue; yLoc--){ // For every block on the y axis, top to bottom
				for(byte zLoc = 0; zLoc <= 15 && !relightIssue; zLoc++){ // For every block on the z axis
					Block currBlk = chk.getBlock(xLoc, yLoc, zLoc); // Pick the block in the chunk at the relative position
					if(!relightBlock(currBlk)) relightIssue = true; // Relight the picked block, else flags relight issue
				}
			}
		}
		
		if(!relightIssue){
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
	protected boolean relightBlock(Block blk){
		
		return rp.npv.relightBlock(blk);
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
