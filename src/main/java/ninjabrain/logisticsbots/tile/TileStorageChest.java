package ninjabrain.logisticsbots.tile;

import net.minecraft.client.resources.I18n;
import ninjabrain.logisticsbots.block.BlockLogisticsChest;
import ninjabrain.logisticsbots.block.BlockLogisticsChest.Types;

public class TileStorageChest extends TileSimpleInventory {
	
	@Override
	public String getGUIName() {
		return I18n.format(BlockLogisticsChest.getUnlocalizedName(Types.STORAGECHEST) + ".name");
	}
	
}
