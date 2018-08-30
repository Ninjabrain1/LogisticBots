package ninjabrain.logisticsbots.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLogisticsRobot extends ModelBase {

	private ModelRenderer body;
	private ModelRenderer[] legs;

	public ModelLogisticsRobot() {

		textureHeight = 32;
		textureWidth = 32;
		// Reference length (in texture coordinate units). Other lengths are defined as
		// multiples of this unit
		int l = 8;

		// Width, depth and height in texture coordinate units
		int bodyTexelW = l, bodyTexelH = l, bodyTexelD = l;

		body = new ModelRenderer(this, 0, 0);
		body.setRotationPoint(0, bodyTexelH / 2, 0);
		body.addBox(-bodyTexelW / 2, -bodyTexelH / 2 + l/2, -bodyTexelD / 2, bodyTexelW, bodyTexelH, bodyTexelD);
		
		ModelRenderer[] joints = new ModelRenderer[4]; 
		legs = new ModelRenderer[4];
		for (int i = 0; i < legs.length; i++) {
			joints[i] = new ModelRenderer(this, 0, 0);
			legs[i] = new ModelRenderer(this, 0, bodyTexelD + bodyTexelH);
			body.addChild(joints[i]);
			joints[i].addChild(legs[i]);
			float offX = i == 0 || i == 3 ? -bodyTexelW/2 : bodyTexelW/2;
			float offZ = i == 0 || i == 1 ? -bodyTexelD/2 : bodyTexelD/2;
			float offY = -bodyTexelH;
			
			joints[i].setRotationPoint(offX, offY/2f + l/2, offZ);
			legs[i].addBox(-l/16f, offY/2f, -l/16f, l/8, l/2, l/8);
			
			ModelRenderer claw = new ModelRenderer(this, 0, bodyTexelD + bodyTexelH);
			claw.addBox(-l/16f, offY + l/3f, -l/16f - l/4, l/8, l/4, l/8);
			claw.rotateAngleX = -0.55f;
			legs[i].addChild(claw);
			
			float quarterTurn = (float) Math.PI/2.0f;
			joints[i].rotateAngleY = (-i + 0.5f)*quarterTurn;
		}

	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {

		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		
		body.render(scale);
		
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scaleFactor, Entity entityIn) {
		
		body.rotateAngleY = (float) Math.toRadians(-netHeadYaw);
		body.rotateAngleX = (float) Math.toRadians(headPitch);
	}
	
	public void setClawAngles(boolean isHoldingSomething) {
		float angle = isHoldingSomething ? -0.2f : 0.6f;
		for (ModelRenderer leg : legs) {
			leg.rotateAngleX = angle;
		}
	}

}
