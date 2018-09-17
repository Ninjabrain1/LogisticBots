package ninjabrain.logisticbots.api.network;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A component of a Logistic Network that stores ITransporters
 */
public interface ITransporterStorage {
	
	/**
	 * Inserts the transporter into this storage.
	 */
	public void insert(ITransporter<? extends IStorable> transp);
	
	/**
	 * Returns true if this storage has space to insert the given transporter.
	 */
	public boolean hasSpace(ITransporter<? extends IStorable> transp);
	
	/**
	 * Tries to remove a transporter of the given type from this storage and returns
	 * it. Returns null if nothing was extracted.
	 */
	public <T extends IStorable> ITransporter<T> extract(Class<T> type);
	
	/**
	 * Returns true if this storage has a transporter of the given type
	 */
	public boolean hasTransporter(Class<? extends IStorable> type);
	
	/**
	 * Returns the position of this component in the world.
	 */
	public BlockPos getPos();
	
	/**
	 * Returns the INetwork this storage belongs to, null if none
	 */
	public INetwork getNetwork();
	
	/**
	 * Sets the INetwork this storage belongs to
	 */
	public void setNetwork(INetwork network);
	
	/**
	 * Returns the world this storage is in
	 */
	public World getWorld();
	
}
