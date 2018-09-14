package ninjabrain.logisticbots.api.network;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A "source" of a Logistic Network (Roboport for example). To notify the world
 * that an instance of your INetworkProvider has been created call
 * {@link NetworkManager#addNetworkProvider}
 */
public interface INetworkProvider {
	
	// TODO remove generic type from INetworkProvider
	
	/**
	 * On being placed this method will be called if this INetworkProvider is not
	 * compatible with any other network.
	 */
	public INetwork createNewNetwork();
	
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
