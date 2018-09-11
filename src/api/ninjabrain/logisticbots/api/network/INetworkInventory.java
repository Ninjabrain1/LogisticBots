package ninjabrain.logisticbots.api.network;

/**
 * Something that can have IStorables inserted/extracted.
 */
public interface INetworkInventory<T extends IStorable> {
	
	/**
	 * Inserts as much as possible of the given IStorable and returns a IStorable
	 * that represents the stuff that was not picked up. The returned stack can be
	 * safely modified.
	 * 
	 * @param storable
	 * The storable to insert
	 * @param simulate
	 * If true the insertion is only simulated
	 * @return The remaining IStorable that was not inserted
	 */
	public T insert(T storable, boolean simulate);
	
	/**
	 * Extracts as much as possible of the requested IStorable from this
	 * ITransporter and returns how much was actually extracted (removed from this
	 * transporters inventory). The returned stack can be safely modified.
	 * 
	 * @param storable
	 * The storable that should be extracted
	 * @param simulate
	 * If true the extraction is only simulated
	 * @return The IStorable that was subtracted from this transporters inventory
	 */
	public T extract(T storable, boolean simulate);
	
	/**
	 * Returns the type of IStorable this inventory stores.
	 */
	public Class<T> getStorableType();
	
}
