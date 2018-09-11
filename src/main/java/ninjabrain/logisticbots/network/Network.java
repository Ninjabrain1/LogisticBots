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
	 * ListWithPriority.priority. It is assumed that the number of distinct priority
	 * values is low (only -1 to 1 is used by default) for a lot of methods in this
	 * class to be efficient.
	 **/
	protected final List<ListWithPriority<INetworkStorage<LBItemStack>>> openInputStorages;
	protected final List<ListWithPriority<INetworkStorage<LBItemStack>>> openOutputStorages;
	
	/**
	 * A list of all storages in this network. Should contain all storages
	 * regardless of priority, openInput and openOutput.
	 */
	protected final List<INetworkStorage<? extends IStorable>> allStorages;
	
	/**
	 * Create a new empty Logistic Network
	 */
	public Network(World world) {
		this.world = world;
		
		robots = new ArrayList<ITransporter<? extends IStorable>>();
		robotsToAdd = new ArrayList<ITransporter<? extends IStorable>>();
		robotsToRemove = new ArrayList<ITransporter<? extends IStorable>>();
		
		openInputStorages = new ArrayList<ListWithPriority<INetworkStorage<LBItemStack>>>();
		openOutputStorages = new ArrayList<ListWithPriority<INetworkStorage<LBItemStack>>>();
		allStorages = new ArrayList<INetworkStorage<? extends IStorable>>();
		
		NetworkManager.addNetworkToWorld(this, world);
	}
	
	@Override
	public void onUpdate() {
		// TODO use lambda or a traditional for loop?
		robots.forEach(robot -> robot.updateTask());
		// for(ITransporter<? extends IStorable> robot : robots) {
		// robot.updateTask();
		// }
		for (ITransporter<? extends IStorable> toRemove : robotsToRemove) {
			robots.remove(toRemove);
		}
		robotsToRemove.clear();
		for (ITransporter<? extends IStorable> toAdd : robotsToAdd) {
			robots.add(toAdd);
		}
		robotsToAdd.clear();
		
		for (ITransporter<? extends IStorable> robot : robots) {
			List<INetworkStorage<LBItemStack>> providers = getStoragesFromPriority(-1, true, false);
			List<INetworkStorage<LBItemStack>> storages = getStoragesFromPriority(0, true, true);
			if (!robot.hasTask() && providers.size() > 0 && storages.size() > 0) {
				TaskItemTransfer pickUp = new TaskItemTransfer(providers.get(0), true);
				TaskItemTransfer dropOff = new TaskItemTransfer(storages.get(0), false);
				pickUp.setNextTask(dropOff);
				((ITransporter<LBItemStack>)robot).setTask(pickUp);
			}
		}
		
		// System.out.println("Robots: " + robots.size() + ", Storages: " +
		// allStorages.size());
	}
	
	public boolean contains(BlockPos pos) {
		return true;
	}
	
	@Override
	public boolean canAddStorage(INetworkStorage<? extends IStorable> storage) {
		return contains(storage.getPos());
	}
	
	@Override
	public void addStorage(INetworkStorage<? extends IStorable> storage) {
		if (storage.hasOpenInput())
			getStoragesFromPriority(storage.getPriority(), true, true).add((INetworkStorage<LBItemStack>) storage);
		if (storage.hasOpenOutput())
			getStoragesFromPriority(storage.getPriority(), true, false).add((INetworkStorage<LBItemStack>) storage);
		allStorages.add(storage);
	}
	
	@Override
	public void removeStorage(INetworkStorage<? extends IStorable> storage) {
		for (ListWithPriority<INetworkStorage<LBItemStack>> storageList : openInputStorages) {
			if (storageList.remove(storage))
				break;
		}
		for (ListWithPriority<INetworkStorage<LBItemStack>> storageList : openOutputStorages) {
			if (storageList.remove(storage))
				break;
		}
		allStorages.remove(storage);
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
		
		for (INetworkStorage<? extends IStorable> storage : allStorages) {
			storage.setNetwork(NetworkManager.addNetworkStorage(storage));
		}
		
	}
	
	/**
	 * Gets the list of storages of the given priority and openInput=true or
	 * openOutput=true
	 * 
	 * @param createIfMissing
	 * If there is no storage list with the given priority this method will return:
	 * null if createIfMissing is false, a new list if createIfMissing is true.
	 * @param openInput
	 * If true will return a list of storages with open input, if false open output.
	 */
	protected List<INetworkStorage<LBItemStack>> getStoragesFromPriority(int priority, boolean createIfMissing,
			boolean openInput) {
		List<ListWithPriority<INetworkStorage<LBItemStack>>> storages;
		storages = openInput ? openInputStorages : openOutputStorages;
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
			ListWithPriority<INetworkStorage<LBItemStack>> newList = new ListWithPriority<INetworkStorage<LBItemStack>>(
					priority);
			storages.add(i, newList);
			return newList;
		}
		return null;
	}
	
	@Override
	public <T extends IStorable> boolean canAddTransporter(ITransporter<T> transporter) {
		// TODO does this need to be offset by 0.5 block?
		return contains(transporter.getPos());
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