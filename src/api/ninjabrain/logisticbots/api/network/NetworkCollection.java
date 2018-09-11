package ninjabrain.logisticbots.api.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

/**
 * Used to store all {@link INetwork}s in a world
 */
public class NetworkCollection extends WorldSavedData {
	
	/**
	 * A list of all Logistic Networks in the world this is attached to
	 */
	public final Map<Class<? extends IStorable>, NetworkList<? extends IStorable>> networkMap;
	
	NetworkCollection(String name) {
		super(name);
		networkMap = new HashMap<Class<? extends IStorable>, NetworkList<? extends IStorable>>();
		for (Class<? extends IStorable> clazz : NetworkManager.storableTypes) {
			networkMap.put(clazz, new NetworkList<>());
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IStorable> NetworkList<T> getListFromType(Class<T> type) {
		return (NetworkList<T>) networkMap.get(type);
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
class NetworkList<T extends IStorable> extends ArrayList<INetwork<T>> {

	private static final long serialVersionUID = -3010520254269983260L;
	
}
