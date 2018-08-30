package ninjabrain.logisticsbots.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import ninjabrain.logisticsbots.EntityLogisticsRobot;
import ninjabrain.logisticsbots.lib.LibResources;

public class RenderLogisticsRobot extends Render<EntityLogisticsRobot> {

	private final ModelLogisticsRobot mainModel;

	public RenderLogisticsRobot(RenderManager rendermanagerIn) {
		super(rendermanagerIn);
		mainModel = new ModelLogisticsRobot();
	}

	@Override
	public void doRender(EntityLogisticsRobot entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		
		mainModel.setClawAngles(entity.isCarryingSomething());
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		bindTexture(LibResources.LOGISTICS_ROBOT_TEXTURE);
		mainModel.render(entity, 0, 0, entity.ticksExisted, entity.rotationYaw, entity.rotationPitch, getScale());
		GlStateManager.popMatrix();

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityLogisticsRobot entity) {
		return LibResources.LOGISTICS_ROBOT_TEXTURE;
	}

	protected float getScale() {
//		GlStateManager.enableRescaleNormal();
//		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		float f = 0.0625F/2f; // 0.0625 = 1/16
//		GlStateManager.translate(0.0F, -1.501F, 0.0F);
		return f;
	}

	protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
		float f;
		for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F) {
			;
		}
		while (f >= 180.0F) {
			f -= 360.0F;
		}
		return prevYawOffset + partialTicks * f;
	}

}
