package ninjabrain.logisticsbots;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ninjabrain.logisticsbots.item.ModCreativeTab;
import ninjabrain.logisticsbots.lib.LibMod;
import ninjabrain.logisticsbots.proxy.CommonProxy;

@Mod(modid = LibMod.MODID, name = LibMod.NAME, version = LibMod.VERSION)
public class LogisticsBots {
	
	public static Logger logger;
	
	@Instance(LibMod.MODID)
	public static LogisticsBots instance;
	
	@SidedProxy(clientSide = LibMod.CLIENT_PROXY_CLASS, serverSide = LibMod.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static CreativeTabs creativeTab = new ModCreativeTab(LibMod.MODID);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}
	
	// TODO add tooltips describing what each chest does
	
}
