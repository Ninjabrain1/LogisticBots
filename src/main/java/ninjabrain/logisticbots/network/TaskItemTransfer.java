package ninjabrain.logisticbots.network;

import net.minecraft.tileentity.TileEntity;
import ninjabrain.logisticbots.api.network.INetworkStorage;
import ninjabrain.logisticbots.api.network.ITransporter;
import ninjabrain.logisticbots.tile.TileSimpleInventory;

public class TaskItemTransfer extends TaskMovement<LBItemStack> {
	
	boolean pickUp;
	LBItemStack lbItemStack;
	
	public TaskItemTransfer(INetworkStorage<LBItemStack> storage, LBItemStack lbItemStack, boolean pickUp) {
		super(storage.getPos());
		this.pickUp = pickUp;
		this.lbItemStack = lbItemStack;
	}
	
	@Override
	public void onComplete(ITransporter<LBItemStack> transporter) {
		super.onComplete(transporter);
		
		TileEntity te = transporter.getWorld().getTileEntity(pos);
		if (te instanceof TileSimpleInventory) {
			TileSimpleInventory tsi = (TileSimpleInventory) te;
			if (pickUp) {
				LBItemStack remainder = transporter.insert(tsi.extract(lbItemStack, false), false);
				tsi.insert(remainder, false);
			} else {
				LBItemStack remainder = tsi.insert(transporter.extract(lbItemStack, false), false);
				transporter.insert(remainder, false);
			}
		}
	}
	
}
