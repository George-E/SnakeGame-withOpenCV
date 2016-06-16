import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Arrays;

import javax.swing.JPanel;

class Grid extends JPanel {

	int[][] index;
	int w,h,s;
	Point tail;
	Point head;
	
	public Grid(int w, int h, int s) {
		index = new int[w][h];
		for (int[] line : index)
			Arrays.fill(line, -1);
		this.w = w;
		this.h = h;
		this.s = s;
		setSize(20+w * s, 20+h * s);
		int middle = h/2;
		tail = new Point(0, middle);
		head = new Point(3, middle);
		index[0][middle] = 0; index[1][middle] = 0; index[2][middle] = 0;
		index[3][middle] = 2;
		setFood();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int x=0; x<w; x++) {
			for (int y=0; y<h; y++) {
				switch (index[x][y]){
				case 0:case 1:case 2:case 3:
					g.setColor(Color.RED);
					g.fillRect(10+x*s, 10+y*s, s, s);
					g.setColor(Color.BLACK);
					g.drawRect(11+x*s, 11+y*s, s-2, s-2);
					break;
				case 10:
					g.setColor(Color.GREEN);
					g.fillRect(10+x*s, 10+y*s, s, s);
					g.setColor(Color.BLACK);
					g.drawRect(11+x*s, 11+y*s, s-2, s-2);
					break;
				}
			}
		}
		g.setColor(Color.GRAY);
		g.drawRect(10, 10, s*w, s*h);

		int bottom =  s*h;
		int right =  s*w;
		
		for (int i = 10; i <= right; i += s) {
			g.drawLine(i, 10, i, 8+bottom);
		}
		for (int i = 10; i <= bottom; i += s) {
			g.drawLine(10, i, 8+right, i);
		}
	}

	public void setFood() {
		int x,y;
		do {
			x = (int)(Math.random()*w);
			y = (int)(Math.random()*h);
		} while (index[x][y] != -1);
		index[x][y] = 10;
	}
	
	public void moveSnake(int dir) {
		if (dir == index[head.x][head.y])
			dir -= (dir>1)?2:-2;
		index[head.x][head.y] = dir;
		int newHead = 0;
		switch (dir) {
		case 0:
			head.x ++; newHead = 2; break;
		case 1:
			head.y --; newHead = 3; break;
		case 2:
			head.x --; newHead = 0; break;
		case 3:
			head.y ++; newHead = 1; break;
		}
		
		if (head.x<w && head.y<h && 0<=head.x && head.y>= 0 )
		{
			if (index[head.x][head.y] == 10) { //food
				Game.score++;
				setFood();
				if (Game.speedWait>Game.MIN_SPEED) Game.speedWait-=Game.SPEED_DECREMENT;
			} else {
				int oldTail = index[tail.x][tail.y];
				index[tail.x][tail.y] = -1;
				switch (oldTail) {
				case 0:
					tail.x ++; break;
				case 1:
					tail.y --; break;
				case 2:
					tail.x --; break;
				case 3:
					tail.y ++; break;
				}

				if (index[head.x][head.y] != -1) {//not empty
					Game.dead = true;
					//die
				}
			}
			
			index[head.x][head.y] = newHead;
		}
		else //outside grid
		{
			Game.dead = true;
			//die
		}
		
		
		repaint();
	}

}