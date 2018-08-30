package ninjabrain.logisticsbots.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import ninjabrain.logisticsbots.LogisticsBots;
import ninjabrain.logisticsbots.render.IModelRegister;

public class ItemBase extends Item implements IModelRegister {

	public ItemBase(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		ModItems.items.add(this);
		setCreativeTab(LogisticsBots.creativeTab);

	}

	@Override
	public void registerModels() {
		LogisticsBots.proxy.registerItemRenderer(this, 0, "inventory");
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return new CreativeTabs[] { LogisticsBots.creativeTab };
	}

}
