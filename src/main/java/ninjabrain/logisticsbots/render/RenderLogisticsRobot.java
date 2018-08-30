package ninjabrain.logisticsbots.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import ninjabrain.logisticsbots.EntityLogisticsRobot;
import ninjabrain.logisticsbots.LogisticsBots;

//public class RenderLogisticsRobot extends RenderLiving<EntityLogisticsRobot> {
//
//	// private static final ResourceLocation LOGISTICS_ROBOT_TEXTURES = new
//	// ResourceLocation("textures/entity/");
//	private final ModelBase mainModel = new ModelLogisticsRobot();
//	
//	public RenderLogisticsRobot(RenderManager rendermanagerIn) {
//		super(rendermanagerIn, new ModelLogisticsRobot(), 0.4f);
//	}
//
//	@Override
//	protected ResourceLocation getEntityTexture(EntityLogisticsRobot entity) {
//		return new ResourceLocation(LogisticsBots.MODID, "textures/items/logisticsrobot.png");
//	}
//
//}
public class RenderLogisticsRobot extends Render<EntityLogisticsRobot> {

	private static final ResourceLocation LOGISTICS_ROBOT_TEXTURE = new ResourceLocation(LogisticsBots.MODID, "textures/entity/logisticsrobot.png");
	private final ModelBase mainModel;
	
	public RenderLogisticsRobot(RenderManager rendermanagerIn) {
		super(rendermanagerIn);
		mainModel = new ModelLogisticsRobot();
	}
	
	@Override
	public void doRender(EntityLogisticsRobot entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x, (float)y, (float)z);
		bindTexture(LOGISTICS_ROBOT_TEXTURE);
		mainModel.render(entity, 0, 0, entity.ticksExisted, 0, 0, prepareScale());
		GlStateManager.popMatrix();
		
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityLogisticsRobot entity) {
		return LOGISTICS_ROBOT_TEXTURE;
	}
	
	private float prepareScale() {
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		float f = 0.0625F;
		GlStateManager.translate(0.0F, -1.501F, 0.0F);
		return f;
	}

}
