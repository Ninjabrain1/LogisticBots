package ninjabrain.logisticbots.api.network;

import net.minecraft.util.math.BlockPos;

/**
 * Represents a task for a {@link ITransporter} to complete.
 */
public interface ITask<T extends IStorable> {
	
	/**
	 * Returns the position the transporter has to be at in order to complete the
	 * task.
	 */
	public BlockPos getDesiredPos();
	
	/**
	 * Called when this task is completed.
	 * 
	 * @param transporter
	 * The transporter that completed this task.
	 */
	public void onComplete(ITransporter<T> transporter);
	
	/**
	 * Returns the next task for the transporter that should be complete after this
	 * task is completed. Returns null if no other task should be completed.
	 */
	public ITask<T> getNextTask();
	
	/**
	 * Sets the next task for the transporter that should be complete after this
	 * task is completed.
	 */
	public void setNextTask(ITask<T> task);
	
}
