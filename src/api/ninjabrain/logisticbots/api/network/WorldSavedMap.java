package ninjabrain.logisticbots.api.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

public class WorldSavedMap<C> extends WorldSavedData {
	
	/**
	 * A Map of ComponentLists that store C's
	 */
	public final Map<Class<? extends IStorable>, ComponentList<C, ? extends IStorable>> map;
	
	WorldSavedMap(String name) {
		super(name);
		map = new HashMap<Class<? extends IStorable>, ComponentList<C, ? extends IStorable>>();
		for (Class<? extends IStorable> clazz : NetworkManager.storableTypes) {
			map.put(clazz, new ComponentList<>());
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IStorable> ComponentList<C, T> getListFromType(Class<T> type) {
		return (ComponentList<C, T>) map.get(type);
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

class ComponentList<C, T extends IStorable> extends ArrayList<C> {
	
	private static final long serialVersionUID = -1010520254269983260L;
	
}