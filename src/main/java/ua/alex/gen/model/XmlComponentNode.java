package ua.alex.gen.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import javafx.scene.paint.Paint;
import ua.alex.gen.model.components.Bot;
import ua.alex.gen.model.components.Corpse;
import ua.alex.gen.utilus.SimplePoint;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "node")
public class XmlComponentNode {
	@XmlAttribute
	private ComponentType type;
	
	@XmlAttribute
	private int x,y;
	
	@XmlAttribute
	private double energy;
	
	@XmlElement
	private byte[] genom;
	
	@XmlAttribute
	private byte pointer;
	
	@XmlAttribute
	private byte direction;
	
	@XmlAttribute
	private boolean toxic;
	
	@XmlAttribute
	private boolean predator;
	
	public XmlComponentNode() {
		
	}

	public XmlComponentNode(Component component, int x, int y) {
		this.x = x;
		this.y = y;
		
		this.type = component.getType();
		
		if (type == ComponentType.BOT) {
			Bot tmp = (Bot) component;
			
			energy    = tmp.getEnergy();
			pointer   = tmp.getPointer();
			direction = tmp.getDirection();
			toxic     = tmp.isToxic();
			predator  = tmp.isPredator();
			genom     = tmp.getGenom();
		}
		
		if (type == ComponentType.CORPSE) {
			Corpse tmp = (Corpse) component;
			
			energy = tmp.generateEnergy();
		}
	}
	
	public Component create() {
		switch (type) {
			case CORPSE:
				return new Corpse(energy);
			case BOT:
				Bot tmp = new Bot();
				tmp.setEnergy(energy);
				tmp.setPointer(pointer);
				tmp.setDirection(direction);
				tmp.setToxic(toxic);
				tmp.setPredator(predator);
				
				for (byte i = 0; i < genom.length; i++) {
					tmp.setGen(i, genom[i]);
				}
				
				return tmp; 
		};
		
		return null;
	}
	
	public SimplePoint getPostition() {
		return new SimplePoint(x, y);
	}
}
