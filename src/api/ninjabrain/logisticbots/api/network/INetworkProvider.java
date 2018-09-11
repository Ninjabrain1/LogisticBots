package ninjabrain.logisticbots.api.network;

/**
 * A "source" of a Logistic Network (Roboport for example). To notify the world
 * that an instance of your INetworkProvider has been created call
 * {@link NetworkManager#addNetworkProvider}
 */
public interface INetworkProvider<T extends IStorable> extends INetworkComponent<T> {
	
	// TODO remove generic type from INetworkProvider
	
	/**
	 * On being placed this method will be called if this INetworkProvider is not
	 * compatible with any other network.
	 */
	public INetwork<T> createNewNetwork();
	

	/**
	 * Returns the type of IStorables the resulting network will transport.
	 */
	public Class<T> getType();
	
}
