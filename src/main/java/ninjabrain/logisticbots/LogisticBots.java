package ninjabrain.logisticbots;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ninjabrain.logisticbots.item.ModCreativeTab;
import ninjabrain.logisticbots.lib.LibMod;
import ninjabrain.logisticbots.proxy.CommonProxy;

@Mod(modid = LibMod.MODID, name = LibMod.NAME, version = LibMod.VERSION)
public class LogisticBots {
	
	public static Logger logger;
	
	@Instance(LibMod.MODID)
	public static LogisticBots instance;
	
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
	// TODO roboport multiblock?
}
