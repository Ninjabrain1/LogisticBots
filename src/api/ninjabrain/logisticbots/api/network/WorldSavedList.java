package ninjabrain.logisticbots.api.network;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

public class WorldSavedList<C> extends WorldSavedData {
	
	/**
	 * A List of C's
	 */
	public final List<C> list;
	
	WorldSavedList(String name) {
		super(name);
		list = new ArrayList<C>();
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