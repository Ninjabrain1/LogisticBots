package ninjabrain.logisticbots.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ninjabrain.logisticbots.LogisticBots;
import ninjabrain.logisticbots.item.ModItems;
import ninjabrain.logisticbots.tile.TileRoboport;

public class BlockRoboport extends BlockBase {

	public BlockRoboport(String name) {
		super(name, Material.IRON);
		
		ModItems.items.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		// TODO drop items when broken
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileRoboport) {
			((TileRoboport)tile).onRemove();
		}
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileRoboport();
	}
	
	@SideOnly(value = Side.CLIENT)
	@Override
	public void registerModels() {
		LogisticBots.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
	
}
