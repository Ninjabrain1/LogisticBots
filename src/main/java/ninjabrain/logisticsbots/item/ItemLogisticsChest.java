package ninjabrain.logisticsbots.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import ninjabrain.logisticsbots.block.BlockLogisticsChest;

public class ItemLogisticsChest extends ItemBlock {
	
	public ItemLogisticsChest(Block block) {
		super(block);
		setHasSubtypes(true);
	}

	public int getMetadata(int damage) {
		return damage;
	}
	
	public String getUnlocalizedName(ItemStack stack) {
		// TODO better
		return super.getUnlocalizedName() + "." + BlockLogisticsChest.Types.values()[stack.getMetadata()].getName();
	}
	
}
