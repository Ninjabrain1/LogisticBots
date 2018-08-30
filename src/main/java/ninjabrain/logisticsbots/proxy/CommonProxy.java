package ninjabrain.logisticsbots.proxy;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import ninjabrain.logisticsbots.EntityLogisticsRobot;
import ninjabrain.logisticsbots.LogisticsBots;
import ninjabrain.logisticsbots.item.ItemBase;
import ninjabrain.logisticsbots.item.ModItems;

@Mod.EventBusSubscriber(modid = LogisticsBots.MODID)
public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		registerEntity("logisticsRobot", EntityLogisticsRobot.class, 64, 10, false);
	}
	
	private static int nextEntityID = 0;
	private void registerEntity(String registryName, Class<? extends Entity> entityClass, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(
				new ResourceLocation(LogisticsBots.MODID, registryName),
				entityClass,
				registryName,
				nextEntityID++,
				LogisticsBots.instance,
				trackingRange,
				updateFrequency,
				sendsVelocityUpdates);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		for (ItemBase item : ModItems.items)
			registry.register(item);
	}
	
	public void registerItemRenderer(Item item, int meta, String id) {};
	
	public void init(FMLInitializationEvent event) {
		
	}
	
}
