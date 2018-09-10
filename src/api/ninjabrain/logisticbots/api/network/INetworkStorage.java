package ninjabrain.logisticbots.api.network;

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
	 * Whether the INetwork can insert items to the storage unconditionally. It is
	 * assumed that the result of this methof does not change over time.
	 */
	public boolean hasOpenInput();
	
	/**
	 * Whether the INetwork can extract items from the storage unconditionally. It
	 * is assumed that the result of this methof does not change over time.
	 */
	public boolean hasOpenOutput();
	
	/**
	 * Returns the storage's priority. The INetwork wants to extract from storages
	 * with low priority and insert to storages with high priority. Exactly how the
	 * network will handle priorities depends on the implementation. It is assumed
	 * that the priority does not change over time.
	 */
	public int getPriority();
	
}
