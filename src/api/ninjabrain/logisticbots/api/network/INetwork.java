package ninjabrain.logisticbots.api.network;

/**
 * A Logistics Network. Handles interactions between different types of
 * logistics chests and robots.
 */
public interface INetwork {
	
	// /**
	// * Loads this Network to the game
	// */
	// public void load();
	//
	// /**
	// * Unloads this Network from the game
	// */
	// public void unload();
	
	/**
	 * Called every tick of the world this Network is attached to. Attach a network
	 * to a world by calling NetworkManager.addNetworkToWorld().
	 */
	public void onUpdate();
	
	/**
	 * Returns true if the network can add the storage
	 */
	public boolean canAddStorage(INetworkStorage<? extends IStorable> storage);
	
	/**
	 * Adds the storage to this network. It is adviced to not call this method
	 * directly, see
	 * {@link NetworkManager#addNetworkStorage(INetworkStorage, boolean, boolean, int)}.
	 * 
	 * @param storage
	 * The storage that should be added
	 */
	public void addStorage(INetworkStorage<? extends IStorable> storage);
	
	/**
	 * Removes the storage from this network. It is adviced to not call this method
	 * directly, see {@link NetworkManager#removeNetworkStorage(INetworkStorage)}.
	 */
	public void removeStorage(INetworkStorage<? extends IStorable> storage);
	
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
	 * Returns true if this INetwork can add the given {@link ITransporter} to this
	 * network.
	 */
	public <T extends IStorable> boolean canAddTransporter(ITransporter<T> transporter);
	
	/**
	 * Adds the {@link ITransporter} to this network.
	 */
	public <T extends IStorable> void addTransporter(ITransporter<T> transporter);
	
	/**
	 * Removes the {@link ITransporter} from this network.
	 */
	public void removeTransporter(ITransporter<? extends IStorable> transporter);
	
}
