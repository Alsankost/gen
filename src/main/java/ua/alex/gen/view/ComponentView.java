package ua.alex.gen.view;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import ua.alex.gen.Start;
import ua.alex.gen.com.World;
import ua.alex.gen.model.Component;
import ua.alex.gen.model.components.Bot;

public class ComponentView extends AnchorPane {
	public static final double RANGE = 8;
	
	private Circle back;
	public int x, y;
	
	private static EventHandler<MouseEvent> mouseClickListener = (event) -> {
		ComponentView tmp = (ComponentView) event.getSource();
		if (Start.world.get(tmp.x, tmp.y) == null || !(Start.world.get(tmp.x, tmp.y) instanceof Bot)) return;
		
		Bot bot = (Bot) Start.world.get(tmp.x, tmp.y);
		
		String str = "";
		
		for (int i = 0; i < bot.getGenom().length; i++) {
			str += "" + bot.getGen((byte) i) + ( ((i + 1)%4 == 0)?"\n":" " );
		}
		Start.codeTA.setText(str);
		/*Bot bot = new Bot();
		for (int i = 60; i < 64; i++) {
			bot.setGen((byte) i, (byte) Start.random.nextInt(50));
		}
		Start.world.set(bot, tmp.x, tmp.y);*/
	};
	
	public ComponentView(int x, int y) {
		this.setOnMouseClicked(mouseClickListener);
		
		this.x = x;
		this.y = y;
		this.setLayoutX(x * RANGE);
		this.setLayoutY(y * RANGE);
		this.setWidth(RANGE);
		this.setHeight(RANGE);
		
		back = new Circle(RANGE / 2, RANGE / 2, RANGE / 2);
		this.getChildren().add(back);
	}

	public void upadte(World world) {
		Component p = world.get(x, y);
		if (p == null) {
			back.setVisible(false);
			return;
		}
		if (this.equals(Start.p)) {
			Start.energy.setText(((Bot)p).getEnergy() + "");
		}
		back.setVisible(true);
		back.setFill((p.getColor() == null)?Color.BLACK:p.getColor());
	}
}
