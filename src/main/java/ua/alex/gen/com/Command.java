package ua.alex.gen.com;

import ua.alex.gen.model.components.Bot;

public interface Command {
	public void call(World world, Bot bot);
}
