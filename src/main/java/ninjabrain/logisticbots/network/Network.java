package ninjabrain.logisticbots.network;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ninjabrain.logisticbots.LogisticBots;
import ninjabrain.logisticbots.api.network.INetwork;
import ninjabrain.logisticbots.api.network.INetworkStorage;
import ninjabrain.logisticbots.api.network.IStorable;
import ninjabrain.logisticbots.api.network.NetworkManager;
import ninjabrain.logisticbots.entity.EntityLogisticRobot;

/**
 * A Logistics Network. Handles interactions between different types of
 * logistics chests and robots.
 */
public class Network implements INetwork {
	
	protected final World world;
	
	protected final ArrayList<EntityLogisticRobot> robots;
	
	protected final ArrayList<Task> tasks;
	
	/**
	 * Each entry in this List is a List of storages that all have the same
	 * priority. The List is sorted in ascending order with respect to
	 * ListWithPriosity.priority. It is assumed that the number of distinct priority
	 * values is low (only 0-2 is used by default) for a lot of methods in this
	 * class to be efficient.
	 **/
	protected final List<ListWithPriority<INetworkStorage<? extends IStorable>>> storages;
	
	/**
	 * Create a new empty Logistic Network
	 */
	public Network(World world) {
		this.world = world;
		robots = new ArrayList<EntityLogisticRobot>();
		tasks = new ArrayList<Task>();
		
		storages = new ArrayList<ListWithPriority<INetworkStorage<?>>>();
		
		world.addEventListener(new NetworkWorldEventListener(this));
	}
	
	@Override
	public void load() {
		NetworkManager.addNetworkToWorld(this, world);
	}
	
	@Override
	public void unload() {
		NetworkManager.removeNetworkfromoWorld(this, world);
	}
	
	@Override
	public void onUpdate() {
		
//		System.out.println(storages.get(0).size());
		
//		getStoragesFromPriority(-20, true);
//		getStoragesFromPriority(3, true);
//		getStoragesFromPriority(100, true);
//		getStoragesFromPriority(3, true);
//		getStoragesFromPriority(1, true);
//		getStoragesFromPriority(2, true);
//		getStoragesFromPriority(-1, true);
		
		String s = "";
		for (ListWithPriority<INetworkStorage<? extends IStorable>> storageList : storages) {
			s += " " + storageList.priority;
		}
		System.out.println(((ListWithPriority)getStoragesFromPriority(-3, false)).priority);
		
		// tasks.removeIf(task -> task.update());
		
		// if (tasks.size() == 0 && robots.size() > 0 && storages.size() > 0 &&
		// activeProviders.size() > 0) {
		// tasks.add(new Task(activeProviders.get(0), storages.get(0), robots.get(0)));
		// }
		
		// LogisticBots.logger.info("Network:" + networks.indexOf(this) + ", Robots:" +
		// robots.size() + ", storages:"
		// + storages.size() + ", active roviders:" + activeProviders.size());
	}
	
	@Override
	public boolean contains(BlockPos pos) {
		return true;
	}
	
	@Override
	public boolean canAddStorage(INetworkStorage<? extends IStorable> storage) {
		return true;
	}
	
	@Override
	public void addStorage(INetworkStorage<? extends IStorable> storage, boolean openInput, boolean openOutput,
			int priority) {
		getStoragesFromPriority(priority, true).add(storage);
	}
	
	@Override
	public void removeStorage(INetworkStorage<? extends IStorable> storage) {
		for (ListWithPriority<INetworkStorage<? extends IStorable>> storageList : storages) {
			if (storageList.remove(storage))
				return;
		}
	}
	
	/**
	 * Gets the list of storages of the given priority
	 * 
	 * @param createIfMissing
	 * If there is no storage list with the given priority this method will return:
	 * null if createIfMissing is false, a new list if createIfMissing is true.
	 */
	protected List<INetworkStorage<? extends IStorable>> getStoragesFromPriority(int priority,
			boolean createIfMissing) {
		// Linear search. Could use binary search but seems like overkill as long as the
		// number of priorities is low
		int i;
		for (i = 0; i < storages.size(); i++) {
			int listPriority = storages.get(i).priority;
			if (listPriority == priority) {
				return storages.get(i);
			} else if (listPriority > priority) {
				break;
			}
		}
		if (createIfMissing) {
			ListWithPriority<INetworkStorage<? extends IStorable>> newList = new ListWithPriority<INetworkStorage<? extends IStorable>>(
					priority);
			storages.add(i, newList);
			return newList;
		}
		return null;
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

class ListWithPriority<E> extends ArrayList<E> {
	
	private static final long serialVersionUID = -5709722441201341712L;
	
	public final int priority;
	
	public ListWithPriority(int priority) {
		this.priority = priority;
	}
	
}