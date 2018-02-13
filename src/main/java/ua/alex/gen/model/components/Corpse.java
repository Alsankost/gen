package ua.alex.gen.model.components;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import ua.alex.gen.com.World;
import ua.alex.gen.model.Component;
import ua.alex.gen.model.ComponentType;
import ua.alex.gen.model.Edible;

public class Corpse implements Component, Edible{

	private double energy;
	
	public Corpse(double e) {
		energy = e;
	}
	
	@Override
	public double generateEnergy() {
		return energy;
	}

	@Override
	public ComponentType getType() {
		return ComponentType.CORPSE;
	}

	@Override
	public void update(World w) {
		
	}

	@Override
	public Paint getColor() {
		return Color.GRAY;
	}

}
