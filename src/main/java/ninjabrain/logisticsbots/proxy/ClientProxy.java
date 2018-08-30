package ninjabrain.logisticsbots.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import ninjabrain.logisticsbots.EntityLogisticsRobot;
import ninjabrain.logisticsbots.LogisticsBots;
import ninjabrain.logisticsbots.item.ItemBase;
import ninjabrain.logisticsbots.item.ModItems;
import ninjabrain.logisticsbots.render.RenderLogisticsRobot;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = LogisticsBots.MODID)
public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		initRenderers();
	}
	
	private void initRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityLogisticsRobot.class, RenderLogisticsRobot::new);
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		for (ItemBase item : ModItems.items)
			item.registerModels();
	}
	
	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
	

}

