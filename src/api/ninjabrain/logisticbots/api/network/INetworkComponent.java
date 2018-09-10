package ninjabrain.logisticbots.api.network;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface INetworkComponent {
	
	/**
	 * Returns the world this storage is in
	 */
	public World getWorld();
	
	/**
	 * Returns the INetwork this storage belongs to, null if none
	 */
	public INetwork getNetwork();
	
	/**
	 * Sets the INetwork this storage belongs to
	 */
	public void setNetwork(INetwork network);
	
	/**
	 * Returns the position of this component in the world.
	 */
	public BlockPos getPos();
	
}
