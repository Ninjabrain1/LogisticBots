package ninjabrain.logisticbots.api.network;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A component of a Logistic Network that stores things. To notify the world,
 * and all Logistic Networks, that an instance of your INetworkStorage has been
 * created call
 * {@link NetworkManager#addNetworkStorage(INetworkStorage, boolean, boolean, int)}.
 * Remember to call {@link NetworkManager#removeNetworkStorage(INetworkStorage)}
 * when the implementer of this interface is removed from the world.
 */
public interface INetworkStorage<T extends IStorable> {
	
	/**
	 * Returns the position of this storage in the world
	 */
	public BlockPos getPos();
	
	/**
	 * Returns the world this storage is in
	 */
	public World getWorld();
	
	/**
	 * Returns the INetwork this storage belongs to, null if none
	 */
	public INetwork getNetwork();
	
	// /**
	// *
	// */
	// public boolean allows
	//
	// /**
	// * Returns the priority of this storage. Convention is that it is more
	// "desirable" that a IStorable is in a INetworkStorage with high priority.
	// */
	// public int getPriority() {
	//
	// }
}
