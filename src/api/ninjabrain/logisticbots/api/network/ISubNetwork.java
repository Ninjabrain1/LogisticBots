package ninjabrain.logisticbots.api.network;

/**
 * A part of a Logistics Network that handles interactions between one type of
 * logistic chests and logistic robots.
 * 
 * @param <T> The type of IStorables this sub network handles.
 */
public interface ISubNetwork<T extends IStorable> {
	
	/**
	 * Called every tick of the world this Network is attached to.
	 */
	public void onUpdate();
	
	/**
	 * Returns true if the network can add the storage
	 */
	public boolean canAddStorage(INetworkStorage<T> storage);
	
	/**
	 * Adds the storage to this network. It is adviced to not call this method
	 * directly, see {@link NetworkManager#addNetworkStorage(INetworkStorage)}.
	 * 
	 * @param storage
	 * The storage that should be added
	 */
	public void addStorage(INetworkStorage<T> storage);
	
	/**
	 * Removes the storage from this network. It is adviced to not call this method
	 * directly, see {@link NetworkManager#removeNetworkStorage(INetworkStorage)}.
	 */
	public void removeStorage(INetworkStorage<T> storage);
	
	/**
	 * Returns true if this INetwork can add the given {@link ITransporter} to this
	 * network.
	 */
	public boolean canAddTransporter(ITransporter<T> transporter);
	
	/**
	 * Adds the {@link ITransporter} to this network.
	 */
	public void addTransporter(ITransporter<T> transporter);
	
	/**
	 * Removes the {@link ITransporter} from this network.
	 */
	public void removeTransporter(ITransporter<T> transporter);
	
	/**
	 * Tells the network that the given storage wants the given storable delivered
	 * to it.
	 */
	public void addWanted(INetworkStorage<T> storage, T storable);
	
	/**
	 * Tells the network that the given storage wants to get rid of the given
	 * storable from its inventory.
	 */
	public void addUnwanted(INetworkStorage<T> storage, T storable);
	
	/**
	 * Called when the given provider is removed.
	 */
	public void onProviderRemoved(INetworkProvider provider);
	
	/**
	 * Returns the type of this network.
	 */
	public Class<T> getType();
	
}
