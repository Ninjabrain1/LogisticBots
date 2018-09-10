package ninjabrain.logisticbots.api.network;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

/**
 * Used to store {@link INetworkStorage}s
 */
public class StorageCollection extends WorldSavedData {
	
	/**
	 * A list of all Logistic Networks in the world this is attached to
	 */
	public final Collection<INetworkStorage<? extends IStorable>> storageList = new ArrayList<INetworkStorage<? extends IStorable>>();
	
	StorageCollection(String name) {
		super(name);
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
