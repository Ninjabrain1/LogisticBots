package ninjabrain.logisticbots.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ninjabrain.logisticbots.api.network.INetwork;
import ninjabrain.logisticbots.api.network.INetworkProvider;
import ninjabrain.logisticbots.api.network.IStorable;
import ninjabrain.logisticbots.api.network.ISubNetwork;
import ninjabrain.logisticbots.api.network.ITransporter;
import ninjabrain.logisticbots.api.network.ITransporterStorage;
import ninjabrain.logisticbots.api.network.NetworkManager;

public class Network implements INetwork {
	
	protected final World world;
	protected Map<Class<? extends IStorable>, ISubNetwork<?>> subNetworks;
	
	protected List<ITransporterStorage> transpStorages;
	protected List<INetworkProvider> providers;
	
	public Network(World world) {
		this.world = world;
		this.subNetworks = new HashMap<Class<? extends IStorable>, ISubNetwork<?>>();
		
		this.transpStorages = new ArrayList<ITransporterStorage>();
		this.providers = new ArrayList<INetworkProvider>();
		
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
		return true;
	}
	
	@Override
	public void merge(INetwork network) {
		// TODO
		providers.addAll(network.getProviders());
		transpStorages.addAll(network.getTransporterStorages());
		
		NetworkManager.removeNetworkfromWorld(network, world);
	}
	
	@Override
	public void removeProvider(INetworkProvider provider) {
		// TODO
		providers.remove(provider);
		if (providers.size() == 0) {
			NetworkManager.removeNetworkfromWorld(this, world);
			
			// TODO OK to use lambda?
			subNetworks.values().forEach(subNet -> subNet.onProviderRemoved(provider));
		}
	}
	
	@Override
	public boolean canAddTransporterStorage(ITransporterStorage storage) {
		return contains(storage.getPos());
	}
	
	@Override
	public void addTransporterStorage(ITransporterStorage storage) {
		transpStorages.add(storage);
	}
	
	@Override
	public void removeTransporterStorage(ITransporterStorage storage) {
		transpStorages.remove(storage);
	}
	
	@Override
	public ITransporterStorage getBestTransporterStorage(ITransporter<?> transporter) {
		// TODO optimize, preferably constant/log time
		BlockPos transpPos = transporter.getPos();
		double minDist2 = Double.MAX_VALUE;
		ITransporterStorage bestStorage = null;
		for (ITransporterStorage storage : transpStorages) {
			if (storage.hasSpace(transporter)) {
				double dist2 = transpPos.distanceSq(storage.getPos());
				if (dist2 < minDist2) {
					minDist2 = dist2;
					bestStorage = storage;
				}
			}
		}
		return bestStorage;
	}
	
	@Override
	public List<INetworkProvider> getProviders() {
		return providers;
	}
	
	@Override
	public List<ITransporterStorage> getTransporterStorages() {
		return transpStorages;
	}
	
}
