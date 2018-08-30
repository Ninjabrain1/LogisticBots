package ninjabrain.logisticsbots;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ninjabrain.logisticsbots.item.ModItems;
import ninjabrain.logisticsbots.proxy.CommonProxy;

@Mod(modid = LogisticsBots.MODID, name = LogisticsBots.NAME, version = LogisticsBots.VERSION)
public class LogisticsBots
{
    public static final String MODID = "logisticsbots";
    public static final String NAME = "Logistics Bots";
    public static final String VERSION = "1.0.0";
    public static final String ACCEPTED_VERSIONS = "[1.12.2]";
    public static final String CLIENT_PROXY_CLASS = "ninjabrain.logisticsbots.proxy.ClientProxy";
    public static final String COMMON_PROXY_CLASS = "ninjabrain.logisticsbots.proxy.CommonProxy";

    public static Logger logger;
    
    @SidedProxy(clientSide = CLIENT_PROXY_CLASS, serverSide = COMMON_PROXY_CLASS)
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
        creativeTab = new CreativeTabs(MODID) {
        	ItemStack icon = new ItemStack(ModItems.items.get(0));
			@Override
			public ItemStack getTabIconItem() {
				return icon;
			}
		};
		
    }
}
