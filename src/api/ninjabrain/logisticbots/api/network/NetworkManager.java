package ninjabrain.logisticbots.api.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
	 * Types of IStorables that Logistic Networks can store/transport. Register a
	 * type using {@link NetworkManager#registerType(Class)} during init.
	 **/
	public static final List<Class<? extends IStorable>> storableTypes = new ArrayList<Class<? extends IStorable>>();
	
	public static void registerType(Class<? extends IStorable> type) {
		storableTypes.add(type);
	}
	
	/**
	 * Attaches a {@link NetworkCollection} to every server world that loads
	 */
	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		World world = event.getWorld();
		if (!world.isRemote) {
			// Init WorldSavedData for each loaded world
			MapStorage worldStorage = world.getPerWorldStorage();
			
			String networksIdentifier = getNetworkCollectionIdentifier(world.provider);
			worldStorage.setData(networksIdentifier, new WorldSavedMap<INetwork<?>>(networksIdentifier));
			
			String transportersIdentifier = getUnassignedTransportersIdentifier(world.provider);
			worldStorage.setData(transportersIdentifier, new WorldSavedMap<ITransporter<?>>(transportersIdentifier));
			
			String storagesIdentifier = getUnassignedStoragesIdentifier(world.provider);
			worldStorage.setData(storagesIdentifier, new WorldSavedMap<INetworkStorage<?>>(storagesIdentifier));
		}
	}
	
	/**
	 * Updates every Logistic Network in each logical server world every tick
	 */
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void onWorldTick(WorldTickEvent event) {
		if (!event.world.isRemote && event.phase == Phase.END) {
			WorldSavedMap<INetwork<?>> netCol = (WorldSavedMap<INetwork<?>>) getNetworkCollection(event.world);
			Collection<ComponentList<INetwork<?>, ? extends IStorable>> networkLists = netCol.map.values();
			for (ComponentList<INetwork<?>, ? extends IStorable> networkList : networkLists) {
				// TODO Is it OK to use lambda here?
				networkList.forEach(network -> network.onUpdate());
			}
		}
	}
	
	/**
	 * Attaches a network to a world. This will cause the network to update each
	 * tick.
	 */
	public static <T extends IStorable> void addNetworkToWorld(INetwork<T> network, World world) {
		Class<T> type = network.getType();
		ComponentList<INetwork<T>, T> networkList = getNetworkList(world, type);
		if (!networkList.contains(network)) {
			networkList.add(network);
		}
		ComponentList<ITransporter<T>, T> unassTransp = getUnassignedTransporters(world, type);
		ComponentList<INetworkStorage<T>, T> unassStorag = getUnassignedStorages(world, type);
		Iterator<ITransporter<T>> transpIterator = unassTransp.iterator();
		while (transpIterator.hasNext()) {
			ITransporter<T> transporter = transpIterator.next();
			if (network.canAddTransporter(transporter)) {
				network.addTransporter(transporter);
				transporter.setNetwork(network);
				transpIterator.remove();
			}
		}
		Iterator<INetworkStorage<T>> storageIterator = unassStorag.iterator();
		while (storageIterator.hasNext()) {
			INetworkStorage<T> storage = storageIterator.next();
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
	public static void removeNetworkfromoWorld(INetwork<?> network, World world) {
		getNetworkList(world, network.getType()).remove(network);
	}
	
	/**
	 * Finds the {@link INetwork} that can add the given INetworkStorage to itself
	 * (if such a network exists).
	 * 
	 * @return The INetwork the storage was added to, null if it was not added to a
	 * network
	 */
	public static <T extends IStorable> INetwork<T> addNetworkStorage(INetworkStorage<T> storage) {
		Class<T> type = storage.getType();
		for (INetwork<T> network : NetworkManager.getNetworkList(storage.getWorld(), type)) {
			if (network.canAddStorage(storage)) {
				network.addStorage(storage);
				storage.setNetwork(network);
				return network;
			}
		}
		getUnassignedStorages(storage.getWorld(), type).add(storage);
		return null;
	}
	
	/**
	 * Removes the given storage from the world, it can no longer be interacted with
	 * by INetworks.
	 */
	public static <T extends IStorable> void removeNetworkStorage(INetworkStorage<T> storage) {
		if (storage.getNetwork() != null) {
			storage.getNetwork().removeStorage(storage);
		} else {
			getUnassignedStorages(storage.getWorld(), storage.getType()).remove(storage);
		}
	}
	
	/**
	 * Finds the {@link INetwork} that can add the given ITransporter to itself (if
	 * such a network exists).
	 * 
	 * @return The INetwork the storage was added to, null if it was not added to a
	 * network
	 */
	public static <T extends IStorable> INetwork<T> addTransporter(ITransporter<T> transporter) {
		Class<T> type = transporter.getType();
		for (INetwork<T> network : NetworkManager.getNetworkList(transporter.getWorld(), type)) {
			if (network.canAddTransporter(transporter)) {
				network.addTransporter(transporter);
				transporter.setNetwork(network);
				return network;
			}
		}
		getUnassignedTransporters(transporter.getWorld(), type).add(transporter);
		return null;
	}
	
	/**
	 * Removes the given storage from the world, it can no longer be interacted with
	 * by INetworks.
	 */
	public static <T extends IStorable> void removeTransporter(ITransporter<T> transporter) {
		if (transporter.getNetwork() != null) {
			transporter.getNetwork().removeTransporter(transporter);
		} else {
			getUnassignedTransporters(transporter.getWorld(), transporter.getType()).remove(transporter);
		}
	}
	
	/**
	 * Creates a new {@link INetwork} according to provider.createNewNetwork(). If
	 * the new network can merge with existing networks in its world they will
	 * merge.
	 * 
	 * @return The INetwork the provider became a part of
	 */
	public static <T extends IStorable> INetwork<T> addNetworkProvider(INetworkProvider<T> provider) {
		INetwork<T> newNetwork = provider.createNewNetwork();
		ComponentList<INetwork<T>, T> networkList = getNetworkList(provider.getWorld(), provider.getType());
		for (INetwork<T> network : networkList) {
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
	public static <T extends IStorable> void removeNetworkProvider(INetworkProvider<T> provider) {
		// No need to check if getNetwork() returns null, it should never return null
		provider.getNetwork().removeProvider(provider);
	}
	
	/**
	 * Returns the {@link NetworkCollection} that is attached to the given world
	 */
	public static WorldSavedMap<?> getNetworkCollection(World world) {
		String identifier = getNetworkCollectionIdentifier(world.provider);
		return (WorldSavedMap<?>) world.getPerWorldStorage().getOrLoadData(WorldSavedMap.class, identifier);
	}
	
	/**
	 * Returns the list of networks of the given type that is attached to the given
	 * world
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IStorable> ComponentList<INetwork<T>, T> getNetworkList(World world, Class<T> type) {
		return ((WorldSavedMap<INetwork<T>>) getNetworkCollection(world)).getListFromType(type);
	}
	
	/**
	 * Returns the list of unassigned transporters of the given type in the given
	 * world
	 */
//	public static <T extends IStorable> TransporterList<T> getUnassignedTransporters(World world, Class<T> type) {
//		String identifier = getUnassignedTransportersIdentifier(world.provider);
//		return ((TransporterCollection) world.getPerWorldStorage().getOrLoadData(TransporterCollection.class,
//				identifier)).getListFromType(type);
//	}
	@SuppressWarnings("unchecked")
	public static <T extends IStorable> ComponentList<ITransporter<T>, T> getUnassignedTransporters(World world, Class<T> type) {
		String identifier = getUnassignedTransportersIdentifier(world.provider);
		return ((WorldSavedMap<ITransporter<T>>) world.getPerWorldStorage().getOrLoadData(WorldSavedMap.class, identifier)).getListFromType(type);
	}
	
	/**
	 * Returns the list of unassigned storages of the given type in the given world
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IStorable> ComponentList<INetworkStorage<T>, T> getUnassignedStorages(World world, Class<T> type) {
		String identifier = getUnassignedStoragesIdentifier(world.provider);
		return ((WorldSavedMap<INetworkStorage<T>>) world.getPerWorldStorage().getOrLoadData(WorldSavedMap.class, identifier)).getListFromType(type);
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
