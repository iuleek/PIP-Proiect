package PIP_Proiect.PacmanInspiredGame;

import java.awt.*;                      //Font, Image, Dimension

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameLogic extends JPanel implements ActionListener{
  
  private Dimension d;                                              // Height and Width of the playing field
  private final Font smallFont = new Font("Arial", Font.BOLD,14);   // To display Text in the game
  private boolean runGame = false;                                  // State of the game - running/ not running
  private boolean dying = false;                                    // Player is alive or not
  
  private final int block_size = 24;                                // How big blocks are in the game
  private final int n_blocks = 15;                                  // Number of blocks - 15 width 15 height => 255 possible positions
  private final int screen_size = block_size * n_blocks;            // 15*24 = 360
  private final int max_passers = 12;                               // Maximum number of passers
  private final int car_speed = 6;                                  // Speed of our car
  
  private int n_passers = 6;                                        // Initial number of passers
  private int lives, score;                                         // Variables for score and lives
  private int [] dx, dy;                                            // Position of the passers
  private int [] passer_x, passer_y, passer_dx, passer_dy, passerSpeed;          // To determine number and position of the passer
  
  private Image heart, passer;                                      // Change the variables to passer1/passer2 if we have multiple
  private Image up, down, left, right;                              // Images of our car according to the movement
  
  private int car_x, car_y, car_dx, car_dy;                         // Car_x, car_y -> coordinates of the car; car_dx, car_dy -> horizontal and vertical directions
  private int req_dx, req_dy;                                       // Determined in TAdapter class (extends KeyAdapter)
  
  private final int validSpeeds[] = {1, 2, 3, 6, 8};                // Array to store valid values for car's speed
  private final int maxSpeed = 6;                                   // Maximum speed
  private int currentSpeed = 3;                                     // Current speed
  private short [] screenData;                                      // Array screenData will take the all the data from level data to redraw the game
  private Timer timer;                                              // Allows animations
  
  // 0 - house obstacle; 1- left border; 2 - top border; 4 - right border
  // 9 - bottom border; 16 - road; 32 - grass; 64 - package
  private final short levelData[] = {
      19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 38,  0,  0,
      17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 36,  0,  0,
      17, 16, 16, 80, 16, 16, 16, 16, 16, 16, 16, 16, 32, 42, 46,
      41, 40, 32, 40, 40, 32, 40, 40, 32, 16, 16, 16, 36,  0,  0,
      0,   0, 37,   0, 0, 37,  0,  0, 33, 16, 16, 16, 36,  0,  0,
      0,   0, 37,   0, 0, 37,  0,  0, 33, 16, 16, 16, 32, 34, 38,
      35, 34, 32, 34, 34, 32, 34, 34, 32, 16, 16, 16, 32, 32, 36,
      17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
      17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
      17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
      41, 40, 32, 40, 40, 32, 40, 40, 32, 16, 16, 16, 32, 40, 44,
      0,   0, 37,  0,  0, 37,  0,  0, 33, 16, 16, 16, 36,  0,  0,
      0,   0, 37,  0,  0, 37,  0,  0, 33, 16, 16, 16, 36,  0,  0,
      35, 34, 32, 34, 34, 32, 34, 34, 32, 16, 16, 16, 32, 34, 38,
      25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28,
  };
  
  // Constructor to call different functions
  public GameLogic() {
    loadImages();                       // Function - Loading the Images 
    initVariables();                    // Function - Initializing the variables
    addKeyListener(new TAdapter());     // Function - Control
    setFocusable(true);                 // Function - Setting the focus of the window
    initGame();                         // Function - Starts the game
  }
  
  // Functions definition
  private void loadImages() {
    
  }
  
  private void initVariables() {
    screenData = new short[n_blocks * n_blocks];
    d = new Dimension(400,400);
    passer_x = new int [max_passers];
    passer_dx = new int [max_passers];
    passer_y = new int [max_passers];
    passer_dy = new int [max_passers];
    passerSpeed = new int [max_passers];
    dx = new int[4];
    dy = new int[4];
    
    timer = new Timer(40, this);             // Establishes how often the images are redraw
    timer.restart();
  }
  
  private void setFocusable() {
   
  }
  
  private void initGame() {
    lives = 3;
    score = 0;
    initLevel();
    n_passers = 6;
    currentSpeed = 3;
  }
  
  private void initLevel() {
    // To initialize level we copy the playfield from levelData to new array screenData
    for(int i = 0; i< n_blocks * n_blocks; i++) {
      screenData[i] = levelData[i];
    }
  }
  
/* The way that the car moves */ 
  
  private void movePacman() {
	  int pos;
	  short ch;
	  
	  if(car_x % block_size == 0 && car_y % block_size == 0) {
		  
		  pos = car_x / block_size + n_blocks * (int) (car_y / block_size);
		  ch = screenData[pos];
		  
		  /* daca masina e pe blocul cu pachetul acesta este luat si devine drum normal */
		  if((ch & 64) != 0)
		  {
			  screenData[pos] = (short) (ch&16);
		  }
		  
		  if(req_dx != 0 || req_dy != 0)
		  {
			  if(!((req_dx == -1 && req_dy == 0 && (ch & 1) !=0) 
					  || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0) 
					  || (req_dx == 0 && req_dy == -1 && (ch & 8) != 0) 
					  || (req_dx == 0 && req_dy == 1 && (ch & 2) != 0))){
				  car_dx = req_dx;
				  car_dy = req_dy;
			  }
		  }
		  
		  /*Checking for collisions with houses*/
			  
			  if ((car_x == 0 && car_y == 1 && (ch & 0) != 0)) {
		          car_x = 0;
		          car_y = 0;
			  }
		  
		  
	  }
  }
 
  
  /* CONTROLS
   * SPACE - START 
   * ESC - EXIT GAME
   * ARROWS - MOVE */
  
  class TAdapter extends KeyAdapter {
	  
    public void keyPressed(KeyEvent e)
    {
    	int key = e.getKeyCode();
    	
    	if(runGame) {
    		if(key == KeyEvent.VK_LEFT) {
    			req_dx = -1;
    			req_dy = 0;
    		}
    		else if(key == KeyEvent.VK_RIGHT) {
    			req_dx = 1;
    			req_dy = 0;
    		}
    		else if(key == KeyEvent.VK_DOWN) {
    			req_dx = 0;
    			req_dy = 1;
    		}
    		else if(key == KeyEvent.VK_UP) {
    			req_dx = 0;
    			req_dy = -1;
    		}
    		else if(key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
    			runGame = false;
    		}
    	}else {
    		if(key == KeyEvent.VK_SPACE) {
    			runGame = true;
    			initGame();
    		}
    	}
    }
  }
  
  
  
  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    
  }

}
