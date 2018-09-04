package ninjabrain.logisticbots.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerMod extends Container {
	
	private final int modInvSize;
	
	protected static final int INV_HOTBAR_SPACE = 4, MODINV_INV_SPACE = 14, SLOT_SIZE = 18;
	
	public ContainerMod(InventoryPlayer playerInv, ItemStackHandler modInv) {
		modInvSize = modInv.getSlots();
		int modInvOffsetY = 18;
		int invOffsetY = modInvOffsetY + modInvSize / 9 * SLOT_SIZE + MODINV_INV_SPACE;
		int hotbarOffsetY = invOffsetY + 3 * SLOT_SIZE + INV_HOTBAR_SPACE;
		int offsetX = 8;
		// Tile entity inventory
		for (int i = 0; i < modInvSize; i++) {
			int col = i % 9;
			addSlotToContainer(new SlotItemHandler(modInv, i, col * SLOT_SIZE + offsetX, i / 9 * SLOT_SIZE + modInvOffsetY));
		}
		// Player inventory
		for (int i = 0; i < 27; i++) {
			int col = i % 9;
			addSlotToContainer(new Slot(playerInv, i + 9, col * SLOT_SIZE + offsetX, i / 9 * SLOT_SIZE + invOffsetY));
		}
		// Hotbar
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(playerInv, i, i * SLOT_SIZE + offsetX, hotbarOffsetY));
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (index < modInvSize) {
				if (!this.mergeItemStack(itemstack1, modInvSize, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (!this.mergeItemStack(itemstack1, 0, modInvSize, false)) {
					return ItemStack.EMPTY;
				}
			}
			
			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}
		
		return itemstack;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
}
