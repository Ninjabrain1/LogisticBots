package ninjabrain.logisticbots.api.network;

import java.util.List;

import net.minecraft.util.math.BlockPos;

/**
 * A Logistic Network. Handles storing of transporters and merging with other
 * networks. Each Logistic Network has a set of ISubNetworks that each handle
 * storage and transportation of one type of IStorable.
 */
public interface INetwork {
	
	/**
	 * Returns true if the given block position is within range of this network.
	 */
	public boolean contains(BlockPos pos);
	
	/**
	 * Returns the ISubNetwork to this network that handles storables of the given
	 * type.
	 */
	public <T extends IStorable> ISubNetwork<T> getSubNetwork(Class<T> type);
	
	/**
	 * Returns true if this INetwork can merge with the given INetwork. This
	 * typically means that the networks are within range of each other, but there
	 * might also be other conditions.
	 */
	public boolean canMerge(INetwork network);
	
	/**
	 * Merges the given network with this one. This network will be the result, the
	 * given network should be discarded. It is adviced to not call this method
	 * directly, see {@link NetworkManager#addNetworkProvider(INetworkProvider)}.
	 */
	public void merge(INetwork network);
	
	/**
	 * Removes the given provider from this network, possibly splitting this network
	 * into two if the provider was the only link between them.
	 */
	public void removeProvider(INetworkProvider provider);
	
	/**
	 * Returns true if the network can add the transporter storage
	 */
	public boolean canAddTransporterStorage(ITransporterStorage storage);
	
	/**
	 * Adds the storage to this network. It is adviced to not call this method
	 * directly, see
	 * {@link NetworkManager#addTransporterStorage(ITransporterStorage)}.
	 * 
	 * @param storage
	 * The storage that should be added
	 */
	public void addTransporterStorage(ITransporterStorage storage);
	
	/**
	 * Removes the storage from this network. It is adviced to not call this method
	 * directly, see
	 * {@link NetworkManager#removeTransporterStorage(ITransporterStorage)}.
	 */
	public void removeTransporterStorage(ITransporterStorage storage);
	
	/**
	 * Returns the "best" storage for the transporter. This is typically the closest
	 * storage to the transporter that also has enough capacity to store the
	 * transporter.
	 * 
	 * @return The best ITransporterStorage for the transporter, null if no
	 * ITransporterStorage in the network can store the transporter.
	 */
	public ITransporterStorage getBestTransporterStorage(ITransporter<?> transporter);
	
	/**
	 * Returns a list of all ITransporterStorages in the network
	 */
	public List<ITransporterStorage> getTransporterStorages();
	
	/**
	 * Returns a list of all INetworkProviders in the network
	 */
	public List<INetworkProvider> getProviders();
	
	/**
	 * Called every tick from the world this Network is attached to. Attach a
	 * network to a world by calling NetworkManager.addNetworkToWorld().
	 */
	public void onUpdate();
}
