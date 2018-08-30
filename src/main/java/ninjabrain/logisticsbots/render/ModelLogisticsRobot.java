package ninjabrain.logisticsbots.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLogisticsRobot extends ModelBase {
	
	private ModelRenderer body;
	
	public ModelLogisticsRobot() {
		
		textureHeight = 32;
		textureWidth = 32;
		
		body = new ModelRenderer(this, 0, 0);
		body.setRotationPoint(0.0F, 16.0F, 0.0F);
		body.addBox(-4, 0, -4, 8, 8, 8);
	}
	
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		
		body.render(scale);
		
//		GlStateManager.pushMatrix();
//		GlStateManager.translate(0.0f, 0.6f, 0.0f);
//		this.body.render(scale);
//		GlStateManager.popMatrix();
	}
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		
		body.rotateAngleY = 0.0f;
	}
	
}
