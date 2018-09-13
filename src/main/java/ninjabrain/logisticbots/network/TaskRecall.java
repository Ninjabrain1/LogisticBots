package ninjabrain.logisticbots.network;

import ninjabrain.logisticbots.api.network.IStorable;
import ninjabrain.logisticbots.api.network.ITransporter;
import ninjabrain.logisticbots.api.network.ITransporterStorage;

public class TaskRecall extends TaskMovement<IStorable> {
	
	ITransporterStorage dest;
	
	public TaskRecall(ITransporterStorage dest) {
		super(dest.getPos());
		this.dest = dest;
	}
	
	@Override
	public void onComplete(ITransporter<IStorable> transporter) {
		super.onComplete(transporter);
		
		if (dest.hasSpace(transporter)) {
			dest.insert(transporter);
			transporter.onAddedToTransporterStorage(dest);
		}
	}
	
}
