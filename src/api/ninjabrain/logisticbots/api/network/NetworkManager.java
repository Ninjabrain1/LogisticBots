package ninjabrain.logisticbots.api.network;

import java.util.Collection;

import net.minecraft.util.math.BlockPos;
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
	
	private static final String NETWORK_COLLECTION_DATA_IDENTIFIER = "LB_networks";
	
	/**
	 * Attaches a {@link NetworkCollection} to every server world that loads
	 */
	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		World world = event.getWorld();
		if (!world.isRemote) {
			MapStorage worldStorage = world.getPerWorldStorage();
			String identifier = fileNameForProvider(world.provider);
			worldStorage.setData(identifier, new NetworkCollection(identifier));
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
	 * @param storage
	 * The storage that should be added
	 * @param openInput
	 * Whether the INetwork can insert items to the storage unconditionally
	 * @param openOutput
	 * Whether the INetwork can extract items from the storage unconditionally
	 * @param priority
	 * The storage's priority. The INetwork wants to extract from storages with low
	 * priority and insert to storages with high priority.
	 * @return The INetwork the storage was added to, null if it was not added to a
	 * network
	 */
	public INetwork addNetworkStorage(INetworkStorage<? extends IStorable> storage, boolean openInput, boolean openOutput, int priority) {
		BlockPos pos = storage.getPos();
		for (INetwork network : NetworkManager.getNetworkCollection(storage.getWorld()).networkList) {
			if (network.contains(pos) && network.canAddStorage(storage)) {
				network.addStorage(storage, openInput, openOutput, priority);
				return network;
			}
		}
		return null;
	}
	
	/**
	 * Removes the given storage from its {@link INetwork} if it has one.
	 * 
	 * @param storage
	 * The storage that should be removed
	 */
	public void removeNetworkStorage(INetworkStorage<? extends IStorable> storage) {
		storage.getNetwork().removeStorage(storage);
	}
	
	/**
	 * Returns the {@link NetworkCollection} that is attached to the given world
	 */
	public static NetworkCollection getNetworkCollection(World world) {
		String identifier = fileNameForProvider(world.provider);
		return (NetworkCollection) world.getPerWorldStorage().getOrLoadData(NetworkCollection.class, identifier);
	}
	
	private static String fileNameForProvider(WorldProvider provider) {
		return NETWORK_COLLECTION_DATA_IDENTIFIER + provider.getDimensionType().getSuffix();
	}
	
}
