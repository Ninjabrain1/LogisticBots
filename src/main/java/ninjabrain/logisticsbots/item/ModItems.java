package ninjabrain.logisticsbots.item;

import java.util.ArrayList;

import net.minecraft.item.Item;
import ninjabrain.logisticsbots.lib.LibNames;

public class ModItems {
	
	public static ArrayList<Item> items = new ArrayList<Item>();
	
	public static final ItemBase logisticsRobot = new ItemLogisticsRobot(LibNames.ITEM_LOGISTICS_ROBOT);
	
	
	
}
