package ninjabrain.logisticbots.proxy;

import net.minecraft.block.Block;
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
import ninjabrain.logisticbots.block.ModBlocks;
import ninjabrain.logisticbots.entity.EntityLogisticRobot;
import ninjabrain.logisticbots.item.ModItems;
import ninjabrain.logisticbots.lib.LibMod;
import ninjabrain.logisticbots.render.IModelRegister;
import ninjabrain.logisticbots.render.RenderLogisticRobot;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = LibMod.MODID)
public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		initRenderers();
	}
	
	private void initRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityLogisticRobot.class, RenderLogisticRobot::new);
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		for (Item item : ModItems.items) {
			if (item instanceof IModelRegister) {
				((IModelRegister)item).registerModels();
			}
		}
		for (Block block : ModBlocks.blocks) {
			if (block instanceof IModelRegister) {
				((IModelRegister)block).registerModels();
			}
		}
	}
	
	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
	

}

