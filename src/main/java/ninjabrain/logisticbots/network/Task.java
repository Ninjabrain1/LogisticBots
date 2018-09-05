package ninjabrain.logisticbots.network;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import ninjabrain.logisticbots.entity.EntityLogisticRobot;
import ninjabrain.logisticbots.tile.TileSimpleInventory;

/**
 * A task for a robot in a Logistic Network.
 */
public class Task {
	
	// Extract items from source and deliver them to target
	private TileSimpleInventory source, target;
	private EntityLogisticRobot robot;
	private Vec3d startPos, sourcePos, targetPos;
	// Time passed in ticks since the task was started
	private int ticks;
	// Time in ticks it will take to reach the source
	private int etaSource;
	// Time in ticks it will take to complete the task (reach the target)
	private int etaTarget;
	
	/**
	 * Creates a task for the robot to transfer an ItemStack from source to target.
	 */
	public Task(TileSimpleInventory source, TileSimpleInventory target, EntityLogisticRobot robot) {
		this.robot = robot;
		this.source = source;
		this.target = target;
		
		this.startPos = robot.getPositionVector();
		this.sourcePos = blockPosToVector(source.getPos());
		this.targetPos = blockPosToVector(target.getPos());
		
		float speed = robot.getSpeed();
		etaSource = (int) (startPos.distanceTo(sourcePos) / speed);
		etaTarget = (int) (sourcePos.distanceTo(targetPos) / speed) + etaSource;
		
	}
	
	/**
	 * Should be called every tick in the server thread.
	 * 
	 * @return true if this task is completed, false if not.
	 */
	public boolean update() {
		ticks++;
		
		Vec3d robotPos;
		if (ticks <= etaSource) {
			robotPos = lerp(startPos, sourcePos, (double) ticks / etaSource);
			if (ticks == etaSource) {
				ItemStack robotStack = robot.getInventoryStack();
				IItemHandler handler = source.getItemStackHandler();
				for (int i = 0; i < handler.getSlots(); i++) {
					ItemStack extractItem = handler.extractItem(i, 64, true);
					if (!extractItem.isEmpty()) {
						if ((robotStack.isEmpty() || robotStack.getCount() < robotStack.getMaxStackSize()
								&& ItemHandlerHelper.canItemStacksStack(extractItem, robotStack))) {
							extractItem = handler.extractItem(i, 64, false);
							robot.pickUpStack(extractItem);
							return true;
						}
						
					}
				}
			}
		} else {
			robotPos = lerp(sourcePos, targetPos, (double) (ticks - etaSource) / (etaTarget - etaSource));
			if (ticks == etaTarget) {
				ItemStack robotStack = robot.getInventoryStack();
				IItemHandler handler = target.getItemStackHandler();
				for (int i = 0; i < handler.getSlots(); i++) {
					if (!robotStack.isEmpty())
						robotStack = handler.insertItem(i, robotStack, false);
				}
				robot.setInventoryStack(robotStack);
			}
		}
		robot.setPosition(robotPos.x, robotPos.y, robotPos.z);
		
		return isComplete();
	}
	
	/**
	 * Returns true if the task is completed
	 */
	public boolean isComplete() {
		return ticks == etaTarget;
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
