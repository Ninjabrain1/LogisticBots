package ninjabrain.logisticsbots.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import ninjabrain.logisticsbots.LogisticsBots;
import ninjabrain.logisticsbots.render.IModelRegister;

public abstract class BlockBase extends Block implements IModelRegister {
	
	public BlockBase(String name, Material material) {
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(LogisticsBots.creativeTab);
		setHardness(3.0f);
		
		ModBlocks.blocks.add(this);
	}
	
	@Override
	public void registerModels() {}
	
}
