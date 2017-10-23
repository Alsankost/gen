package ua.alex.gen.model.components;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import ua.alex.gen.code.VM;
import ua.alex.gen.com.World;
import ua.alex.gen.model.Component;
import ua.alex.gen.model.ComponentType;
import ua.alex.gen.model.Edible;
import ua.alex.gen.utilus.SimplePoint;

public class Bot implements Component, Edible {
	public static final int RANGE = 8;
	public static final double MAX_ENERGY = 1500;
	public static double SUN_DE = 0.5;
	public static double GEO_DE = 0.7;
	public static final double EAT_DIV = 20;
	public static final double MOVE_EXP = 0.1;
	public static final double LIVE_EXP = 0.008;
	
	public static int viewMode = 0;
	
	private Paint color = Color.GREEN;
	
	public boolean cool = false;
	
	@Override
	public ComponentType getType() {
		return ComponentType.BOT;
	}

	@Override
	public void update(World w) {
		VM.call(w, this);
	}

	@Override
	public Paint getColor() {
		switch (viewMode) {
			case 0:
				return color;
			case 1:
				return (toxic)?Color.LIME:Color.BLUEVIOLET;
			case 2:
				int pc = (int) ((energy / MAX_ENERGY) * 255);
				pc = (pc > 255)?255:pc;
				return Color.rgb(pc, pc, 0);
			default:
				return Color.YELLOW;
		}
	}
	
	@Override
	public double generateEnergy() {
		return energy / EAT_DIV;
	}
	
	private double energy = 100;
	
	private Random random = new Random();
	
	private byte[] genom;
	private byte pointer = 0;
	private byte direction = 0;
	
	public boolean testClone = false;
	
	//Energy control
	public double getEnergy() {
		return energy;
	}
	
	public void setEnergy(double e) {
		energy = e;
	}
	
	//Direction and move
	public SimplePoint getDirectionPoint(World w, byte dir) {
		int x = w.getCurrentX(), y = w.getCurrentY();
		switch (dir) {
			case 0:
				x += 1;
				break;
			case 1:
				x += 1;
				y -= 1;
				break;
			case 2:
				y -= 1;
				break;
			case 3:
				x -= 1;
				y -= 1;
				break;
			case 4:
				x -= 1;
				break;
			case 5:
				x -= 1;
				y += 1;
				break;
			case 6:
				y += 1;
				break;
			case 7:
				x += 1;
				y += 1;
		}
		if (x < 0) x = w.getWidth();
		if (x > w.getWidth() - 1) x = 0;
		if (y < 0) return null;
		if (y > w.getHeight() -1) return null;
		return new SimplePoint(x,y);
	}
	
	public SimplePoint getSeePoint(World w) {
		return getDirectionPoint(w, direction);
	}
	
	public void setDirection(byte dir) {
		direction = (byte) ((dir > 7)?dir%7:dir);
	}
	
	public byte getDirection() {
		return direction;
	}
	
	public void turnLeft() {
		setDirection((byte) (this.getDirection() + 1));
	}
	
	public void turnRight() {
		setDirection((byte) (this.getDirection() - 1));
	}
	
	public void move(World w) {
		SimplePoint sp = this.getSeePoint(w);
		if (sp == null) return;
		if (w.get(sp.getX(), sp.getY()) != null) return;
		w.set(this, sp.getX(), sp.getY());
		w.set(null, w.getCurrentX(), w.getCurrentY());
		//this.setPos(w, sp.getX(), sp.getY());
		energy -= MOVE_EXP;
		/*
		switch (direction) {
			case 0:
				this.setPos((x == Start.world.length - 1)?0:x+1, y);
				break;
			case 1:
				this.setPos((x == Start.world.length - 1)?0:x+1, (y == 0)?y:y - 1);
				break;
			case 2:
				this.setPos(x, (y == 0)?y:y - 1);
				break;
			case 3:
				this.setPos((x == 0)?Start.world.length - 1:x - 1, (y == 0)?0:y - 1);
				break;
			case 4:
				this.setPos((x == 0)?Start.world.length - 1:x - 1, y);
				break;
			case 5:
				this.setPos((x == 0)?Start.world.length - 1:x - 1, (y == Start.world[x].length - 1)?y:y - 1);
				break;
			case 6:
				this.setPos(x, (y == Start.world[x].length - 1)?y:y - 1);
				break;
			case 7:
				this.setPos((x == Start.world.length - 1)?0:x+1, (y == Start.world[x].length - 1)?y:y - 1);
		}*/
	}
	/*
	public void setPos(World w, int x, int y) {
		x = (x < 0)?0:( (x > w.getWidth())?x%w.getWidth():x );
		y = (y < 0)?0:( (y > w.getHeight())?w.getHeight():y );
		
		if () return;
		this.setLayoutX(x * RANGE);
		this.setLayoutY(y * RANGE);
		Start.world[this.x][this.y] = null;
		this.x = x;
		this.y = y;
		Start.world[x][y] = this;
	}*/
	
