package ninjabrain.logisticsbots.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ninjabrain.logisticsbots.EntityLogisticsRobot;

public class ItemLogisticsRobot extends ItemBase {

	public ItemLogisticsRobot(String name) {
		super(name);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		if (worldIn.isRemote) {
			return EnumActionResult.SUCCESS;
		} else {
			pos = pos.offset(facing);
			ItemStack itemStack = player.getHeldItem(hand);
			Entity robot = new EntityLogisticsRobot(worldIn);
			robot.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
	
			worldIn.spawnEntity(robot);
	
			if (!player.capabilities.isCreativeMode) {
				itemStack.shrink(1);
			}
	
			return EnumActionResult.SUCCESS;
		}
	}

}
