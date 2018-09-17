package ninjabrain.logisticbots.tile;

import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;
import ninjabrain.logisticbots.api.network.INetwork;
import ninjabrain.logisticbots.api.network.INetworkProvider;
import ninjabrain.logisticbots.api.network.IStorable;
import ninjabrain.logisticbots.api.network.ITransporter;
import ninjabrain.logisticbots.api.network.ITransporterStorage;
import ninjabrain.logisticbots.api.network.NetworkManager;
import ninjabrain.logisticbots.entity.EntityLogisticRobot;
import ninjabrain.logisticbots.network.LBItemStack;
import ninjabrain.logisticbots.network.Network;

/**
 * Roboport tile entity that contains a Logistic Network
 */
public class TileRoboport extends TileEntity implements ITickable, INetworkProvider, ITransporterStorage {
	
	INetwork network;
	
	Vec3d robotIO;
	
	Queue<EntityLogisticRobot> robotsToSpawn = new LinkedList<EntityLogisticRobot>();
	
	@Override
	public void onLoad() {
		// TODO instead of checking world.isRemote all the time make client and server
		// specific networks
		if (!world.isRemote) {
			network = NetworkManager.addNetworkProvider(this);
			if (network != NetworkManager.addTransporterStorage(this)) {
				throw new Error("Error when loading a roboport at " + getPos() + ", network is ambiguous.");
			}
			robotIO = new Vec3d(getPos().getX() + 0.5, getPos().getY() + 1.5, getPos().getZ() + 0.5);
		}
	}
	
	@Override
	public void onChunkUnload() {
		// TODO should it really be removed on unload? If not, dont add it on load
		onRemove();
	}
	
	@Override
	public void update() {
		if (robotsToSpawn.size() > 0) {
			spawnRobot(robotsToSpawn.poll());
		}
	}
	
	public void onRemove() {
		if (!world.isRemote) {
//			while(robotsToSpawn.size() > 0) {
//				spawnRobot(robotsToSpawn.poll());
//			}
			robotsToSpawn.forEach(robot -> spawnRobot(robot));
			
			NetworkManager.removeNetworkProvider(this);
			NetworkManager.removeTransporterStorage(this);
		}
	}

	@Override
	public INetwork getNetwork() {
		return network;
	}
	
	@Override
	public void setNetwork(INetwork network) {
		this.network = network;
	}

	@Override
	public INetwork createNewNetwork() {
		Network network = new Network(world);
		network.getProviders().add(this);
		return network;
	}

	@Override
	public void insert(ITransporter<? extends IStorable> transp) {
		// TODO roboport inventory
	}

	@Override
	public boolean hasSpace(ITransporter<? extends IStorable> transp) {
		// TODO roboport inventory
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IStorable> ITransporter<T> extract(Class<T> type) {
		// TODO roboport inventory
		if (type == LBItemStack.class) {
			EntityLogisticRobot robot = new EntityLogisticRobot(world, false);
			robot.setPosition(robotIO.x, robotIO.y, robotIO.z);
			
			robotsToSpawn.add(robot);
			
			return (ITransporter<T>) robot;
		}
		return null;
	}
	
	private void spawnRobot(EntityLogisticRobot e) {
		world.spawnEntity(e);
		e.activate();
	}

	@Override
	public boolean hasTransporter(Class<? extends IStorable> type) {
		// TODO roboport inventory
		return true;
	}
	
	
	
}
