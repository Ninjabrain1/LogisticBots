package ninjabrain.logisticbots.network;

import net.minecraft.util.math.BlockPos;
import ninjabrain.logisticbots.api.network.IStorable;
import ninjabrain.logisticbots.api.network.ITask;
import ninjabrain.logisticbots.api.network.ITransporter;

/**
 * Tells an ITransporter to move to a specific location.
 */
public class TaskMovement<T extends IStorable> implements ITask<T> {
	
	BlockPos pos;
	ITask<T> nextTask;
	
	public TaskMovement(BlockPos pos) {
		this.pos = pos;
	}
	
	@Override
	public BlockPos getDesiredPos() {
		return pos;
	}
	
	@Override
	public void onComplete(ITransporter<T> transporter) {
		
	}
	
	@Override
	public ITask<T> getNextTask() {
		return nextTask;
	}
	
	@Override
	public void setNextTask(ITask<T> task) {
		this.nextTask = task;
	}
	
}
