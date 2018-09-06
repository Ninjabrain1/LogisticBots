package ninjabrain.logisticbots.network;

import java.util.ArrayList;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ninjabrain.logisticbots.LogisticBots;
import ninjabrain.logisticbots.entity.EntityLogisticRobot;
import ninjabrain.logisticbots.tile.TileActiveProviderChest;
import ninjabrain.logisticbots.tile.TileSimpleInventory;
import ninjabrain.logisticbots.tile.TileStorageChest;

/**
 * A Logistics Network. Handles interactions between different types of
 * logistics chests and robots. A lot of this class is temporary.
 */
public class Network {
	
	/**
	 * Called whenever a TileSimpleInventory is created in the server thread
	 * 
	 * @return the network the tile was added to, null if it was not added to a network
	 */
	public static Network onTileCreated(TileSimpleInventory tile) {
		BlockPos pos = tile.getPos();
		for (Network network : NetworkCollection.getNetworkCollection(tile.getWorld()).networkList) {
			if (network.contains(pos)) {
				network.addChest(tile);
				return network;
			}
		}
		return null;
	}
	
	/**
	 * Called whenever a TileSimpleInventory is removed in the server thread
	 */
	public static void onTileRemoved(TileSimpleInventory tile) {
		boolean removed = tile.getNetwork().removeChest(tile);
		if (!removed)
			LogisticBots.logger.error("TileEntity " + tile + " was missing from the network and could not be removed.");
	}
	
	// ************************************************************* //
	// ************************************************************* //
	// ************************************************************* //
	
	private final World world;
	
	private final ArrayList<EntityLogisticRobot> robots;
	
	private final ArrayList<TileActiveProviderChest> activeProviders;
	private final ArrayList<TileStorageChest> storages;
	
	private final ArrayList<Task> tasks;
	
	/**
	 * Create a new empty Logistic Network
	 */
	public Network(World world) {
		this.world = world;
		robots = new ArrayList<EntityLogisticRobot>();
		activeProviders = new ArrayList<TileActiveProviderChest>();
		storages = new ArrayList<TileStorageChest>();
		tasks = new ArrayList<Task>();
		
		world.addEventListener(new NetworkWorldEventListener(this));
	}
	
	/**
	 * Loads this Network to the game
	 */
	public void load() {
		NetworkCollection.getNetworkCollection(world).networkList.add(this);
	}
	
	/**
	 * Unloads this Network from the game
	 */
	public void unload() {
		NetworkCollection.getNetworkCollection(world).networkList.remove(this);
	}
	
	/**
	 * Called every tick in the server thread
	 */
	public void onUpdate() {
		
		tasks.removeIf(task -> task.update());
		
		if (tasks.size() == 0 && robots.size() > 0 && storages.size() > 0 && activeProviders.size() > 0) {
			tasks.add(new Task(activeProviders.get(0), storages.get(0), robots.get(0)));
		}
		System.out.println(NetworkCollection.getNetworkCollection(world).networkList.size());
//		LogisticBots.logger.info("Network:" + networks.indexOf(this) + ", Robots:" + robots.size() + ", storages:"
//				+ storages.size() + ", active roviders:" + activeProviders.size());
	}
	
	/**
	 * Called whenever a EntityLogisticRobot is created or loaded in the server
	 * thread
	 */
	public void onRobotAdded(EntityLogisticRobot entity) {
		robots.add(entity);
	}
	
	/**
	 * Called whenever a EntityLogisticRobot is created or loaded in the server
	 * thread
	 */
	public void onRobotRemoved(EntityLogisticRobot entity) {
		if (!robots.remove(entity)) {
			LogisticBots.logger
					.error("Could not remove entity from Logistic Network, the entity is not a part of the network.");
		}
	}
	
	/**
	 * Returns true if the given BlockPos is within this networks boundaries
	 */
	public boolean contains(BlockPos pos) {
		return true;
	}
	
	/**
	 * Adds the given chest to the network
	 */
	private void addChest(TileSimpleInventory tile) {
		// TODO can this be made more elegant?
		if (tile instanceof TileStorageChest) {
			storages.add((TileStorageChest) tile);
			return;
		}
		if (tile instanceof TileActiveProviderChest) {
			activeProviders.add((TileActiveProviderChest) tile);
			return;
		}
	}
	
	/**
	 * Removes the given chest from the network
	 * 
	 * @return true if the chest was removed
	 */
	private boolean removeChest(TileSimpleInventory tile) {
		// TODO can this be made more elegant?
		if (tile instanceof TileStorageChest) {
			return storages.remove(tile);
		}
		if (tile instanceof TileActiveProviderChest) {
			return activeProviders.remove(tile);
		}
		return false;
	}
	
}
