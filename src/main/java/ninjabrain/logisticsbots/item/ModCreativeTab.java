package ninjabrain.logisticsbots.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ModCreativeTab extends CreativeTabs{
	
	public ModCreativeTab(String label) {
		super(label);
	}
	
	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.logisticsRobot);
	}
	
}
