package ninjabrain.logisticsbots.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import ninjabrain.logisticsbots.LogisticsBots;
import ninjabrain.logisticsbots.item.ModItems;
import ninjabrain.logisticsbots.render.IModelRegister;

public class BlockBase extends Block implements IModelRegister {
	
	public BlockBase(String name, Material material) {
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(LogisticsBots.creativeTab);
		setHardness(3.0f);
		
		ModBlocks.blocks.add(this);
		ModItems.items.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public void registerModels() {
		LogisticsBots.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
	
}
