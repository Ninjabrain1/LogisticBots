package ninjabrain.logisticbots.network;

import net.minecraft.tileentity.TileEntity;
import ninjabrain.logisticbots.api.network.INetworkStorage;
import ninjabrain.logisticbots.api.network.ITransporter;
import ninjabrain.logisticbots.tile.TileSimpleInventory;

public class TaskItemTransfer extends TaskMovement<LBItemStack> {
	
	boolean pickUp;
	
	public TaskItemTransfer(INetworkStorage<LBItemStack> storage, boolean pickUp) {
		super(storage.getPos());
		this.pickUp = pickUp;
	}
	
	@Override
	public void onComplete(ITransporter<LBItemStack> transporter) {
		super.onComplete(transporter);
		
		TileEntity te = transporter.getWorld().getTileEntity(pos);
		if (te instanceof TileSimpleInventory) {
			TileSimpleInventory tsi = (TileSimpleInventory) te;
			if (pickUp) {
				LBItemStack remainder = transporter.insert(tsi.extract(LBItemStack.ANY_STACK, false), false);
				tsi.insert(remainder, false);
			} else {
				LBItemStack remainder = tsi.insert(transporter.extract(LBItemStack.ANY_STACK, false), false);
				transporter.insert(remainder, false);
			}
		}
	}
	
}
