package ninjabrain.logisticbots.tile;

import net.minecraft.client.resources.I18n;
import ninjabrain.logisticbots.block.BlockLogisticChest;
import ninjabrain.logisticbots.block.BlockLogisticChest.ChestType;

public class TileActiveProviderChest extends TileSimpleInventory {
	
	@Override
	public String getGUIName() {
		return I18n.format(BlockLogisticChest.getUnlocalizedName(ChestType.ACTIVEPROVIDERCHEST) + ".name");
	}
	
}
