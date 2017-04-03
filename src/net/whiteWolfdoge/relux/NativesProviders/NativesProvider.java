package net.whiteWolfdoge.relux.NativesProviders;

import org.bukkit.block.Block;

/**
 * A Natives Provider allows usage of certain native Minecraft methods.
 */
public interface NativesProvider{
	public NativesProvider providers[] = { //@formatter:off
		new V1_11_R1(),
		new V1_10_R1(),
		new V1_9_R2(),
		new V1_9_R1(),
		new V1_8_R3(),
		new V1_8_R2(),
		new V1_8_R1()
	}; //@formatter:on
	
	/**
	 * This method gets the alias of this Natives Provider.
	 * @return The alias of this Natives Provider
	 */
	abstract public String getAlias();
	
	/**
	 * This method gets the name of this Natives Provider.
	 * @return The name of this Natives Provider
	 */
	abstract public String getName();
	
	/**
	 * This method gets whether this Natives Provider is available for usage.
	 * @return <b>true</b> if available, <b>false</b> otherwise
	 */
	abstract public boolean isAvailable();
	
	/**
	 * This method relights a specified block.
	 * @return <b>true</b> if successful, <b>false</b> otherwise
	 */
	abstract public boolean relightBlock(Block blk);
}
