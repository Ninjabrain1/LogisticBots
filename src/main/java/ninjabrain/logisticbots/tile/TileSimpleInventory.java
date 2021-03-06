package ninjabrain.logisticbots.tile;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import ninjabrain.logisticbots.api.network.ISubNetwork;
import ninjabrain.logisticbots.api.network.INetworkStorage;
import ninjabrain.logisticbots.api.network.NetworkManager;
import ninjabrain.logisticbots.item.ModItems;
import ninjabrain.logisticbots.network.LBItemStack;

/**
 * Simple inventory TileEntity with 54 item slots
 */
public abstract class TileSimpleInventory extends TileEntity implements INetworkStorage<LBItemStack> {
	
	protected static final int INVENTORY_SIZE = 54;
	
	protected ItemStackHandler itemHandler = createItemStackHandler();
	
	/** The Logistic Network this chest is connected to, null if none **/
	@Nullable
	protected ISubNetwork<LBItemStack> network;
	
	protected ItemStackHandler createItemStackHandler() {
		return new ItemStackHandler(INVENTORY_SIZE);
	}
	
	@Override
	public void onLoad() {
		if (!world.isRemote)
			NetworkManager.addNetworkStorage(this);
	}
	
	@Override
	public void onChunkUnload() {
		onRemove();
	}
	
	/**
	 * Called when this block is broken or unloaded
	 */
	public void onRemove() {
		if (!world.isRemote)
			NetworkManager.removeNetworkStorage(this);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler);
		return super.getCapability(capability, facing);
	}
	
	public ItemStackHandler getItemStackHandler() {
		return itemHandler;
	}
	
	/**
	 * Returns the string that will be displayed as the name of this inventory when
	 * the player opens this inventorys GUI. This is usually the same as the name of
	 * the block this tile should be attached to.
	 */
	public abstract String getGUIName();
	
	@Override
	public ISubNetwork<LBItemStack> getNetwork() {
		return network;
	}
	
	@Override
	public void setNetwork(ISubNetwork<LBItemStack> network) {
		this.network = network;
		if (network != null)
			network.addWanted(this, new LBItemStack(new ItemStack(ModItems.logisticsRobot)));
	}
	
	@Override
	public Class<LBItemStack> getStorableType() {
		return LBItemStack.class;
	}
	
	@Override
	public LBItemStack insert(LBItemStack storable, boolean simulate) {
		return LBItemStack.insert(itemHandler, storable, simulate);
	}
	
	@Override
	public LBItemStack extract(LBItemStack storable, boolean simulate) {
		return LBItemStack.extract(itemHandler, storable, simulate);
	}
	
	@Override
	public Class<LBItemStack> getType() {
		return LBItemStack.class;
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
		compound.merge(itemHandler.serializeNBT());
	}
	
	public void readPacketNBT(NBTTagCompound compound) {
		itemHandler = createItemStackHandler();
		itemHandler.deserializeNBT(compound);
	}
	
	@Override
	public void onDataPacket(net.minecraft.network.NetworkManager net, SPacketUpdateTileEntity pkt) {
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
