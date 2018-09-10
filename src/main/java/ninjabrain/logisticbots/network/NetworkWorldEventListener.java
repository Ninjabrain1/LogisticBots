package ninjabrain.logisticbots.network;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ninjabrain.logisticbots.api.network.NetworkManager;
import ninjabrain.logisticbots.entity.EntityLogisticRobot;

/**
 * Listens to onEntityAdded/onEntityRemoved events to add/remove Logistic Robots
 * from Logistic Networks.
 */
@Mod.EventBusSubscriber
public class NetworkWorldEventListener implements IWorldEventListener {
	
	/**
	 * Attaches an instance of this class to every server world that loads
	 */
	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		World world = event.getWorld();
		if (!world.isRemote) {
			world.addEventListener(new NetworkWorldEventListener(world));
		}
	}
	
	World world;
	
	public NetworkWorldEventListener(World world) {
		this.world = world;
	}
	
	@Override
	public void onEntityAdded(Entity entityIn) {
		// TODO can this cause a robot to be added multiple times?
		if (entityIn instanceof EntityLogisticRobot) {
			NetworkManager.addTransporter((EntityLogisticRobot) entityIn);
		}
	}
	
	@Override
	public void onEntityRemoved(Entity entityIn) {
		// TODO should the robot really be removed from its network when it is unloaded?
		if (entityIn instanceof EntityLogisticRobot) {
			NetworkManager.removeTransporter((EntityLogisticRobot) entityIn);;
		}
	}
	
	@Override
	public void notifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags) {
	}
	
	@Override
	public void notifyLightSet(BlockPos pos) {
	}
	
	@Override
	public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
	}
	
	@Override
	public void playSoundToAllNearExcept(EntityPlayer player, SoundEvent soundIn, SoundCategory category, double x,
			double y, double z, float volume, float pitch) {
	}
	
	@Override
	public void playRecord(SoundEvent soundIn, BlockPos pos) {
	}
	
	@Override
	public void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord,
			double xSpeed, double ySpeed, double zSpeed, int... parameters) {
	}
	
	@Override
	public void spawnParticle(int id, boolean ignoreRange, boolean p_190570_3_, double x, double y, double z,
			double xSpeed, double ySpeed, double zSpeed, int... parameters) {
	}
	
	@Override
	public void broadcastSound(int soundID, BlockPos pos, int data) {
	}
	
	@Override
	public void playEvent(EntityPlayer player, int type, BlockPos blockPosIn, int data) {
	}
	
	@Override
	public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
	}
	
}
