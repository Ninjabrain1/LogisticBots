package ninjabrain.logisticsbots.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import ninjabrain.logisticsbots.LogisticsBots;
import ninjabrain.logisticsbots.item.ItemBase;
import ninjabrain.logisticsbots.item.ModItems;

@Mod.EventBusSubscriber(modid = LogisticsBots.MODID)
public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
//		MinecraftForge.EVENT_BUS.register(new EventHandler());
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
