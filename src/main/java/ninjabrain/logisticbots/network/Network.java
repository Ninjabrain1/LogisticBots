package ninjabrain.logisticbots.network;

import java.util.ArrayList;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import ninjabrain.logisticbots.LogisticBots;
import ninjabrain.logisticbots.entity.EntityLogisticRobot;
import ninjabrain.logisticbots.tile.TileActiveProviderChest;
import ninjabrain.logisticbots.tile.TileSimpleInventory;
import ninjabrain.logisticbots.tile.TileStorageChest;

/**
 * A Logistics Network. Handles interactions between different types of
 * logistics chests and robots. A lot of this class is temporary.
 */
@Mod.EventBusSubscriber
public class Network {
	
	// TODO dont just have one global network, also save the network to the world
	public static Network instance;
	
	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		if (!event.getWorld().isRemote) {
			if (instance == null) {
				instance = new Network(event.getWorld());
			} else {
				LogisticBots.logger
						.warn("Warning in LogisticBots Network when loading world, a world has already been loaded");
			}
		}
	}
	
	@SubscribeEvent
	public static void onWorldTickEvent(WorldTickEvent event) {
		if (event.phase == Phase.START && !event.world.isRemote)
			instance.onUpdate();
	}
	
	/**
	 * Called whenever a TileSimpleInventory is created in the server thread
	 */
	public static void onTileCreated(TileSimpleInventory tile) {
		if (tile instanceof TileStorageChest) {
			instance.storages.add((TileStorageChest) tile);
		} else if (tile instanceof TileActiveProviderChest) {
			instance.activeProviders.add((TileActiveProviderChest) tile);
		}
	}
	
	/**
	 * Called whenever a TileSimpleInventory is removed in the server thread
	 */
	public static void onTileRemoved(TileSimpleInventory tile) {
		if (tile instanceof TileStorageChest) {
			if (!instance.storages.remove(tile))
				LogisticBots.logger
						.error("TileEntity " + tile + " was missing from the network and could not be removed.");
		} else if (tile instanceof TileActiveProviderChest) {
			if (!instance.activeProviders.remove(tile))
				LogisticBots.logger
						.error("TileEntity " + tile + " was missing from the network and could not be removed.");
		}
	}
	
	// ************************************************************* //
	// ************************************************************* //
	// ************************************************************* //
	
	private ArrayList<EntityLogisticRobot> robots;
	
	private ArrayList<TileActiveProviderChest> activeProviders;
	private ArrayList<TileStorageChest> storages;
	
	/**
	 * Create a new empty Logistic Network
	 */
	public Network(World world) {
		robots = new ArrayList<EntityLogisticRobot>();
		activeProviders = new ArrayList<TileActiveProviderChest>();
		storages = new ArrayList<TileStorageChest>();
		
		world.addEventListener(new NetworkWorldEventListener(this));
	}
	
	/**
	 * Called every tick in the server thread
	 */
	public void onUpdate() {
		
		
		
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
	
}
