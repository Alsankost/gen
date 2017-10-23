package ua.alex.gen.com;

import ua.alex.gen.model.Component;

public class World implements Runnable {
	private boolean isRun = false;
	private int width, height;
	private Component[][] map;
	private Thread thread;
	private int curX, curY;
	private boolean pause = false;
	
	public void pause() {
		pause = true;
	}
	
	public void resume() {
		pause = false;
	}
	
	public boolean isPause() {
		return pause;
	}
	
	public int getCurrentX() {
		return curX;
	}
	
	public int getCurrentY() {
		return curY;
	}

	public World(int w, int h) {
		width  = w;
		height = h;
		map = new Component[width][height];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Component get(int x, int y) {
		return map[x][y];
	}
	
	public void set(Component component, int x, int y) {
		map[x][y] = component;
	}
	
	@Override
	public void run() {
		while (isRun) {
			//int count = 0;
			if (pause) {
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			
			for (int x = 0; x < width; x++) {
				this.curX = x;
				for (int y = 0; y < height; y++) {
					this.curY = y;
					if (map[x][y] != null) {
						map[x][y].update(this);
						//count++;
					}
				}
			}
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(count);
		}
	}
	
	public void start() {
		isRun = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public boolean isRun() {
		return isRun && thread.isAlive();
	}
	
	public void stop() {
		isRun = false;
	}
	
}
