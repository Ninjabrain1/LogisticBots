package ninjabrain.logisticsbots.entity;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class EntityLogisticsRobot extends Entity {
	
	ItemStackHandler itemStackHandler;
	
	public EntityLogisticsRobot(World worldIn) {
		super(worldIn);
	}
	
	@Override
	protected void entityInit() {
		// TODO create custom IItemHandler
		itemStackHandler = new ItemStackHandler(1);
		setSize(0.3f, 0.4f);
	}
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if (!world.isRemote)
			this.rotationYaw++;
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		itemStackHandler = new ItemStackHandler(1);
		itemStackHandler.deserializeNBT(compound);
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.merge(itemStackHandler.serializeNBT());
	}
	
	/**
	 * Picks up as many items at possible from the given stack.
	 * 
	 * @param stack
	 * ItemStack to pick up
	 * @return The remaining stack that was not picked up
	 */
	public ItemStack pickUpStack(ItemStack stack) {
		return itemStackHandler.insertItem(0, stack, false);
	}
	
	/**
	 * @return true if this robot is carrying any items, false otherwise
	 */
	public boolean isCarryingSomething() {
		return !itemStackHandler.getStackInSlot(0).isEmpty();
	}
	
}
