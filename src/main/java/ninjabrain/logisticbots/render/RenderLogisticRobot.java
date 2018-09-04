package ninjabrain.logisticbots.render;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import ninjabrain.logisticbots.entity.EntityLogisticRobot;
import ninjabrain.logisticbots.lib.LibResources;

public class RenderLogisticRobot extends Render<EntityLogisticRobot> {
	
	private final ModelLogisticRobot mainModel;
	private static Random random = new Random();
	
	public RenderLogisticRobot(RenderManager rendermanagerIn) {
		super(rendermanagerIn);
		mainModel = new ModelLogisticRobot();
	}
	
	@Override
	public void doRender(EntityLogisticRobot entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		
		mainModel.setClawAngles(entity.isCarryingSomething());
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		bindTexture(LibResources.LOGISTICS_ROBOT_TEXTURE);
		mainModel.render(entity, 0, 0, entity.ticksExisted, entity.rotationYaw, entity.rotationPitch, getScale());
		GlStateManager.popMatrix();
		
		renderItem(entity, x, y, z, entityYaw, partialTicks);
		
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityLogisticRobot entity) {
		return LibResources.LOGISTICS_ROBOT_TEXTURE;
	}
	
	protected float getScale() {
		// GlStateManager.enableRescaleNormal();
		// GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		float f = 0.0625F / 2f; // 0.0625 = 1/16
		// GlStateManager.translate(0.0F, -1.501F, 0.0F);
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
	
	/**
	 * Renders the item the robot is carrying
	 */
	private void renderItem(EntityLogisticRobot entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
		ItemStack itemstack = entity.getInventoryStack();
		int i = itemstack.isEmpty() ? 187 : Item.getIdFromItem(itemstack.getItem()) + itemstack.getMetadata();
		random.setSeed((long) i);
		
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		this.renderManager.renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
		
		GlStateManager.enableRescaleNormal();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		GlStateManager.pushMatrix();
		IBakedModel ibakedmodel = itemRenderer.getItemModelWithOverrides(itemstack, entity.world, null);
		int j = this.transformModelCount(entity, x, y, z, partialTicks, ibakedmodel);
		
		float f3 = -0.0F * (float) (j - 1) * 0.5F;
		float f4 = -0.0F * (float) (j - 1) * 0.5F;
		float f5 = -0.032F * (float) (j - 1) * 0.5F;
		GlStateManager.translate(f3, f4, f5);
		
		for (int k = 0; k < j; ++k) {
			GlStateManager.pushMatrix();
			
			if (k > 0) {
				float f8 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
				float f10 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
				GlStateManager.translate(f8, f10, 0.0F);
			}
			
			IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient
					.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);
			itemRenderer.renderItem(itemstack, transformedModel);
			GlStateManager.popMatrix();
			GlStateManager.translate(0.0F, 0.0F, 0.032f);
			
		}
		
		GlStateManager.popMatrix();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
		
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		this.renderManager.renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
	}
	
	private int transformModelCount(EntityLogisticRobot entity, double x, double y, double z, float partialTicks,
			IBakedModel iBakedModel) {
		ItemStack itemstack = entity.getInventoryStack();
		Item item = itemstack.getItem();
		
		if (item == null) {
			return 0;
		} else {
			int i = this.getModelCount(itemstack);
			GlStateManager.translate((float) x, (float) y, (float) z);
			
			GlStateManager.rotate(-entity.rotationYaw, 0.0F, 1.0F, 0.0F);
			
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			return i;
		}
	}
	
	protected int getModelCount(ItemStack stack) {
		int i = 1;
		
		if (stack.getCount() > 48) {
			i = 5;
		} else if (stack.getCount() > 32) {
			i = 4;
		} else if (stack.getCount() > 16) {
			i = 3;
		} else if (stack.getCount() > 1) {
			i = 2;
		}
		
		return i;
	}
	
}
