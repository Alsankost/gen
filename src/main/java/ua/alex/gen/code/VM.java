package ua.alex.gen.code;

import java.util.HashMap;
import java.util.Map;
import ua.alex.gen.com.Command;
import ua.alex.gen.com.World;
import ua.alex.gen.model.Component;
import ua.alex.gen.model.components.Bot;
import ua.alex.gen.model.components.Corpse;
import ua.alex.gen.utilus.SimplePoint;

public abstract class VM {
	private static Map<Byte, Command> commands;
	
	//private static Random random = new Random();
	
	static {
		commands = new HashMap<Byte, Command>();
		
		//Move
		commands.put((byte) 1, (w, b) -> { b.move(w); });
		
		//Turn Left
		commands.put((byte) 2, (w, b) -> { b.turnLeft(); });
		//Turn Right
		commands.put((byte) 3, (w, b) -> { b.turnRight(); });
		
		//Eat
		commands.put((byte) 4, (w, b) -> { b.eat(w); });
		
		//Clone
		commands.put((byte) 5, (w, b) -> { b.cloneThis(w); });
		
		//Is Friend
		commands.put((byte) 6, (w, b) -> {
			if (b.isFriend(w)) {
				b.addPointer(b.getGen((byte) (b.getPointer() + 1)));
			}
			else {
				b.addPointer((byte) 1);
			}
		});
		
		//Is not Friend
		commands.put((byte) 7, (w, b) -> {
			if (!b.isFriend(w)) {
				b.addPointer(b.getGen((byte) (b.getPointer() + 1)));
			}
			else {
				b.addPointer((byte) 1);
			}
		});
		
		//Move back
		commands.put((byte) 8, (w, b) -> {
			for (int i = 0; i < 2; i++) {
				b.turnLeft();
			}
			b.move(w);
			for (int i = 0; i < 2; i++) {
				b.turnRight();
			}
		});
		
		//Move left
		commands.put((byte) 9, (w, b) -> {
			for (int i = 0; i < 2; i++) {
				b.turnLeft();
			}
			b.move(w);
			for (int i = 0; i < 2; i++) {
				b.turnRight();
			}
		});
		
		//Move right
		commands.put((byte) 10, (w, b) -> {
			for (int i = 0; i < 2; i++) {
				b.turnRight();
			}
			b.move(w);
			for (int i = 0; i < 2; i++) {
				b.turnLeft();
			}
		});
		
		//See
		commands.put((byte) 11, (w, b) -> {
			SimplePoint sp = b.getSeePoint(w);
			if (sp == null) return;
			if (w.get(sp.getX(), sp.getY()) == null) return;
			Component tmp = w.get(sp.getX(), sp.getY());
			
			if (tmp instanceof Bot) {
				if (b.isFriend(w)) b.addPointer((byte) 1);
				return;
			}
			
			if (tmp instanceof Corpse) {
				b.addPointer((byte) 2);
				return;
			}
			b.addPointer((byte) 3);
		});
		
		//Recycle
		commands.put((byte) 12, (w, b) -> { b.recycle(w); });
		
		//Check energy
		commands.put((byte) 13, (w, b) -> {
			double energy = b.getEnergy();
			if (energy > Bot.MAX_ENERGY / 2) {
				return;
			}
			if (energy < Bot.MAX_ENERGY / 2) {
				b.addPointer((byte) 1);
				return;
			}
			if (energy > Bot.MAX_ENERGY / 4) {
				b.addPointer((byte) 2);
				return;
			}
			if (energy < Bot.MAX_ENERGY / 4) {
				b.addPointer((byte) 3);
				return;
			}
		});
		
		//Toxic
		commands.put((byte) 14, (w, b) -> { b.setToxic(b.getGen((byte) (b.getCurrentCommand() + 1)) % 3 == 0); });
		
		//Is Toxic
		commands.put((byte) 15, (w, b) -> {
			SimplePoint sp = b.getSeePoint(w);
			if (sp == null) return;
			if (w.get(sp.getX(), sp.getY()) == null || w.get(sp.getX(), sp.getY()).getClass().getName() != "Bot") return;
			Bot bot = (Bot) w.get(sp.getX(), sp.getY());
			if (bot.isToxic()) {
				bot.addPointer((byte) 1);
			}
		});
		
		//Geo
		commands.put((byte) 16, (w, b) -> { b.generateFromGeo(w); });
		
		//Sun
		commands.put((byte) 17, (w, b) -> { b.generateFromSun(w); });
	};
	
	public static void call(World w, Bot bot) {
		//System.out.println("Command: " + bot.getCurrentCommand());
		//System.out.println(bot.getEnergy());
		Command tmp = commands.get(bot.getCurrentCommand());
		if (tmp != null) {
			tmp.call(w, bot);
		}
		else {
			bot.addPointer(bot.getCurrentCommand());
		}
		
		if (bot.getEnergy() >= Bot.MAX_ENERGY) {
			//System.out.println("sad");
			bot.cloneThis(w);
		}
		
		if (bot.getEnergy() <= 0) {
			bot.suicide(w);
		}
		
		bot.addPointer((byte) 1);
		bot.setEnergy(bot.getEnergy() - Bot.LIVE_EXP);
	}
}
