package ninjabrain.logisticsbots;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityLogisticsRobot extends Entity{

	public EntityLogisticsRobot(World worldIn) {
		super(worldIn);
		setSize(1f, 1f);
	}

	@Override
	protected void entityInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		System.out.println("READ");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		System.out.println("WRITE");
	}

}
