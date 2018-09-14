package ninjabrain.logisticbots.network;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ninjabrain.logisticbots.api.network.ISubNetwork;
import ninjabrain.logisticbots.api.network.INetwork;
import ninjabrain.logisticbots.api.network.INetworkProvider;
import ninjabrain.logisticbots.api.network.IStorable;
import ninjabrain.logisticbots.api.network.ITransporterStorage;
import ninjabrain.logisticbots.api.network.NetworkManager;

public class Network implements INetwork {
	
	protected final World world;
	protected Map<Class<? extends IStorable>, ISubNetwork<?>> subNetworks;
	
	public Network(World world) {
		this.world = world;
		subNetworks = new HashMap<Class<? extends IStorable>, ISubNetwork<?>>();
		
		NetworkManager.addNetworkToWorld(this, world);

	}
	
	@Override
	public void onUpdate() {
		// TODO OK to use lambda?
		subNetworks.values().forEach(subNet -> subNet.onUpdate());
	}
	
	@Override
	public boolean contains(BlockPos pos) {
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IStorable> ISubNetwork<T> getSubNetwork(Class<T> type) {
		return (ISubNetwork<T>) subNetworks.computeIfAbsent(type, k -> NetworkManager.networkFromType.get(k).apply(this));
	}
	
	@Override
	public boolean canMerge(INetwork network) {
		// TODO
		return false;
	}
	
	@Override
	public void merge(INetwork network) {
		// TODO
	}
	
	@Override
	public void removeProvider(INetworkProvider provider) {
		// TODO
		NetworkManager.removeNetworkfromoWorld(this, world);
		
		// TODO OK to use lambda?
		subNetworks.values().forEach(subNet -> subNet.onProviderRemoved(provider));
	}
	
	@Override
	public boolean canAddTransporterStorage(ITransporterStorage storage) {
		return contains(storage.getPos());
	}
	
	@Override
	public void addTransporterStorage(ITransporterStorage storage) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void removeTransporterStorage(ITransporterStorage storage) {
		// TODO Auto-generated method stub
		
	}
	
}
