package ninjabrain.logisticbots.network;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Used to store all Logistic Networks in a world
 */
@Mod.EventBusSubscriber
public class NetworkCollection extends WorldSavedData {
	
	private static final String DATA_IDENTIFIER = "LB_networks";
	
	/**
	 * Attaches a NetworkCollection to every server world that loads
	 */
	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		World world = event.getWorld();
		if (!world.isRemote) {
			MapStorage worldStorage = world.getPerWorldStorage();
			String identifier = fileNameForProvider(world.provider);
			worldStorage.setData(identifier, new NetworkCollection(identifier));
		}
	}
	
	/**
	 * Returns the NetworkCollection that is attached to the given world
	 */
	public static NetworkCollection getNetworkCollection(World world) {
		String identifier = fileNameForProvider(world.provider);
		return (NetworkCollection) world.getPerWorldStorage().getOrLoadData(NetworkCollection.class, identifier);
	}
	
	private static String fileNameForProvider(WorldProvider provider) {
		return DATA_IDENTIFIER + provider.getDimensionType().getSuffix();
	}
	
	// ************************************************************* //
	// ************************************************************* //
	// ************************************************************* //
	
	/**
	 * A list of all Logistic Networks in the world this is attached to
	 */
	public final List<Network> networkList = new ArrayList<Network>();
	
	private NetworkCollection(String name) {
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
