package ninjabrain.logisticsbots.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ninjabrain.logisticsbots.lib.LibGUI;
import ninjabrain.logisticsbots.lib.LibMod;
import ninjabrain.logisticsbots.tile.TileSimpleInventory;

public class BlockLogisticsChest extends BlockBase {
	
	public BlockLogisticsChest(String name) {
		super(name, Material.IRON);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		TileEntity te = worldIn.getTileEntity(pos);
		if (te == null || !(te instanceof TileSimpleInventory))
			return true;
		if (worldIn.isRemote)
			return true;
		playerIn.openGui(LibMod.MODID, LibGUI.GUI_SIMPLE_INVENTORY_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileSimpleInventory();
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
//		TileEntity te = worldIn.getTileEntity(pos);
//		if (te != null && te instanceof TileInventory) {
//			((TileInventory)te).
//		}
		super.breakBlock(worldIn, pos, state);
	}
	
}