	//Created and dead
	public void cloneThis(World w) {
		for (byte i = 0; i < 8; i++) {
			SimplePoint sp = this.getDirectionPoint(w, i);
			if (sp != null && w.get(sp.getX(), sp.getY()) == null) {
				//System.out.println("dsa");
				if (energy < Bot.MAX_ENERGY / 4) {
					suicide(w);
					return;
				}
				Bot tmp = new Bot();
				tmp.genom = genom.clone();
				if (random.nextInt(100) < 3) {
					tmp.genom[random.nextInt(genom.length)] = (byte) random.nextInt(50);
				}
				//System.out.println(sp.getX() + " " + sp.getY());
				w.set(tmp, sp.getX(), sp.getY());
				energy -= Bot.MAX_ENERGY / 4;
				return;
			}
		}
		suicide(w);
	}
	
	public void suicide(World w) {
		//System.out.println(energy);
		w.set(new Corpse(energy / 25), w.getCurrentX(), w.getCurrentY());
	}
	
	//Code
	public byte getGen(byte p) {
		p = (byte) ((p >= genom.length - 1)?p%genom.length:p);
		return genom[p];
	}
	
	public void setGen(byte p, byte v) {
		p = (byte) ((p >= genom.length - 1)?p%genom.length:p);
		genom[p] = v;
	}
	
	public byte[] getGenom() {
		return genom;
	}
	
	public byte getPointer() {
		return pointer;
	}
	
	public void setPointer(byte p) {
		pointer = (byte) ((p > genom.length - 1)?p%genom.length:p);
	}
	
	public void addPointer(byte ab) {
		this.setPointer((byte) (pointer + ab));
	}
	
	public byte getCurrentCommand() {
		return genom[pointer];
	}
	
	//Extraction
	public void generateFromSun(World w) {
		double imp = 1 - (w.getCurrentY() / (w.getHeight() / 2));
		energy += (SUN_DE / ((predator)?2:1)) * ( (imp < 0)?0:imp );
		//color = Color.GREEN;
		//circle.setFill(Color.GREEN);
	}
	
	public void generateFromGeo(World w) {
		double te = energy;
		double imp = ((w.getCurrentY() - (w.getHeight() / 2.5)) / (w.getHeight() / 1.5));
		energy += (SUN_DE / ((predator)?2:1)) * ( (imp < 0)?0:imp );
		if (energy - te > 0) color = Color.BLUE;
		toxic = false;
	}
	
	public void eat(World w) {
		SimplePoint sp = this.getSeePoint(w);
		if (sp == null) return;
		Component tmp = w.get(sp.getX(), sp.getY());
		if (tmp == null || !(tmp instanceof Bot)) return;
		if (((Bot) tmp).isToxic()) {
			suicide(w);
			return;
		}
		this.energy += ((Edible) tmp).generateEnergy();
		w.set(null, sp.getX(), sp.getY());
		predator = true;
		color = Color.RED;
	}
	
	public void recycle(World w) {
		SimplePoint sp = this.getSeePoint(w);
		if (sp == null) return;
		Component tmp = w.get(sp.getX(), sp.getY());
		if (tmp == null || !(tmp instanceof Corpse)) return;
		this.energy += ((Edible) tmp).generateEnergy();
		w.set(null, sp.getX(), sp.getY());
		toxic = false;
	}
	
	//OOO
	public boolean isFriend(World w) {
		SimplePoint sp = this.getSeePoint(w);
		if (sp == null) return false;
		Component tmp = w.get(sp.getX(), sp.getY());
		if (tmp == null || !(tmp instanceof Bot)) return false;
		//return ((Bot) tmp).genom.equals(genom);
		Bot bot = (Bot) tmp;
		int count = 0;
		for (int i = 1; i < genom.length; i++) {
			if (genom[i] != bot.genom[i]) {
				if (count > 0) return false;
				count++;
			}
		}
		return true;
	}
	
	//Properties
	private boolean toxic = false;
	
	public void setToxic(boolean t) {
		toxic = t;
	}
	
	public boolean isToxic() {
		return toxic && !predator;
	}
	
	private boolean predator = false;
	
	public boolean isPredator() {
		 return predator;
	}
	
	//Constructor
	public Bot() {
		genom = new byte[48];
		direction = (byte) random.nextInt(8);
	}

}
