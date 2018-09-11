package ninjabrain.logisticbots.api.network;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface INetworkComponent<T extends IStorable> {
	
	/**
	 * Returns the world this storage is in
	 */
	public World getWorld();
	
	/**
	 * Returns the INetwork this storage belongs to, null if none
	 */
	public INetwork<T> getNetwork();
	
	/**
	 * Sets the INetwork this storage belongs to
	 */
	public void setNetwork(INetwork<T> network);
	
	/**
	 * Returns the position of this component in the world.
	 */
	public BlockPos getPos();
	
}
