package ninjabrain.logisticbots.network;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import ninjabrain.logisticbots.api.network.IStorable;

public class LBItemStack implements IStorable {
	
	protected ItemStack stack;
	
	public LBItemStack(@Nonnull ItemStack stack) {
		this.stack = stack;
	}
	
	public ItemStack getStack() {
		return stack;
	}
	
}
