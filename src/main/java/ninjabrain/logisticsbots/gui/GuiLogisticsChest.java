package ninjabrain.logisticsbots.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.ItemStackHandler;
import ninjabrain.logisticsbots.lib.LibResources;

public class GuiLogisticsChest extends GuiContainer {
	
	InventoryPlayer playerInv;
	String guiName;
	
	public GuiLogisticsChest(InventoryPlayer playerInv, ItemStackHandler modInv, String guiName) {
		super(new ContainerMod(playerInv, modInv));
		this.playerInv = playerInv;
		this.guiName = guiName;
		int inventoryRows = modInv.getSlots() / 9;
		this.ySize = 114 + inventoryRows * 18;
	}
	
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString(guiName, 8, 6, 4210752);
        this.fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(LibResources.LOGISTICS_CHEST_BG_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
	}
	
	
	
}
