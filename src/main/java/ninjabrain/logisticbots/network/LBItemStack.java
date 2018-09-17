package ninjabrain.logisticbots.network;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import ninjabrain.logisticbots.api.network.IStorable;

public class LBItemStack implements IStorable {
	
	public static final LBItemStack ANY_STACK = new LBItemStack(ItemStack.EMPTY);
	
	protected final ItemStack stack;
	
	public LBItemStack(@Nonnull ItemStack stack) {
		this.stack = stack.copy();
	}
	
	public ItemStack getStack() {
		return stack;
	}
	
	/**
	 * Inserts as much as possible of the given LBItemStack and returns a
	 * LBItemStack that represents the stuff that was not picked up. The returned
	 * stack can be safely modified.
	 * 
	 * @param itemHandler
	 * The ItemStackHandler to insert to
	 * @param storable
	 * The LBItemStack to insert
	 * @param simulate
	 * If true the insertion is only simulated
	 * @return The remaining LBItemStack that was not inserted
	 */
	public static LBItemStack insert(ItemStackHandler itemHandler, LBItemStack storable, boolean simulate) {
		ItemStack stack = storable.getStack();
		for (int i = 0; i < itemHandler.getSlots(); i++) {
			if (stack.isEmpty())
				break;
			stack = itemHandler.insertItem(i, stack, simulate);
		}
		return new LBItemStack(stack);
	}
	
	/**
	 * Extracts as much as possible of the requested LBItemStack from the given
	 * ItemStackHandler and returns how much was actually extracted (removed from
	 * the ItemStackHandler). The returned stack can be safely modified.
	 * 
	 * @param itemHandler
	 * The ItemStackHandler to insert to
	 * @param storable
	 * The LBItemStack that should be extracted
	 * @param simulate
	 * If true the extraction is only simulated
	 * @return The LBItemStack that was subtracted from the ItemStackHandler
	 */
	public static LBItemStack extract(ItemStackHandler itemHandler, LBItemStack storable, boolean simulate) {
		ItemStack extracted = ItemStack.EMPTY;
		ItemStack requested;
		if (storable == ANY_STACK) {
			requested = null;
		} else {
			requested = storable.getStack().copy();
		}
		for (int i = 0; i < itemHandler.getSlots(); i++) {
			if (requested != null && requested.getCount() == 0)
				break;
			if (requested == null) {
				extracted = itemHandler.extractItem(i, Integer.MAX_VALUE, simulate);
				if (!extracted.isEmpty()) {
//					requested = new ItemStack(extracted.getItem(), extracted.getMaxStackSize() - extracted.getCount());
					requested = extracted.copy();
					requested.setCount(extracted.getMaxStackSize() - extracted.getCount());
				}
			}
			// TODO how to handle item damage / metadata?
			else if (ItemHandlerHelper.canItemStacksStack(itemHandler.getStackInSlot(i), requested)) {
				int oldCount = extracted.getCount();
				extracted = itemHandler.extractItem(i, requested.getCount(), simulate);
				requested.shrink(extracted.getCount());
				extracted.grow(oldCount);
			}
		}
		return new LBItemStack(extracted);
	}
	
}
