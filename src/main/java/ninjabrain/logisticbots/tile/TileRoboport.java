package ninjabrain.logisticbots.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import ninjabrain.logisticbots.network.Network;

/**
 * Roboport tile entity that contains a Logistic Network
 */
public class TileRoboport extends TileEntity implements ITickable {
	
	Network network;
	
	@Override
	public void onLoad() {
		// TODO instead of checking world.isRemote all the time make client and server
		// specific networks
		if (!world.isRemote) {
			network = new Network(world);
			network.load();
		}
	}
	
	@Override
	public void onChunkUnload() {
		onRemove();
	}
	
	public void onRemove() {
		if (!world.isRemote)
			network.unload();
	}
	
	@Override
	public void update() {
		if (!world.isRemote)
			network.onUpdate();
	}
	
}
