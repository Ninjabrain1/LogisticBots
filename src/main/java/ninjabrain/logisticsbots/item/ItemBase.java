package ninjabrain.logisticsbots.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import ninjabrain.logisticsbots.render.IModelRegister;

public class ItemBase extends Item implements IModelRegister{
	
	public ItemBase(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		ModItems.items.add(this);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
}
