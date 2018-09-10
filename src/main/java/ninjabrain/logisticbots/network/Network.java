package ninjabrain.logisticbots.network;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ninjabrain.logisticbots.api.network.INetwork;
import ninjabrain.logisticbots.api.network.INetworkProvider;
import ninjabrain.logisticbots.api.network.INetworkStorage;
import ninjabrain.logisticbots.api.network.IStorable;
import ninjabrain.logisticbots.api.network.ITransporter;
import ninjabrain.logisticbots.api.network.NetworkManager;

/**
 * A Logistics Network. Handles interactions between different types of
 * logistics chests and robots.
 */
public class Network implements INetwork {
	
	protected final World world;
	
	protected final ArrayList<ITransporter<? extends IStorable>> robots, robotsToAdd, robotsToRemove;
	
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
		
		robots = new ArrayList<ITransporter<? extends IStorable>>();
		robotsToAdd = new ArrayList<ITransporter<? extends IStorable>>();
		robotsToRemove = new ArrayList<ITransporter<? extends IStorable>>();
		
		storages = new ArrayList<ListWithPriority<INetworkStorage<? extends IStorable>>>();
		
		NetworkManager.addNetworkToWorld(this, world);
	}
	
	@Override
	public void onUpdate() {
		// TODO use lambda or a traditional for loop?
		robots.forEach(robot -> robot.updateTask());
//		for(ITransporter<? extends IStorable> robot : robots) {
//			robot.updateTask();
//		}
		for (ITransporter<? extends IStorable> toRemove : robotsToRemove) {
			robots.remove(toRemove);
		}
		robotsToRemove.clear();
		for (ITransporter<? extends IStorable> toAdd : robotsToAdd) {
			robots.add(toAdd);
		}
		robotsToAdd.clear();
		
//		System.out.println("Robots: " + robots.size() + ", Storages: " + getNumberOfStorages());
	}
	
	public boolean contains(BlockPos pos) {
		return true;
	}
	
	@Override
	public boolean canAddStorage(INetworkStorage<? extends IStorable> storage) {
		return true;
	}
	
	@Override
	public void addStorage(INetworkStorage<? extends IStorable> storage) {
		// TODO open input / open output
		getStoragesFromPriority(storage.getPriority(), true).add(storage);
	}
	
	@Override
	public void removeStorage(INetworkStorage<? extends IStorable> storage) {
		for (ListWithPriority<INetworkStorage<? extends IStorable>> storageList : storages) {
			if (storageList.remove(storage))
				return;
		}
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
		
		for (ITransporter<? extends IStorable> transporter : robots) {
			transporter.setNetwork(NetworkManager.addTransporter(transporter));
		}
		
		List<INetworkStorage<? extends IStorable>> storagesAsList = getStoragesAsList();
		for (INetworkStorage<? extends IStorable> storage : storagesAsList) {
			storage.setNetwork(NetworkManager.addNetworkStorage(storage));
		}
		
	}
	
	protected int getNumberOfStorages() {
		int i = 0;
		for (ListWithPriority<INetworkStorage<? extends IStorable>> storageList : storages) {
			i += storageList.size();
		}
		return i;
	}
	
	protected List<INetworkStorage<? extends IStorable>> getStoragesAsList() {
		List<INetworkStorage<? extends IStorable>> ret = new ArrayList<INetworkStorage<? extends IStorable>>();
		for (ListWithPriority<INetworkStorage<? extends IStorable>> storageList : storages) {
			ret.addAll(storageList);
		}
		return ret;
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
	
	@Override
	public <T extends IStorable> boolean canAddTransporter(ITransporter<T> transporter) {
		// TODO does this need to be offset by 0.5 block?
		return contains(new BlockPos(transporter.getPos()));
	}
	
	@Override
	public <T extends IStorable> void addTransporter(ITransporter<T> transporter) {
		robotsToAdd.add(transporter);
		transporter.setTask(new TaskMovement<T>(new BlockPos(1000, 10, 0)));
	}
	
	@Override
	public void removeTransporter(ITransporter<? extends IStorable> transporter) {
		robotsToRemove.add(transporter);
	}

}

class ListWithPriority<E> extends ArrayList<E> {
	
	private static final long serialVersionUID = -5709722441201341712L;
	
	public final int priority;
	
	public ListWithPriority(int priority) {
		this.priority = priority;
	}
	
}