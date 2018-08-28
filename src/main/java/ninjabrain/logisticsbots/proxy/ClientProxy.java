package ninjabrain.logisticsbots.proxy;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import ninjabrain.logisticsbots.LogisticsBots;
import ninjabrain.logisticsbots.item.ItemBase;
import ninjabrain.logisticsbots.item.ModItems;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = LogisticsBots.MODID)
public class ClientProxy extends CommonProxy {

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		for (ItemBase item : ModItems.items)
			item.registerModels();
	}

	public void init(FMLInitializationEvent event) {
		System.out.println("Changing FontRenderer");
		Minecraft mc = Minecraft.getMinecraft();
		FontRenderer f = mc.fontRenderer;
		mc.fontRenderer = new CustomFontRenderer();
		if (mc.gameSettings.language != null) {
			mc.fontRenderer.setUnicodeFlag(mc.isUnicode());
			mc.fontRenderer.setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
		}
		((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(mc.fontRenderer);
	}

}

class CustomFontRenderer extends FontRenderer {

	public CustomFontRenderer() {
		super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"),
				Minecraft.getMinecraft().renderEngine, false);
	}

	@Override
	protected float renderDefaultChar(int ch, boolean italic) {
		// if (ch != 64) {
		return super.renderDefaultChar(ch, italic);
		// }
		// return 10;
	}

	@Override
	public int drawString(String text, float x, float y, int color, boolean dropShadow) {
		int emotes = 0;
		int width = 0;
//		Pattern p = Pattern.compile("@[0-9][0-9][0-9][0-9][0-9]");
//		String[] emoteless = p.split(text);
//		Matcher m = p.matcher(text);
//		while (m.find()) {
//			emotes++;
//		}
//		for (int i = 0; i < emoteless.length; i++) {
//			width += super.drawString(emoteless[i], x + width, y, color, dropShadow);
//			if (emotes > 0) {
//				width += renderEmote();
//				emotes--;
//			}
//		}
		text = text.replaceAll("§r", "");
		String[] words = text.split(" ", -1);
		for (int i = 0; i < words.length; i++) {
			if (i != 0)
				x = super.drawString(" ", x, y, color, dropShadow) - 1;
			if (words[i].equals("Kappa") && i != words.length-1) {
				x = renderEmote(x, y);
			} else {
				x = super.drawString(words[i], x, y, color, dropShadow);
			}
		}
		return (int)x;
//		return super.drawString(text, x, y, color, dropShadow);
	}

	int kappa = -1;

	protected float renderEmote(float x, float y) {
		if (kappa == -1) {
			try {
				kappa = TextureUtil.uploadTextureImage(TextureUtil.glGenTextures(),
						ImageIO.read(new URL("https://static-cdn.jtvnw.net/emoticons/v1/25/1.0")));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.posX = x;
		this.posY = y;
		GlStateManager.bindTexture(kappa);
		int l = 12;
		float w = (float) l - 0.01F;
		float dx = (8-l)*0.5f;
		GlStateManager.glBegin(5);
		GlStateManager.glTexCoord2f(0, 0);
		GlStateManager.glVertex3f(this.posX, this.posY + dx, 0);
		GlStateManager.glTexCoord2f(0, 1);
		GlStateManager.glVertex3f(this.posX, this.posY + w + dx, 0);
		GlStateManager.glTexCoord2f(1, 0);
		GlStateManager.glVertex3f(this.posX + w - 1.0F, this.posY + dx, 0);
		GlStateManager.glTexCoord2f(1, 1);
		GlStateManager.glVertex3f(this.posX + w - 1.0F, this.posY + w + dx, 0);
		GlStateManager.glEnd();
		this.posX += l;
		return (float) this.posX;
	}

}
