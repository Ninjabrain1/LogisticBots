package ninjabrain.logisticbots.tile;

import net.minecraft.client.resources.I18n;
import ninjabrain.logisticbots.block.BlockLogisticChest;
import ninjabrain.logisticbots.block.BlockLogisticChest.ChestType;

public class TileStorageChest extends TileSimpleInventory {
	
	@Override
	public String getGUIName() {
		return I18n.format(BlockLogisticChest.getUnlocalizedName(ChestType.STORAGECHEST) + ".name");
	}

	@Override
	public boolean hasOpenInput() {
		return true;
	}

	@Override
	public boolean hasOpenOutput() {
		return true;
	}

	@Override
	public int getPriority() {
		return 0;
	}
	
}
