package ninjabrain.logisticsbots;

import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {

//	@SubscribeEvent
//	public void lootLoad(LootTableLoadEvent evt) {
//	    System.out.println(evt.getName().toString());
//		if (evt.getName().toString().equals("minecraft:entities/blaze")) {
//	        evt.getTable().removePool("main");
//	        //LootEntry blazeLoot = new LootEntryItem(Item.getByNameOrId("minecraft:blazerod"), weightIn, qualityIn, functionsIn, conditionsIn, entryName)
//	        //evt.getTable().addPool(new LootPool(, poolConditionsIn, rollsIn, bonusRollsIn, "main"));
//	    }
//	}
	
	@SubscribeEvent
	public void messageReceived(ServerChatEvent event) {
//		System.out.println("MESSAGE. From: " + event.getUsername() + ", msg: " + event.getMessage());
//		String message = event.getComponent().getFormattedText();
//		message = message.replaceAll("Kappa", "@00000");
//		event.setComponent(new TextComponentString(message));
//		event.setCanceled(true);

        
	}

}
