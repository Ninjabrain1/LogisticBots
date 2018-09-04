package ninjabrain.logisticbots.item;

import net.minecraft.item.Item;
import ninjabrain.logisticbots.LogisticBots;
import ninjabrain.logisticbots.render.IModelRegister;

public class ItemBase extends Item implements IModelRegister {

	public ItemBase(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		System.out.println(LogisticBots.creativeTab);
		setCreativeTab(LogisticBots.creativeTab);

		ModItems.items.add(this);
	}

	@Override
	public void registerModels() {
		LogisticBots.proxy.registerItemRenderer(this, 0, "inventory");
	}

}
