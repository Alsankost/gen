package ua.alex.gen.model;

import javafx.scene.paint.Paint;
import ua.alex.gen.com.World;

public interface Component {
	public ComponentType getType();
	public void update(World w);
	public Paint getColor();
}
