package ninjabrain.logisticbots.entity;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import ninjabrain.logisticbots.api.network.INetwork;
import ninjabrain.logisticbots.api.network.ITask;
import ninjabrain.logisticbots.api.network.ITransporter;
import ninjabrain.logisticbots.api.network.ITransporterStorage;
import ninjabrain.logisticbots.network.LBItemStack;

public class EntityLogisticRobot extends Entity implements ITransporter<LBItemStack> {
	
	// TODO better synchronization with client, (Current solution uses 2 duplicate ItemStacks)
	protected static final DataParameter<ItemStack> INVENTORY = EntityDataManager
			.<ItemStack>createKey(EntityLogisticRobot.class, DataSerializers.ITEM_STACK);
	
	protected static final String TAG_INVENTORY = "invStack";
	
	protected INetwork<LBItemStack> network;
	protected ItemStackHandler itemHandler = createItemStackHandler();
	protected ITask<LBItemStack> task;
	
	/** The speed of Logistic Robots in Blocks/tick */
	protected static float speed = 0.3f;
	
	public EntityLogisticRobot(World worldIn) {
		super(worldIn);
	}
	
	protected ItemStackHandler createItemStackHandler() {
		return new ItemStackHandler(1) {
			@Override
			protected void onContentsChanged(int slot) {
				super.onContentsChanged(slot);
				dataManager.set(INVENTORY, getStackInSlot(0));
			}
		};
	}
	
	@Override
	protected void entityInit() {
		dataManager.register(INVENTORY, ItemStack.EMPTY);
		setSize(0.3f, 0.4f);
	}
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if (!world.isRemote) {
			this.rotationYaw++;
		} else {
			posX += motionX;
			posY += motionY;
			posZ += motionZ;
		}
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		ItemStack loadedStack = new ItemStack(compound.getCompoundTag(TAG_INVENTORY));
		dataManager.set(INVENTORY, loadedStack);
//		itemHandler = createItemStackHandler();
//		itemHandler.deserializeNBT(compound);
		
		itemHandler.setStackInSlot(0, loadedStack);
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setTag(TAG_INVENTORY, getItemStack().serializeNBT());
//		compound.merge(itemHandler.serializeNBT());
	}
	
	/**
	 * @return The ItemStack this robot is carrying. Returns ItemStack.EMPTY if the
	 * stack is empty.
	 */
	public ItemStack getItemStack() {
		itemHandler.setStackInSlot(0, dataManager.get(INVENTORY));
		return itemHandler.getStackInSlot(0);
	}
	
	/**
	 * @return true if this robot is carrying any items, false otherwise
	 */
	public boolean isCarryingSomething() {
		return !itemHandler.getStackInSlot(0).isEmpty();
	}
	
	/**
	 * Returns the max speed of this Logistic Robot in Blocks/tick
	 */
	public float getSpeed() {
		return speed;
	}
	
	@Override
	public Class<LBItemStack> getStorableType() {
		return LBItemStack.class;
	}
	
	@Override
	public LBItemStack insert(LBItemStack storable, boolean simulate) {
		ItemStack remainder = itemHandler.insertItem(0, storable.getStack(), simulate);
		return new LBItemStack(remainder);
	}
	
	@Override
	public LBItemStack extract(LBItemStack storable, boolean simulate) {
		int amount;
		if (storable == LBItemStack.ANY_STACK) {
			amount = Integer.MAX_VALUE; 
		} else {
			amount = storable.getStack().getCount();
		}
		ItemStack extracted = itemHandler.extractItem(0, amount, simulate);
		return new LBItemStack(extracted);
	}
	
	// Task related variables
	// Position of this robot when the task was given and the desired pos
	protected Vec3d startPos, desPos;
	// Amount of ticks it will take to complete the task
	protected int eta;
	// Amount of ticks that has passed since the task was given
	protected int ticks;
	
	@Override
	public void updateTask() {
		if (task != null) {
			ticks++;
			Vec3d robotPos = lerp(startPos, desPos, (double) ticks / eta);
			setPositionAndUpdate(robotPos.x, robotPos.y, robotPos.z);
			if (ticks == eta) {
				task.onComplete(this);
				setTask(task.getNextTask());
			}
		}
	}
	
	@Override
	public void setTask(ITask<LBItemStack> task) {
		this.task = task;
		startPos = getPositionVector();
		desPos = task == null ? startPos : blockPosToVector(task.getDesiredPos());
		eta = (int) (startPos.distanceTo(desPos) / speed);
		eta = eta == 0 ? 1 : eta;
		ticks = 0;
		Vec3d vel = desPos.subtract(startPos).normalize().scale(speed);
		
		motionX = vel.x;
		motionY = vel.y;
		motionZ = vel.z;
		markVelocityChanged();
	}
	
	@Override
	public boolean hasTask() {
		return this.task != null;
	}
	
	@Override
	public BlockPos getPos() {
		return getPosition();
	}
	
	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public INetwork<LBItemStack> getNetwork() {
		return network;
	}

	@Override
	public void setNetwork(INetwork<LBItemStack> network) {
		this.network = network;
	}
	
	@Override
	public Class<LBItemStack> getType() {
		return LBItemStack.class;
	}
	
	@Override
	public void onAddedToTransporterStorage(ITransporterStorage storage) {
		world.removeEntity(this);
	}
	
	/**
	 * Linearly interpolates between vectors a and b. k=0 will return a vector
	 * identical to a, k=1 will return a vector identical to b.
	 */
	protected Vec3d lerp(Vec3d a, Vec3d b, double k) {
		return a.scale(1.0 - k).add(b.scale(k));
	}
	
	protected Vec3d blockPosToVector(BlockPos pos) {
		return new Vec3d(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);
	}
	
}
