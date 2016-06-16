import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class KeyMovementListener extends KeyAdapter{

	@Override
	public void keyPressed(KeyEvent e) {
	    int keyCode = e.getKeyCode();
	    switch( keyCode ) { 
	        case KeyEvent.VK_UP:
	            Game.moveDir = 1;
	            break;
	        case KeyEvent.VK_DOWN:
	        	Game.moveDir = 3;
	            break;
	        case KeyEvent.VK_LEFT:
	        	Game.moveDir = 2;
	            break;
	        case KeyEvent.VK_RIGHT :
	        	Game.moveDir = 0;
	            break;
	     }
	} 

}
