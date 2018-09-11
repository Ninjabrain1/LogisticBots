package ninjabrain.logisticbots.api.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

/**
 * Used to store {@link ITransporter}s
 */
public class TransporterCollection extends WorldSavedData {
	
	/**
	 * A Map of all unassigned ITransporters in the world this is attached to
	 */
	public final Map<Class<? extends IStorable>, TransporterList<? extends IStorable>> transporterMap;
	
	TransporterCollection(String name) {
		super(name);
		transporterMap = new HashMap<Class<? extends IStorable>, TransporterList<? extends IStorable>>();
		for (Class<? extends IStorable> clazz : NetworkManager.storableTypes) {
			transporterMap.put(clazz, new TransporterList<>());
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IStorable> TransporterList<T> getListFromType(Class<T> type) {
		return (TransporterList<T>) transporterMap.get(type);
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
class TransporterList<T extends IStorable> extends ArrayList<ITransporter<T>> {

	private static final long serialVersionUID = -2010520254269983260L;
	
}