package ninjabrain.logisticsbots;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityLogisticsRobot extends Entity{
	
	private boolean isCarrying = false;
	
	public EntityLogisticsRobot(World worldIn) {
		super(worldIn);
		setSize(1f, 1f);
	}
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		setSize(0.3f, 0.4f);
		if (!world.isRemote)
			this.rotationYaw++;
	}
	
	@Override
	protected void entityInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
	}
	
	public boolean isCarryingSomething() {
		return isCarrying;
	}

}
