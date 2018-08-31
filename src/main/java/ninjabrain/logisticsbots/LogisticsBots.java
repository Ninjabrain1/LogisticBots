package ninjabrain.logisticsbots;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import ninjabrain.logisticsbots.gui.ContainerMod;
import ninjabrain.logisticsbots.gui.GuiLogisticsChest;
import ninjabrain.logisticsbots.item.ModItems;
import ninjabrain.logisticsbots.lib.LibMod;
import ninjabrain.logisticsbots.proxy.CommonProxy;
import ninjabrain.logisticsbots.tile.TileInventory;

@Mod(modid = LibMod.MODID, name = LibMod.NAME, version = LibMod.VERSION)
public class LogisticsBots
{

    public static Logger logger;
    
    @Instance(LibMod.MODID)
    public static LogisticsBots instance;
    
    @SidedProxy(clientSide = LibMod.CLIENT_PROXY_CLASS, serverSide = LibMod.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;
    
    public static CreativeTabs creativeTab;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
        
        NetworkRegistry.INSTANCE.registerGuiHandler(LogisticsBots.instance, new IGuiHandler() {
			@Override
			public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
				TileInventory inv = (TileInventory) world.getTileEntity(new BlockPos(x, y, z));
				return new ContainerMod(player.inventory, inv.getItemStackHandler());
			}
			@Override
			public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
				TileInventory inv = (TileInventory) world.getTileEntity(new BlockPos(x, y, z));
				return new GuiLogisticsChest(player.inventory, inv.getItemStackHandler(), inv.getBlockType().getLocalizedName());
			}
		});
        
        creativeTab = new CreativeTabs(LibMod.MODID) {
        	ItemStack icon = new ItemStack(ModItems.items.get(0));
			@Override
			public ItemStack getTabIconItem() {
				return icon;
			}
		};
		
    }
}
