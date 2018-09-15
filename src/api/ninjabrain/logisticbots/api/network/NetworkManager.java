package ninjabrain.logisticbots.api.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
	private static final String UNASSIGNED_TRANSPORTERS_DATA_IDENTIFIER = "LB_freeTransporters";
	// All INetworkStorages that have not been assigned to a network
	private static final String UNASSIGNED_STORAGES_DATA_IDENTIFIER = "LB_freeStorages";
	// All INetworkStorages that have not been assigned to a network
	private static final String UNASSIGNED_TRANSPORTER_STORAGES_DATA_IDENTIFIER = "LB_freeTranspStorages";
	
	/**
	 * Types of IStorables that Logistic Networks can store/transport. Register a
	 * type using {@link NetworkManager#registerType(Class)} during init.
	 **/
	public static final List<Class<? extends IStorable>> storableTypes = new ArrayList<Class<? extends IStorable>>();
	/**
	 * Maps IStorable types to new ISubNetworks that can store/transport the given
	 * type.
	 */
	public static final Map<Class<? extends IStorable>, Function<INetwork, ? extends ISubNetwork<?>>> networkFromType = new HashMap<Class<? extends IStorable>, Function<INetwork, ? extends ISubNetwork<?>>>();
	
	public static <T extends IStorable> void registerType(Class<T> type,
			Function<INetwork, ? extends ISubNetwork<T>> supplier) {
		storableTypes.add(type);
		networkFromType.put(type, supplier);
	}
	
	/**
	 * Attaches {@link WorldSavedMap}s to every server world that loads
	 */
	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		World world = event.getWorld();
		if (!world.isRemote) {
			// Init WorldSavedData for each loaded world
			MapStorage worldStorage = world.getPerWorldStorage();
			
			String networksIdentifier = getNetworkCollectionIdentifier(world.provider);
			worldStorage.setData(networksIdentifier, new WorldSavedList<INetwork>(networksIdentifier));
			
			String transporterStoragesIdentifier = getUnassignedTransporterStoragesIdentifier(world.provider);
			worldStorage.setData(transporterStoragesIdentifier,
					new WorldSavedList<ITransporterStorage>(transporterStoragesIdentifier));
			
			String transportersIdentifier = getUnassignedTransportersIdentifier(world.provider);
			worldStorage.setData(transportersIdentifier, new WorldSavedMap<ITransporter<?>>(transportersIdentifier));
			
			String storagesIdentifier = getUnassignedStoragesIdentifier(world.provider);
			worldStorage.setData(storagesIdentifier, new WorldSavedMap<INetworkStorage<?>>(storagesIdentifier));
		}
	}
	
	/**
	 * Updates every Logistic Network in each logical server world every tick
	 */
	@SubscribeEvent
	public static void onWorldTick(WorldTickEvent event) {
		if (!event.world.isRemote && event.phase == Phase.END) {
			getNetworkList(event.world).forEach(network -> network.onUpdate());
		}
	}
	
	/**
	 * Attaches a network to a world. This will add any free network components
	 * within range to the network. This will also cause the network to update each
	 * tick.
	 */
	public static void addNetworkToWorld(INetwork network, World world) {
		List<INetwork> networkList = getNetworkList(world);
		if (!networkList.contains(network)) {
			networkList.add(network);
		}
		for (Class<? extends IStorable> type : storableTypes) {
			addSubNetworkToWorld(network.getSubNetwork(type), world);
		}
		List<ITransporterStorage> unassTranspStorag = getUnassignedTransporterStorages(world);
		Iterator<ITransporterStorage> transpStorageIterator = unassTranspStorag.iterator();
		while (transpStorageIterator.hasNext()) {
			ITransporterStorage storage = transpStorageIterator.next();
			if (network.canAddTransporterStorage(storage)) {
				network.addTransporterStorage(storage);
				storage.setNetwork(network);
				transpStorageIterator.remove();
			}
		}
	}
	
	private static <T extends IStorable> void addSubNetworkToWorld(ISubNetwork<T> subNet, World world) {
		Class<T> type = subNet.getType();
		ComponentList<ITransporter<T>, T> unassTransp = getUnassignedTransporters(world, type);
		Iterator<ITransporter<T>> transpIterator = unassTransp.iterator();
		while (transpIterator.hasNext()) {
			ITransporter<T> transporter = transpIterator.next();
			if (subNet.canAddTransporter(transporter)) {
				subNet.addTransporter(transporter);
				transporter.setNetwork(subNet);
				transpIterator.remove();
			}
		}
		ComponentList<INetworkStorage<T>, T> unassStorag = getUnassignedStorages(world, type);
		Iterator<INetworkStorage<T>> storageIterator = unassStorag.iterator();
		while (storageIterator.hasNext()) {
			INetworkStorage<T> storage = storageIterator.next();
			if (subNet.canAddStorage(storage)) {
				subNet.addStorage(storage);
				storage.setNetwork(subNet);
				storageIterator.remove();
			}
		}
	}
	
	/**
	 * Removes a network from a world.
	 */
	public static void removeNetworkfromWorld(INetwork network, World world) {
		getNetworkList(world).remove(network);
	}
	
	/**
	 * Finds the {@link ISubNetwork} that can add the given INetworkStorage to
	 * itself (if such a network exists).
	 * 
	 * @return The ISubNetwork the storage was added to, null if it was not added to
	 * a network
	 */
	public static <T extends IStorable> ISubNetwork<T> addNetworkStorage(INetworkStorage<T> storage) {
		Class<T> type = storage.getType();
		for (INetwork network : NetworkManager.getNetworkList(storage.getWorld())) {
			ISubNetwork<T> subNet = network.getSubNetwork(type);
			if (subNet.canAddStorage(storage)) {
				subNet.addStorage(storage);
				storage.setNetwork(subNet);
				return subNet;
			}
		}
		getUnassignedStorages(storage.getWorld(), type).add(storage);
		return null;
	}
	
	/**
	 * Removes the given storage from the world, it can no longer be interacted with
	 * by ISubNetworks.
	 */
	public static <T extends IStorable> void removeNetworkStorage(INetworkStorage<T> storage) {
		if (storage.getNetwork() != null) {
			storage.getNetwork().removeStorage(storage);
		} else {
			getUnassignedStorages(storage.getWorld(), storage.getType()).remove(storage);
		}
	}
	
	/**
	 * Finds the {@link ISubNetwork} that can add the given ITransporterStorage to
	 * itself (if such a network exists).
	 * 
	 * @return The INetwork the storage was added to, null if it was not added to a
	 * network
	 */
	public static INetwork addTransporterStorage(ITransporterStorage storage) {
		for (INetwork network : NetworkManager.getNetworkList(storage.getWorld())) {
			if (network.canAddTransporterStorage(storage)) {
				network.addTransporterStorage(storage);
				storage.setNetwork(network);
				return network;
			}
		}
		getUnassignedTransporterStorages(storage.getWorld()).add(storage);
		return null;
	}
	
	/**
	 * Removes the given transporter storage from the world, it can no longer be
	 * interacted with by INetworks.
	 */
	public static void removeTransporterStorage(ITransporterStorage storage) {
		if (storage.getNetwork() != null) {
			storage.getNetwork().removeTransporterStorage(storage);
		} else {
			getUnassignedTransporterStorages(storage.getWorld()).remove(storage);
		}
	}
	
	/**
	 * Finds the {@link ISubNetwork} that can add the given ITransporter to itself
	 * (if such a subnetwork exists).
	 * 
	 * @return The INetwork the storage was added to, null if it was not added to a
	 * network
	 */
	public static <T extends IStorable> ISubNetwork<T> addTransporter(ITransporter<T> transporter) {
		Class<T> type = transporter.getType();
		for (INetwork network : NetworkManager.getNetworkList(transporter.getWorld())) {
			ISubNetwork<T> subNet = network.getSubNetwork(type);
			if (subNet.canAddTransporter(transporter)) {
				subNet.addTransporter(transporter);
				transporter.setNetwork(subNet);
				return subNet;
			}
		}
		getUnassignedTransporters(transporter.getWorld(), type).add(transporter);
		return null;
	}
	
	/**
	 * Removes the given transporter from the world, it can no longer be interacted
	 * with by INetworks or ISubNetworks.
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
	public static INetwork addNetworkProvider(INetworkProvider provider) {
		INetwork newNetwork = provider.createNewNetwork();
		List<INetwork> networkList = getNetworkList(provider.getWorld());
		for (INetwork network : networkList) {
			// TODO the provider might be able to merge with more than one network
			if (network != newNetwork && newNetwork.canMerge(network) && network.canMerge(newNetwork)) {
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
	 * Returns the list of networks of the given type that is attached to the given
	 * world
	 */
	// @SuppressWarnings("unchecked")
	// public static <T extends IStorable> ComponentList<INetwork<T>, T>
	// getNetworkList(World world, Class<T> type) {
	// return ((WorldSavedMap<INetwork<T>>)
	// getNetworkCollection(world)).getListFromType(type);
	// }
	
	/**
	 * Returns the list of networks that is attached to the given world
	 */
	@SuppressWarnings("unchecked")
	public static List<INetwork> getNetworkList(World world) {
		String identifier = getNetworkCollectionIdentifier(world.provider);
		WorldSavedList<INetwork> networkList = (WorldSavedList<INetwork>) world.getPerWorldStorage()
				.getOrLoadData(WorldSavedList.class, identifier);
		return networkList.list;
	}
	
	/**
	 * Returns the list of unassigned transporter storages in the given world
	 */
	@SuppressWarnings("unchecked")
	public static List<ITransporterStorage> getUnassignedTransporterStorages(World world) {
		String identifier = getUnassignedTransporterStoragesIdentifier(world.provider);
		WorldSavedList<ITransporterStorage> transpStorageList = (WorldSavedList<ITransporterStorage>) world
				.getPerWorldStorage().getOrLoadData(WorldSavedList.class, identifier);
		return transpStorageList.list;
	}
	
	/**
	 * Returns the list of unassigned transporters of the given type in the given
	 * world
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IStorable> ComponentList<ITransporter<T>, T> getUnassignedTransporters(World world,
			Class<T> type) {
		String identifier = getUnassignedTransportersIdentifier(world.provider);
		return ((WorldSavedMap<ITransporter<T>>) world.getPerWorldStorage().getOrLoadData(WorldSavedMap.class,
				identifier)).getListFromType(type);
	}
	
	/**
	 * Returns the list of unassigned storages of the given type in the given world
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IStorable> ComponentList<INetworkStorage<T>, T> getUnassignedStorages(World world,
			Class<T> type) {
		String identifier = getUnassignedStoragesIdentifier(world.provider);
		return ((WorldSavedMap<INetworkStorage<T>>) world.getPerWorldStorage().getOrLoadData(WorldSavedMap.class,
				identifier)).getListFromType(type);
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
	
	private static String getUnassignedTransporterStoragesIdentifier(WorldProvider provider) {
		return UNASSIGNED_TRANSPORTER_STORAGES_DATA_IDENTIFIER + provider.getDimensionType().getSuffix();
	}
	
}
