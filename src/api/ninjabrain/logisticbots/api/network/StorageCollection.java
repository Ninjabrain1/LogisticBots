package ninjabrain.logisticbots.api.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

/**
 * Used to store {@link INetworkStorage}s
 */
public class StorageCollection extends WorldSavedData {
	
	/**
	 * A Map of all unassigned INetworkStorages in the world this is attached to
	 */
	public final Map<Class<? extends IStorable>, StorageList<? extends IStorable>> storageMap;
	
	StorageCollection(String name) {
		super(name);
		storageMap = new HashMap<Class<? extends IStorable>, StorageList<? extends IStorable>>();
		for (Class<? extends IStorable> clazz : NetworkManager.storableTypes) {
			storageMap.put(clazz, new StorageList<>());
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IStorable> StorageList<T> getListFromType(Class<T> type) {
		return (StorageList<T>) storageMap.get(type);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		// Nothing needs saving yet
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		// Nothing needs saving yet
		return null;
	}
	
}

class StorageList<T extends IStorable> extends ArrayList<INetworkStorage<T>> {

	private static final long serialVersionUID = -1010520254269983260L;
	
}