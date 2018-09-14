package ninjabrain.logisticbots.tile;

import net.minecraft.tileentity.TileEntity;
import ninjabrain.logisticbots.api.network.INetwork;
import ninjabrain.logisticbots.api.network.INetworkProvider;
import ninjabrain.logisticbots.api.network.IStorable;
import ninjabrain.logisticbots.api.network.ITransporter;
import ninjabrain.logisticbots.api.network.ITransporterStorage;
import ninjabrain.logisticbots.api.network.NetworkManager;
import ninjabrain.logisticbots.network.Network;

/**
 * Roboport tile entity that contains a Logistic Network
 */
public class TileRoboport extends TileEntity implements INetworkProvider, ITransporterStorage {
	
	INetwork network;
	
	@Override
	public void onLoad() {
		// TODO instead of checking world.isRemote all the time make client and server
		// specific networks
		if (!world.isRemote) {
			network = NetworkManager.addNetworkProvider(this);
		}
	}
	
	@Override
	public void onChunkUnload() {
		// TODO should it really be removed on unload? If not, dont add it on load
		onRemove();
	}
	
	public void onRemove() {
		if (!world.isRemote)
			NetworkManager.removeNetworkProvider(this);
	}

	@Override
	public INetwork getNetwork() {
		return network;
	}
	
	@Override
	public void setNetwork(INetwork network) {
		this.network = network;
	}

	@Override
	public INetwork createNewNetwork() {
		return new Network(world);
	}

	@Override
	public void insert(ITransporter<? extends IStorable> transp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasSpace(ITransporter<? extends IStorable> transp) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T extends IStorable> ITransporter<T> extract(Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
