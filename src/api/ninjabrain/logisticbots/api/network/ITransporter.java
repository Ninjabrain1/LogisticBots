package ninjabrain.logisticbots.api.network;

/**
 * A component of a Logistic Network that can transport {@link IStorables} from
 * one {@link INetworkStorage} to another.
 */
public interface ITransporter<T extends IStorable> extends INetworkComponent<T>, INetworkInventory<T> {
	
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
	
	/**
	 * Returns true if this ITransporter is carrying something.
	 */
	public boolean isCarryingSomething();
	
	/**
	 * Called when this transporter is added to a ITransporterStorage.
	 */
	public void onAddedToTransporterStorage(ITransporterStorage storage);
	
	/**
	 * Returns the type of IStorable this can transport.
	 */
	public Class<T> getType();
	
}
