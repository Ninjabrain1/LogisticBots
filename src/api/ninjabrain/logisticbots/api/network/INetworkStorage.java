package ninjabrain.logisticbots.api.network;

import net.minecraft.util.math.BlockPos;

/**
 * A component of a Logistic Network that stores things. To notify the world,
 * and all Logistic Networks, that an instance of your INetworkStorage has been
 * created call
 * {@link NetworkManager#addNetworkStorage(INetworkStorage, boolean, boolean, int)}.
 * Remember to call {@link NetworkManager#removeNetworkStorage(INetworkStorage)}
 * when the implementer of this interface is removed from the world.
 */
public interface INetworkStorage<T extends IStorable> extends INetworkComponent {
	
	/**
	 * Returns the position of this storage in the world
	 */
	public BlockPos getPos();
	
}
