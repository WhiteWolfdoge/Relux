package net.whiteWolfdoge.relux.task;

import java.util.Arrays;

import org.bukkit.World;
import org.bukkit.block.Block;

public class RelightRegion{
	private int blkTotal;
	private World wld;
	private Block[] blkList;
	
	/**
	 * Constructor to restrict access
	 */
	@SuppressWarnings("unused")
	private RelightRegion(){
		// Do nothing
	}
	
	public RelightRegion(Block[] newBlkList){
		if(newBlkList == null || newBlkList.length == 0){ // Verify valid array
			throw new IllegalArgumentException("A RelightTask must contain at least one block.");
		}
		blkTotal = newBlkList.length;
		wld = newBlkList[0].getWorld();
		blkList = Arrays.copyOf(newBlkList, newBlkList.length); // Take a clone of the array
	}
}
