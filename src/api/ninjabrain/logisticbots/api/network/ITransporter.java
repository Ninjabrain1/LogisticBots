package ninjabrain.logisticbots.api.network;

/**
 * A component of a Logistic Network that can transport {@link IStorables} from
 * one {@link INetworkStorage} to another.
 */
public interface ITransporter<T extends IStorable> {
	
	/**
	 * Inserts as much as possible of the given IStorable and returns a IStorable
	 * that represents the stuff that was not picked up.
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
	 * transporters inventory).
	 * 
	 * @param storable
	 * The storable that should be extracted
	 * @param simulate
	 * If true the extraction is only simulated
	 * @return The IStorable that was subtracted from this transporters inventory
	 */
	public T extract(T storable, boolean simulate);
	
	/**
	 * Called once every tick. Updates stuff relevant to the task. 
	 */
	public void updateTask();
	
	/**
	 * Gives this ITransporter a task to complete.
	 */
	public void setTask(ITask<T> task);
	
	/**
	 * Returns true if this ITransporter is already working on a task.
	 */
	public boolean hasTask();
	
}
