package ninjabrain.logisticbots.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import ninjabrain.logisticbots.api.network.ISubNetwork;
import ninjabrain.logisticbots.api.network.INetwork;
import ninjabrain.logisticbots.api.network.INetworkProvider;
import ninjabrain.logisticbots.api.network.INetworkStorage;
import ninjabrain.logisticbots.api.network.ITask;
import ninjabrain.logisticbots.api.network.ITransporter;
import ninjabrain.logisticbots.api.network.ITransporterStorage;
import ninjabrain.logisticbots.api.network.NetworkManager;

/**
 * A Logistics Network. Handles interactions between different types of
 * logistics chests and robots.
 */
public class ItemSubNetwork implements ISubNetwork<LBItemStack> {
	
	protected INetwork superNet;
	
	protected final ArrayList<ITransporter<LBItemStack>> robots, robotsToAdd, robotsToRemove;
	
	/**
	 * Each entry in this List is a List of storages that all have the same
	 * priority. The List is sorted in ascending order with respect to
	 * ListWithPriority.priority. It is assumed that the number of distinct priority
	 * values is low (only -1 to 1 is used by default) for a lot of methods in this
	 * class to be efficient.
	 **/
	protected final List<ListWithPriority<INetworkStorage<LBItemStack>>> openInputStorages, openOutputStorages;
	
	/**
	 * A list of all storages in this network. Should contain all storages
	 * regardless of priority, openInput and openOutput.
	 */
	protected final List<INetworkStorage<LBItemStack>> allStorages;
	
	/**
	 * A list of all transporter storages in this network.
	 */
	protected final List<ITransporterStorage> transpStorages;
	
	/**
	 * List of storages that want certain items that they dont have and want to get
	 * rid of items they do have.
	 */
	protected final Map<Object, Pair<INetworkStorage<LBItemStack>, LBItemStack>> wanted, unwanted;
	
	/**
	 * Create a new empty Logistic Network
	 */
	public ItemSubNetwork(INetwork superNet) {
		this.superNet = superNet;
		
		robots = new ArrayList<ITransporter<LBItemStack>>();
		robotsToAdd = new ArrayList<ITransporter<LBItemStack>>();
		robotsToRemove = new ArrayList<ITransporter<LBItemStack>>();
		
		openInputStorages = new ArrayList<ListWithPriority<INetworkStorage<LBItemStack>>>();
		openOutputStorages = new ArrayList<ListWithPriority<INetworkStorage<LBItemStack>>>();
		allStorages = new ArrayList<INetworkStorage<LBItemStack>>();
		
		transpStorages = new ArrayList<ITransporterStorage>();
		
		wanted = new HashMap<Object, Pair<INetworkStorage<LBItemStack>, LBItemStack>>();
		unwanted = new HashMap<Object, Pair<INetworkStorage<LBItemStack>, LBItemStack>>();
	}
	
	@Override
	public void onUpdate() {
		// TODO use lambda or a traditional for loop?
		robots.forEach(robot -> robot.updateTask());
		// for (ITransporter<LBItemStack> robot : robots) {
		// robot.updateTask();
		// }
		robots.forEach(robot -> recallIfFree(robot));
		
		for (ITransporter<LBItemStack> toRemove : robotsToRemove) {
			robots.remove(toRemove);
		}
		robotsToRemove.clear();
		for (ITransporter<LBItemStack> toAdd : robotsToAdd) {
			robots.add(toAdd);
		}
		robotsToAdd.clear();
		
		updateLBItems();
		
		// System.out.println("Robots: " + robots.size() + ", Storages: " +
		// allStorages.size());
	}
	
	private void updateLBItems() {
		for (ITransporter<LBItemStack> robot : robots) {
			List<INetworkStorage<LBItemStack>> providers = getStoragesFromPriority(-1, true, false);
			List<INetworkStorage<LBItemStack>> storages = getStoragesFromPriority(0, true, true);
			if (!robot.hasTask() && providers.size() > 0 && storages.size() > 0) {
				ITask<LBItemStack> pickUp = new TaskItemTransfer(providers.get(0), LBItemStack.ANY_STACK, true);
				ITask<LBItemStack> dropOff = new TaskItemTransfer(storages.get(0), LBItemStack.ANY_STACK, false);
				pickUp.setNextTask(dropOff);
				robot.setTask(pickUp);
			}
		}
	}
	
	private void recallIfFree(ITransporter<LBItemStack> robot) {
		if (!robot.hasTask()) {
			// robot.setTask(new TaskRecall(dest));
		}
	}
	
	@Override
	public void addWanted(INetworkStorage<LBItemStack> storage, LBItemStack storable) {
		
	}
	
	@Override
	public void addUnwanted(INetworkStorage<LBItemStack> storage, LBItemStack storable) {
		
	}
	
	private Object getKey(LBItemStack lbItemStack) {
		return lbItemStack.stack.getItem();
	}
	
	@Override
	public boolean canAddStorage(INetworkStorage<LBItemStack> storage) {
		return superNet.contains(storage.getPos());
	}
	
	@Override
	public void addStorage(INetworkStorage<LBItemStack> storage) {
		if (storage.getStorableType() == LBItemStack.class) {
			INetworkStorage<LBItemStack> itemStorage = (INetworkStorage<LBItemStack>) storage;
			if (storage.hasOpenInput())
				getStoragesFromPriority(storage.getPriority(), true, true).add(itemStorage);
			if (storage.hasOpenOutput())
				getStoragesFromPriority(storage.getPriority(), true, false).add(itemStorage);
			allStorages.add(itemStorage);
		}
	}
	
	@Override
	public void removeStorage(INetworkStorage<LBItemStack> storage) {
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
	public boolean canAddTransporter(ITransporter<LBItemStack> transporter) {
		// TODO does this need to be offset by 0.5 block?
		return superNet.contains(transporter.getPos());
	}
	
	@Override
	public void addTransporter(ITransporter<LBItemStack> transporter) {
		if (transporter.getStorableType() == LBItemStack.class) {
			robotsToAdd.add((ITransporter<LBItemStack>) transporter);
		}
	}
	
	@Override
	public void removeTransporter(ITransporter<LBItemStack> transporter) {
		if (transporter.getStorableType() == LBItemStack.class) {
			robotsToRemove.add((ITransporter<LBItemStack>) transporter);
		}
	}
	
	@Override
	public Class<LBItemStack> getType() {
		return LBItemStack.class;
	}

	@Override
	public void onProviderRemoved(INetworkProvider provider) {
		// TODO
		for (ITransporter<LBItemStack> transporter : robots) {
			transporter.setNetwork(NetworkManager.addTransporter(transporter));
		}
		
		for (INetworkStorage<LBItemStack> storage : allStorages) {
			storage.setNetwork(NetworkManager.addNetworkStorage(storage));
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