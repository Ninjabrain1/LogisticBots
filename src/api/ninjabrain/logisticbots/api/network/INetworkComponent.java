package ninjabrain.logisticbots.api.network;

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
	
}
