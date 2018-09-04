package ninjabrain.logisticbots.item;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import ninjabrain.logisticbots.block.BlockLogisticChest;
import ninjabrain.logisticbots.block.BlockLogisticChest.ChestType;

public class ItemLogisticChest extends ItemBlock {
	
	public ItemLogisticChest(BlockLogisticChest block) {
		super(block);
		setHasSubtypes(true);
	}

	public int getMetadata(int damage) {
		return damage;
	}
	
	public String getUnlocalizedName(ItemStack stack) {
		int meta = stack.getMetadata();
		if (meta >= 0 && meta < ChestType.values().length) {
			ChestType type = ChestType.values()[meta];
			return BlockLogisticChest.getUnlocalizedName(type);
		}
		return super.getUnlocalizedName();
	}
	
}
