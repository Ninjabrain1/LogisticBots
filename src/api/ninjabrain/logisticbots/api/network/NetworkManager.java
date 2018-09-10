package ninjabrain.logisticbots.api.network;

import java.util.Collection;
import java.util.Iterator;

import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

/**
 * Main class for dealing with Logistic Networks
 */
@Mod.EventBusSubscriber
public class NetworkManager {
	
	// All networks in a world
	private static final String NETWORK_COLLECTION_DATA_IDENTIFIER = "LB_networks";
	// All ITransporters that have not been assigned to a network
	private static final String UNASSIGNED_TRANSPORTERS_DATA_IDENTIFIER = "LB_loneTransporters";
	// All INetworkStorages that have not been assigned to a network
	private static final String UNASSIGNED_STORAGES_DATA_IDENTIFIER = "LB_loneStorages";
	
	/**
	 * Attaches a {@link NetworkCollection} to every server world that loads
	 */
	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		World world = event.getWorld();
		if (!world.isRemote) {
			MapStorage worldStorage = world.getPerWorldStorage();
			String networksIdentifier = getNetworkCollectionIdentifier(world.provider);
			worldStorage.setData(networksIdentifier, new NetworkCollection(networksIdentifier));
			
			String transportersIdentifier = getUnassignedTransportersIdentifier(world.provider);
			worldStorage.setData(transportersIdentifier, new TransporterCollection(transportersIdentifier));
			
			String storagesIdentifier = getUnassignedStoragesIdentifier(world.provider);
			worldStorage.setData(storagesIdentifier, new StorageCollection(storagesIdentifier));
		}
	}
	
	/**
	 * Updates every Logistic Network in each logical server world every tick
	 */
	@SubscribeEvent
	public static void onWorldTick(WorldTickEvent event) {
		if (!event.world.isRemote && event.phase == Phase.END) {
			Collection<INetwork> networkCollection = getNetworkCollection(event.world).networkList;
			networkCollection.forEach(network -> network.onUpdate());
		}
		
	}
	
	/**
	 * Attaches a network to a world. This will cause the network to update each
	 * tick.
	 */
	public static void addNetworkToWorld(INetwork network, World world) {
		Collection<INetwork> networkCollection = getNetworkCollection(world).networkList;
		if (!networkCollection.contains(network)) {
			networkCollection.add(network);
		}
		TransporterCollection unassignedTransporters = getUnassignedTransporters(world);
		StorageCollection unassignedStorages = getUnassignedStorages(world);
		Iterator<ITransporter<? extends IStorable>> transpIterator = unassignedTransporters.transporterList.iterator();
		while (transpIterator.hasNext()) {
			ITransporter<? extends IStorable> transporter = transpIterator.next();
			if (network.canAddTransporter(transporter)) {
				network.addTransporter(transporter);
				transporter.setNetwork(network);
				transpIterator.remove();
			}
		}
		Iterator<INetworkStorage<? extends IStorable>> storageIterator = unassignedStorages.storageList.iterator();
		while (storageIterator.hasNext()) {
			INetworkStorage<? extends IStorable> storage = storageIterator.next();
			if (network.canAddStorage(storage)) {
				network.addStorage(storage);
				storage.setNetwork(network);
				storageIterator.remove();
			}
		}
	}
	
	/**
	 * Removes a network from a world.
	 */
	public static void removeNetworkfromoWorld(INetwork network, World world) {
		getNetworkCollection(world).networkList.remove(network);
	}
	
	/**
	 * Finds the {@link INetwork} that can add the given INetworkStorage to itself
	 * (if such a network exists).
	 * 
	 * @return The INetwork the storage was added to, null if it was not added to a
	 * network
	 */
	public static INetwork addNetworkStorage(INetworkStorage<? extends IStorable> storage) {
		for (INetwork network : NetworkManager.getNetworkCollection(storage.getWorld()).networkList) {
			if (network.canAddStorage(storage)) {
				network.addStorage(storage);
				storage.setNetwork(network);
				return network;
			}
		}
		getUnassignedStorages(storage.getWorld()).storageList.add(storage);
		return null;
	}
	
	/**
	 * Removes the given storage from the world, it can no longer be interacted with
	 * by INetworks.
	 */
	public static void removeNetworkStorage(INetworkStorage<? extends IStorable> storage) {
		if (storage.getNetwork() != null) {
			storage.getNetwork().removeStorage(storage);
		} else {
			getUnassignedStorages(storage.getWorld()).storageList.remove(storage);
		}
	}
	
	/**
	 * Finds the {@link INetwork} that can add the given ITransporter to itself (if
	 * such a network exists).
	 * 
	 * @return The INetwork the storage was added to, null if it was not added to a
	 * network
	 */
	public static INetwork addTransporter(ITransporter<? extends IStorable> transporter) {
		for (INetwork network : NetworkManager.getNetworkCollection(transporter.getWorld()).networkList) {
			if (network.canAddTransporter(transporter)) {
				network.addTransporter(transporter);
				transporter.setNetwork(network);
				return network;
			}
		}
		getUnassignedTransporters(transporter.getWorld()).transporterList.add(transporter);
		return null;
	}
	
	/**
	 * Removes the given storage from the world, it can no longer be interacted with
	 * by INetworks.
	 */
	public static void removeTransporter(ITransporter<? extends IStorable> transporter) {
		if (transporter.getNetwork() != null) {
			transporter.getNetwork().removeTransporter(transporter);
		} else {
			getUnassignedTransporters(transporter.getWorld()).transporterList.remove(transporter);
		}
	}
	
	/**
	 * Creates a new {@link INetwork} according to provider.createNewNetwork(). If
	 * the new network can merge with existing networks in its world they will
	 * merge.
	 * 
	 * @return The INetwork the provider became a part of
	 */
	public static INetwork addNetworkProvider(INetworkProvider provider) {
		INetwork newNetwork = provider.createNewNetwork();
		for (INetwork network : NetworkManager.getNetworkCollection(provider.getWorld()).networkList) {
			// TODO the provider might be able to merge with more than one network
			if (newNetwork.canMerge(network) && network.canMerge(newNetwork)) {
				network.merge(newNetwork);
				return network;
			}
		}
		return newNetwork;
	}
	
	/**
	 * Removes the given provider from its {@link INetwork}, possibly removing the
	 * entire network if it was the only provider left in the network, or splitting
	 * its network into two if the provider was the only link between them.
	 */
	public static void removeNetworkProvider(INetworkProvider provider) {
		// No need to check if getNetwork() returns null, it should never return null
		provider.getNetwork().removeProvider(provider);
	}
	
	/**
	 * Returns the {@link NetworkCollection} that is attached to the given world
	 */
	public static NetworkCollection getNetworkCollection(World world) {
		String identifier = getNetworkCollectionIdentifier(world.provider);
		return (NetworkCollection) world.getPerWorldStorage().getOrLoadData(NetworkCollection.class, identifier);
	}
	
	/**
	 * Returns the {@link TransporterCollection} of unassigned transporters in the
	 * given world
	 */
	public static TransporterCollection getUnassignedTransporters(World world) {
		String identifier = getUnassignedTransportersIdentifier(world.provider);
		return (TransporterCollection) world.getPerWorldStorage().getOrLoadData(TransporterCollection.class,
				identifier);
	}
	
	/**
	 * Returns the {@link StorageCollection} of unassigned storages in the given
	 * world
	 */
	public static StorageCollection getUnassignedStorages(World world) {
		String identifier = getUnassignedStoragesIdentifier(world.provider);
		return (StorageCollection) world.getPerWorldStorage().getOrLoadData(StorageCollection.class, identifier);
	}
	
	private static String getNetworkCollectionIdentifier(WorldProvider provider) {
		return NETWORK_COLLECTION_DATA_IDENTIFIER + provider.getDimensionType().getSuffix();
	}
	
	private static String getUnassignedTransportersIdentifier(WorldProvider provider) {
		return UNASSIGNED_TRANSPORTERS_DATA_IDENTIFIER + provider.getDimensionType().getSuffix();
	}
	
	private static String getUnassignedStoragesIdentifier(WorldProvider provider) {
		return UNASSIGNED_STORAGES_DATA_IDENTIFIER + provider.getDimensionType().getSuffix();
	}
	
}
