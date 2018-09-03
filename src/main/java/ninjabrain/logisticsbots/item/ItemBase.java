package ninjabrain.logisticsbots.item;

import net.minecraft.item.Item;
import ninjabrain.logisticsbots.LogisticsBots;
import ninjabrain.logisticsbots.render.IModelRegister;

public class ItemBase extends Item implements IModelRegister {

	public ItemBase(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		System.out.println(LogisticsBots.creativeTab);
		setCreativeTab(LogisticsBots.creativeTab);

		ModItems.items.add(this);
	}

	@Override
	public void registerModels() {
		LogisticsBots.proxy.registerItemRenderer(this, 0, "inventory");
	}

}
