import javax.swing.*;

import java.util.Arrays;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Game {
	
    static int width= 20;
    static int height= 20;
    static int side = 20;
    static int speedWait  = 200;
    static final int SPEED_DECREMENT = 10;
    static final int MIN_SPEED  = 50;
    static final int EYEBROW_HANDICAP_SPEEDBOOST  = 200;
	static int score = 0;
	static int moveDir = 0;
	static int[] eyebrowReadings =  new int[3];
	static boolean dead = false;
	static final int CAMERA_REFRESH_SPEED = 2;
	
	public static void start(int type, int w, int h, int s) { 
		width = w; height = h; side =s;
		speedWait = 200; score = 0; moveDir = 0; dead = false; Arrays.fill(eyebrowReadings,0);//reset static vars
		JFrame frame = new JFrame();
		frame.setTitle("Snake");
		frame.setSize(27+width * side, 100+height * side);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		switch (type) {
		case 0:
			frame.addKeyListener(new KeyMovementListener()); 
			break;
		case 4:
			EyebrowControls.activate(false,CAMERA_REFRESH_SPEED); 
			speedWait += EYEBROW_HANDICAP_SPEEDBOOST;
			break;	
		}
		JPanel content = new JPanel();
		BoxLayout layout = new BoxLayout(content, BoxLayout.Y_AXIS);
		content.setLayout(layout);
		JLabel scoreText = new JLabel(""+score);
		content.add(scoreText);
		scoreText.setFont(new Font(scoreText.getFont().getName(), Font.PLAIN, 30));
		scoreText.setAlignmentX(Component.CENTER_ALIGNMENT);
		Grid grid = new Grid(width,height,side);
		content.add(grid);
		frame.add(content);
		frame.setVisible(true);
		Timer t = new Timer(speedWait, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (dead) {
					JOptionPane.showMessageDialog(frame,
							"You Died.\nTotal Score: " + score,
							"Game Over",
							JOptionPane.PLAIN_MESSAGE);
					frame.dispose(); 
					((Timer)e.getSource()).stop();
					return;
				}
				if (type == 4) {
					//System.out.println(Arrays.toString(eyebrowReadings));
					if (eyebrowReadings[1] < eyebrowReadings[0]) {
						if (eyebrowReadings[0] < eyebrowReadings[2]) {
							moveDir ++; //System.out.println("turned left");	
						} else {
							moveDir --; //System.out.println("turned right");	
						}
					} else if (eyebrowReadings[1] < eyebrowReadings[2]) {
						moveDir ++; //System.out.println("turned left");	
					}
					if (moveDir ==-1) moveDir=3;
					if (moveDir ==4) moveDir=0;
					Arrays.fill(eyebrowReadings,0);
				}
				grid.moveSnake(moveDir);
				scoreText.setText(""+score);
			}
		});
		t.start();
	}
}




