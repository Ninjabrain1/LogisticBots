package ninjabrain.logisticsbots.block;

import java.util.function.Supplier;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ninjabrain.logisticsbots.LogisticsBots;
import ninjabrain.logisticsbots.item.ItemLogisticsChest;
import ninjabrain.logisticsbots.item.ModItems;
import ninjabrain.logisticsbots.lib.LibGUI;
import ninjabrain.logisticsbots.lib.LibMod;
import ninjabrain.logisticsbots.lib.LibNames;
import ninjabrain.logisticsbots.tile.TileActiveProviderChest;
import ninjabrain.logisticsbots.tile.TileSimpleInventory;
import ninjabrain.logisticsbots.tile.TileStorageChest;

public class BlockLogisticsChest extends BlockBase {
	
	public static final PropertyEnum<Types> TYPE = PropertyEnum.create("type", Types.class);
	
	public BlockLogisticsChest(String name) {
		super(name, Material.IRON);
		
		setDefaultState(blockState.getBaseState().withProperty(TYPE, Types.ACTIVEPROVIDERCHEST));
		
		ModItems.items.add(new ItemLogisticsChest(this).setRegistryName(this.getRegistryName()));
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
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		// TODO
		// TileEntity te = worldIn.getTileEntity(pos);
		// if (te != null && te instanceof TileInventory) {
		// ((TileInventory)te).
		// }
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return state.getValue(TYPE).createTileEntity();
	}
	
	@SideOnly(value = Side.CLIENT)
	@Override
	public void registerModels() {
		for (int i = 0; i < Types.values().length; i++) {
			Types type = Types.values()[i];
			IBlockState state = getDefaultState().withProperty(TYPE, type);
			Item item = Item.getItemFromBlock(state.getBlock());
			int meta = i;
			String id = TYPE.getName() + "=" + type.getName();
			LogisticsBots.proxy.registerItemRenderer(item, meta, id);
		}
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for (Types type : Types.values()) {
			items.add(new ItemStack(this, 1, type.getMeta()));
		}
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).getMeta();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, Types.values()[meta]);
	}
	
	/**
	 * Returns the unlocalized name of the BlockLogisticsChest of the given type
	 * with "tile." appended to the front
	 */
	public static String getUnlocalizedName(Types type) {
		return ModBlocks.blockLogisticsChest.getUnlocalizedName() + "." + type.getUnlocalizedNameSuffix();
	}
	
	/**
	 * All types of logistics chests
	 */
	public enum Types implements IStringSerializable {
		STORAGECHEST(TileStorageChest::new, LibNames.STATE_STORAGE), ACTIVEPROVIDERCHEST(TileActiveProviderChest::new,
				LibNames.STATE_ACTIVE_PROVIDER);
		
		final Supplier<TileEntity> tileEntitySupplier;
		final String name;
		final String unlocalizedNameSuffix;
		
		private Types(Supplier<TileEntity> tileEntitySupplier, String unlocalizedNameSuffix) {
			this.tileEntitySupplier = tileEntitySupplier;
			this.name = name().toLowerCase();
			this.unlocalizedNameSuffix = unlocalizedNameSuffix;
		}
		
		public TileEntity createTileEntity() {
			return tileEntitySupplier.get();
		}
		
		public int getMeta() {
			return ordinal();
		}
		
		public String getUnlocalizedNameSuffix() {
			return unlocalizedNameSuffix;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
	}
	
}
