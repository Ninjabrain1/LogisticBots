package ninjabrain.logisticsbots.item;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import ninjabrain.logisticsbots.block.BlockLogisticsChest;
import ninjabrain.logisticsbots.block.BlockLogisticsChest.Types;

public class ItemLogisticsChest extends ItemBlock {
	
	public ItemLogisticsChest(BlockLogisticsChest block) {
		super(block);
		setHasSubtypes(true);
	}

	public int getMetadata(int damage) {
		return damage;
	}
	
	public String getUnlocalizedName(ItemStack stack) {
		int meta = stack.getMetadata();
		if (meta >= 0 && meta < Types.values().length) {
			Types type = Types.values()[meta];
			return BlockLogisticsChest.getUnlocalizedName(type);
		}
		return super.getUnlocalizedName();
	}
	
}
