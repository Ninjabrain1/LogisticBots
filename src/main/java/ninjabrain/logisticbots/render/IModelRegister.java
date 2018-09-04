package ninjabrain.logisticbots.render;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IModelRegister {
	
	@SideOnly(Side.CLIENT)
	public void registerModels();
	
}
