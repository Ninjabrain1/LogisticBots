package ninjabrain.logisticbots.tile;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import ninjabrain.logisticbots.block.BlockLogisticChest;
import ninjabrain.logisticbots.block.BlockLogisticChest.ChestType;
import ninjabrain.logisticbots.network.LBItemStack;

public class TileActiveProviderChest extends TileSimpleInventory {
	
	@Override
	public String getGUIName() {
		return I18n.format(BlockLogisticChest.getUnlocalizedName(ChestType.ACTIVEPROVIDERCHEST) + ".name");
	}
	
	@Override
	protected ItemStackHandler createItemStackHandler() {
		return new CustomItemStackHandler(this, INVENTORY_SIZE);
	}
	
	class CustomItemStackHandler extends ItemStackHandler {
		
		TileSimpleInventory tsi;
		
		public CustomItemStackHandler(TileSimpleInventory tsi, int size) {
			super(size);
			this.tsi = tsi;
		}
		
		@Override
		public void setStackInSlot(int slot, ItemStack stack) {
			if (!world.isRemote && !stack.isEmpty() && network != null) {
				network.addUnwanted(tsi, new LBItemStack(stack));
			}
			super.setStackInSlot(slot, stack);
		}
		
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if (!world.isRemote && !simulate && network != null) {
				network.addUnwanted(tsi, new LBItemStack(stack));
			}
			return super.insertItem(slot, stack, simulate);
		}
		
	}
	
	@Override
	public boolean hasOpenInput() {
		return false;
	}
	
	@Override
	public boolean hasOpenOutput() {
		return true;
	}
	
	@Override
	public int getPriority() {
		return -1;
	}
	
}
