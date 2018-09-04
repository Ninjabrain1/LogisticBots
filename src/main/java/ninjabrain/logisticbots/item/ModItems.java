package ninjabrain.logisticbots.item;

import java.util.ArrayList;

import net.minecraft.item.Item;
import ninjabrain.logisticbots.lib.LibNames;

public class ModItems {
	
	public static ArrayList<Item> items = new ArrayList<Item>();
	
	public static final ItemBase logisticsRobot = new ItemLogisticRobot(LibNames.ITEM_LOGISTICS_ROBOT);
	
	
	
}
