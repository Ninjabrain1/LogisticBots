package ninjabrain.logisticbots.api.network;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface INetworkComponent<T extends IStorable> {
	
	/**
	 * Returns the world this storage is in
	 */
	public World getWorld();
	
	/**
	 * Returns the ISubNetwork this storage belongs to, null if none
	 */
	public ISubNetwork<T> getNetwork();
	
	/**
	 * Sets the ISubNetwork this storage belongs to
	 */
	public void setNetwork(ISubNetwork<T> network);
	
	/**
	 * Returns the position of this component in the world.
	 */
	public BlockPos getPos();
	
}
