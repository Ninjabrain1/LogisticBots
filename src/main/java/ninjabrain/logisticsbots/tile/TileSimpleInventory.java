package ninjabrain.logisticsbots.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Simple inventory TileEntity with 54 item slots
 */
public class TileSimpleInventory extends TileEntity {
	
	private static final int INVENTORY_SIZE = 54;
	
	protected ItemStackHandler itemStackHandler = createItemStackHandler();
	
	protected ItemStackHandler createItemStackHandler() {
		return new ItemStackHandler(INVENTORY_SIZE);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
		return super.getCapability(capability, facing);
	}
	
	public ItemStackHandler getItemStackHandler() {
		return itemStackHandler;
	}
	
	/**
	 * Returns the string that will be displayed as the name of this inventory when
	 * the player opens this inventorys GUI. This is usually the same as the name of
	 * the block this tile should be attached to.
	 */
	public String getGUIName() {
		return "";
	}
	
	// ################################################################# //
	// ########################### NBT Stuff ########################### //
	// ################################################################# //
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound ret = super.writeToNBT(compound);
		writePacketNBT(ret);
		return ret;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		readPacketNBT(compound);
	}
	
	public void writePacketNBT(NBTTagCompound compound) {
		compound.merge(itemStackHandler.serializeNBT());
	}
	
	public void readPacketNBT(NBTTagCompound compound) {
		itemStackHandler = createItemStackHandler();
		itemStackHandler.deserializeNBT(compound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		readPacketNBT(pkt.getNbtCompound());
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound compound = new NBTTagCompound();
		writePacketNBT(compound);
		return new SPacketUpdateTileEntity(pos, 0, compound);
	}
	
}
