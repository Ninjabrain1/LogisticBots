package ninjabrain.logisticbots.api.network;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

/**
 * Used to store all {@link INetwork}s in a world
 */
public class NetworkCollection extends WorldSavedData {
	
	/**
	 * A list of all Logistic Networks in the world this is attached to
	 */
	public final Collection<INetwork> networkList = new ArrayList<INetwork>();
	
	NetworkCollection(String name) {
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
