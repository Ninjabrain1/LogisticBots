package ninjabrain.logisticbots.proxy;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import ninjabrain.logisticbots.LogisticBots;
import ninjabrain.logisticbots.block.ModBlocks;
import ninjabrain.logisticbots.entity.EntityLogisticRobot;
import ninjabrain.logisticbots.gui.ContainerMod;
import ninjabrain.logisticbots.gui.GuiSimpleChest;
import ninjabrain.logisticbots.item.ModItems;
import ninjabrain.logisticbots.lib.LibGUI;
import ninjabrain.logisticbots.lib.LibMod;
import ninjabrain.logisticbots.lib.LibNames;
import ninjabrain.logisticbots.tile.TileActiveProviderChest;
import ninjabrain.logisticbots.tile.TileSimpleInventory;
import ninjabrain.logisticbots.tile.TileStorageChest;

@Mod.EventBusSubscriber(modid = LibMod.MODID)
public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		registerEntity(LibNames.ENTITY_LOGISTICS_ROBOT, EntityLogisticRobot.class, 64, 10, false);
		
		registerTileEntity(TileStorageChest.class, LibNames.STATE_STORAGE);
		registerTileEntity(TileActiveProviderChest.class, LibNames.STATE_ACTIVE_PROVIDER);
	}
	
	private static int nextEntityID = 0;
	
	private void registerEntity(String registryName, Class<? extends Entity> entityClass, int trackingRange,
			int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(new ResourceLocation(LibMod.MODID, registryName), entityClass, registryName,
				nextEntityID++, LogisticBots.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
	}
	
	private static void registerTileEntity(Class<? extends TileEntity> tileClass, String registryName) {
		GameRegistry.registerTileEntity(tileClass, new ResourceLocation(LibMod.MODID, registryName));
	}
	
	@SubscribeEvent
	public static void registerBlocks(Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		for (Block block : ModBlocks.blocks)
			registry.register(block);
	}
	
	@SubscribeEvent
	public static void registerItems(Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		for (Item item : ModItems.items)
			registry.register(item);
	}
	
	public void registerItemRenderer(Item item, int meta, String id) {
	};
	
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(LogisticBots.instance, new IGuiHandler() {
			
			@Override
			public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
				if (ID == LibGUI.GUI_SIMPLE_INVENTORY_ID) {
					TileSimpleInventory inv = (TileSimpleInventory) world.getTileEntity(new BlockPos(x, y, z));
					return new ContainerMod(player.inventory, inv.getItemStackHandler());
				}
				return null;
			}
			
			@Override
			public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
				if (ID == LibGUI.GUI_SIMPLE_INVENTORY_ID) {
					TileSimpleInventory inv = (TileSimpleInventory) world.getTileEntity(new BlockPos(x, y, z));
					return new GuiSimpleChest(player.inventory, inv.getItemStackHandler(),inv.getGUIName());
				}
				return null;
				// TODO Customize name for each inventory, (check instanceof TileSimpleInventory?)
			}
		});
	}
	
}
