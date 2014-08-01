package me.kingingo.kcore.Kit.Perks.Event;

import lombok.Getter;
import me.kingingo.kcore.Kit.Kit;
import me.kingingo.kcore.Kit.Perk;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KitHasPlayerEvent extends Event implements Cancellable{
		private HandlerList handlers = new HandlerList();
		private boolean cancel=false;
		@Getter
		private Perk perk;
		@Getter
		Player player;
		@Getter
		Kit kit;
		
		public KitHasPlayerEvent(Perk perk,Player player,Kit kit){
			this.player=player;
			this.kit=kit;
			this.perk=perk;
		}
		
		@Override
		public HandlerList getHandlers() {
			return handlers;
		}
		
		public HandlerList getHandlerList() {
	        return handlers;
	    }

		@Override
		public boolean isCancelled() {
			return cancel;
		}

		@Override
		public void setCancelled(boolean arg0) {
			cancel=arg0;
		}

	}