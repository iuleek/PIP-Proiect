package PIP_Proiect.PacmanInspiredGame;

import java.awt.*;                      //Font, Image, Dimension

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class GameLogic extends JPanel implements ActionListener{
  
    private Dimension d;                                              // Height and Width of the playing field
	private final Font smallFont = new Font("Arial", Font.BOLD,14);   // To display Text in the game
	private boolean runGame = false;                                  // State of the game - running/ not running

	private boolean dying = false;                                    // Player is alive or not
	private boolean carrying = false;                                 // Variable that it's true if the car is not in delivering state(not carrying a package) and false otherwise

	private final int block_size = 24;                                // How big blocks are in the game
	private final int n_blocks = 15;                                  // Number of blocks - 15 width 15 height => 255 possible positions
	private final int screen_size = block_size * n_blocks;            // 15*24 = 360
	private final int max_passers = 12;                               // Maximum number of passers
	private final int car_speed = 6;                                  // Speed of our car

	private int n_passers = 3;                                        // Initial number of passers
	private int lives, score;                                         // Variables for score and lives
	private int [] dx, dy;                                            // Position of the passers
	private int [] passer_x, passer_y, passer_dx, passer_dy, passerSpeed;          // To determine number and position of the passer

	private Image heart, passer, pack;                                // Change the variables to passer1/passer2 if we have multiple
	private Image up, down, left, right;                              // Images of our car according to the movement
	private Image house, grass, road;

	private int car_x, car_y, car_dx, car_dy;                         // Car_x, car_y -> coordinates of the car; car_dx, car_dy -> horizontal and vertical directions
	private int req_dx, req_dy;                                       // Determined in TAdapter class (extends KeyAdapter)

	
	// 0 - house obstacle; 1- left border; 2 - top border; 4 - right border
	// 8 - bottom border; 16 - road; 32 - grass; 64 - package; 128 - delivery point
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
			35, 34, 32, 34, 34, 32, 34, 34, 32, 16, 16, 144, 32, 34, 38,
			25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28,
	};
	
    private final int validSpeeds[] = {1, 2, 3, 4, 6, 8};
    private final int maxSpeed = 6;

    private int currentSpeed = 3;
    private short[] screenData;
    private Timer timer;


	// Constructor to call different functions
	public GameLogic() {
		loadImages();                       // Function - Loading the Images 
		initVariables();                    // Function - Initializing the variables
		addKeyListener(new TAdapter());     // Function - Control
		setFocusable(true);                 // Function - Setting the focus of the window
		initGame();                         // Function - Starts the game
	}

	// Loading the images used in the game
	private void loadImages() {
		try {
		    File up1 = new File("D:\\AC facultate\\anu 3 sem 2\\PIP-pr\\Proiect-final\\PIP-Proiect\\src\\main\\java\\images\\car_up.png");
		    File left1 = new File("D:\\AC facultate\\anu 3 sem 2\\PIP-pr\\Proiect-final\\PIP-Proiect\\src\\main\\java\\images\\car_left.png");
		    File right1 = new File("D:\\AC facultate\\anu 3 sem 2\\PIP-pr\\Proiect-final\\PIP-Proiect\\src\\main\\java\\images\\car_right.png");
		    File passer1 = new File("D:\\AC facultate\\anu 3 sem 2\\PIP-pr\\Proiect-final\\PIP-Proiect\\src\\main\\java\\images\\p_front.png");
		    File down1 = new File("D:\\AC facultate\\anu 3 sem 2\\PIP-pr\\Proiect-final\\PIP-Proiect\\src\\main\\java\\images\\car_down.png");
		    
		    File pack1 = new File("D:\\AC facultate\\anu 3 sem 2\\PIP-pr\\Proiect-final\\PIP-Proiect\\src\\main\\java\\images\\pack.png");
		    File house1 = new File("D:\\AC facultate\\anu 3 sem 2\\PIP-pr\\Proiect-final\\PIP-Proiect\\src\\main\\java\\images\\house1.png");
		    File road1 = new File("D:\\AC facultate\\anu 3 sem 2\\PIP-pr\\Proiect-final\\PIP-Proiect\\src\\main\\java\\images\\road.png");
		    File grass1 = new File("D:\\AC facultate\\anu 3 sem 2\\PIP-pr\\Proiect-final\\PIP-Proiect\\src\\main\\java\\images\\grass1.png");
		    
		    up = ImageIO.read(up1);
		    down = ImageIO.read(down1);
		    left = ImageIO.read(left1);
		    right = ImageIO.read(right1);
		    passer = ImageIO.read(passer1);
		    pack = ImageIO.read(pack1);
		    house = ImageIO.read(house1);
		    road = ImageIO.read(road1);
		    grass = ImageIO.read(grass1);
		} catch (Exception e) {
		    System.out.println("Failed to load image");
		    e.printStackTrace();
		}
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
		timer.start();
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

		continueLevel();
	}

	
	private void playGame(Graphics2D g2d) {

	    if(dying)
	    {
	        death();

	    }else {

	        moveCar();
	        drawCar(g2d);
	        //movePassers(g2d);
	        checkMaze();

	    }
	}
	
	private void drawMaze(Graphics2D g2d) {
	    for (int i = 0; i < levelData.length; i++) {
	        int x = (i % n_blocks) * block_size;
	        int y = (i / n_blocks) * block_size;

	        if (levelData[i] == 0) {
	        	g2d.setColor(new Color(92, 64, 51));
	        	g2d.fillRect(x,y, 24,24);
	            g2d.drawImage(house, x, y, this);
	        } else if (levelData[i] == 19 || levelData[i] == 18 || levelData[i] == 17
	                || levelData[i] == 16 || levelData[i] == 24
	                || levelData[i] == 25 || levelData[i] == 28
	                || levelData[i] == 20) {
	            g2d.setColor(new Color (128, 128, 128));
	            g2d.fillRect(x,y, 24,24);
	        } else if (levelData[i] == 32 || levelData[i] == 41 || levelData[i] == 40
	                || levelData[i] == 33 || levelData[i] == 37
	                || levelData[i] == 34 || levelData[i] == 35
	                || levelData[i] == 38 || levelData[i] == 36
	                || levelData[i] == 42 || levelData[i] == 46
	                || levelData[i] == 44) {
	        	g2d.setColor(new Color(0, 128, 0));
	        	g2d.fillRect(x,y, 24,24);
	            g2d.drawImage(grass, x, y, this);
	        } else if (levelData[i] == 80) {
	        	g2d.setColor(Color.gray);
	            g2d.fillRect(x,y, 24,24);
	            g2d.drawImage(pack, x, y, this);
	        }else if(levelData[i] == 128){
	        	g2d.setColor(Color.green);
	            g2d.fillRect(x,y, 24,24);
	        }
	    }
	}

	
	// The intro screen for the game - first thing the player sees
	private void showIntroScreen(Graphics2D g2d) {
		String start = "Press SPACE to start";                      // Text for the intro screen
		g2d.setColor(Color.red);                                    // Color for the text
		g2d.drawString(start, (screen_size)/4, 150);                // Positioning the text

	}
	
	// Display of the score and lives 
	private void drawScore(Graphics2D g) {
		g.setFont(smallFont);
		g.setColor(new Color(5, 181, 79));
		String s = "Score: " + score;
		g.drawString(s, screen_size / 2 + 96, screen_size + 16);


		for (int i = 0; i < lives; i++) {                           // Display the number of hearts(lives)
			g.drawImage(heart, i * 28 + 8, screen_size + 1, this);
		}
	}
	
	//Drawing the car we are playing with
	private void drawCar(Graphics2D g2d) {

		if (req_dx == -1) {
			g2d.drawImage(left, car_x + 1, car_y + 1, this);
		} else if (req_dx == 1) {
			g2d.drawImage(right, car_x + 1, car_y + 1, this);
		} else if (req_dy == -1) {
			g2d.drawImage(up, car_x + 1, car_y + 1, this);
		} else {
			g2d.drawImage(down, car_x + 1, car_y + 1, this);
		}
	}

	
	
	//Drawing the passers that are obstacles
	private void drawPasser(Graphics2D g2d, int x, int y) {
		g2d.drawImage(passer, x, y, this);
	}
	
	
	// Checking if there are any packages left to deliver by our driver - 64 stands for the package existing
	private void checkMaze() {

		boolean allPackagesDelivered = true;                        // Set a boolean variable allPackagesDelivered to true,
		// indicating that all packages have been delivered. 

		for(int i = 0; i < screenData.length; i++) {               // Iterate over all the elements in the screenData array
			if ((screenData[i] & 64) != 0) {                          // and check if any element contains a package
				allPackagesDelivered = false;                         // If we find any packages that have not been picked up yet 
				break;                                                // We set allPackagesDelivered to false and break out of the loop.
			}
			if (carrying == true)
			{
				allPackagesDelivered = false;     
			}
		}

		if (allPackagesDelivered) {                                 //  If allPackagesDelivered is still true, we can move to the next level.

			score += 50;                                            // Increasing the score by 50

			if (n_passers < max_passers) {
				n_passers++;
			}

			if (currentSpeed < maxSpeed) {
				currentSpeed++;
			}

			initLevel();                                            // For now we restart the game when we completed delivering packages and just increase the number of passers and their speed
		}
	}

	// Establishing the number of lives and according to that either stop the game or continue
	private void death() {

		lives--;

		if (lives == 0) {
			runGame = false;
		}

		continueLevel();
	}

	//passers movement function
		private void movePassers(Graphics2D g2d) {
			int pos=0;
			int count;

			//setting the position of all 6 passers in block lines
			for (int i = 0; i<n_passers; i++) {

				// the passer move one square and the decides if want to change direction after finish moving in square
				if(passer_x[i] % block_size == 0 && passer_y[i]%block_size == 0)
					pos = passer_x[i]  / block_size + n_blocks * (int) (passer_y[i] / block_size);
				count = 0;

				//determining how the passer can move, the passer can't go through houses
				if ((screenData[pos] & 1) == 0 && passer_dx[i] != 1) {
					dx[count] = -1;
					dy[count] = 0;
					count++;
				}

				if ((screenData[pos] & 2) == 0 && passer_dy[i] != 1) {
					dx[count] = 0;
					dy[count] = -1;
					count++;
				}

				if ((screenData[pos] & 4) == 0 && passer_dx[i] != -1) {
					dx[count] = 1;
					dy[count] = 0;
					count++;
				}

				if ((screenData[pos] & 8) == 0 && passer_dy[i] != -1) {
					dx[count] = 0;
					dy[count] = 1;
					count++;
				}

				//
				if (count == 0) {
					//determining where the passer is located in our square
					if ((screenData[pos] & 16) == 16) {
						passer_dx[i] = 0;
						passer_dy[i] = 0;
					} else {
						passer_dx[i] = -passer_dx[i];
						passer_dy[i] = -passer_dy[i];
					}

				} else {

					count = (int) (Math.random() * count);

					if (count > 3) {
						count = 3;
					}

					passer_dx[i] = dx[count];
					passer_dy[i] = dy[count];
				}


				passer_x[i] = passer_x[i] + (passer_dx[i] * passerSpeed[i]);
				passer_y[i] = passer_y[i] + (passer_dy[i] * passerSpeed[i]);
				drawPasser(g2d, passer_x[i] + 1, passer_y[i] + 1);

				if (car_x > (passer_x[i] - 12) && car_x < (passer_x[i] + 12)
						&& car_y > (passer_y[i] - 12) && car_y < (passer_y[i] + 12)
						&& runGame) {

					dying = true;
				}
			}
		}

	
	/* The way that the car moves */ 
	private void moveCar() {
		int pos;
		short ch;

		if(car_x % block_size == 0 && car_y % block_size == 0) {

			pos = car_x / block_size + n_blocks * (int) (car_y / block_size);
			ch = screenData[pos];

			/* If the car is on the block with a package, the package disappears */
			if((ch & 64) != 0)
			{
				levelData[pos] = (short)(ch&16);
				carrying = true;                   // The driver picked up the package and has to deliver it
			}
			if((ch & 128) != 0 && carrying == true)
			{
				levelData[pos] = (short)(ch&16);
				carrying = false;                   // The driver delivered the package
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
			/*Checking for collisions with borders*/
			if (req_dx == -1 && car_x <= 0 
					|| req_dx == 1 && car_x >= n_blocks * block_size 
					|| req_dy == -1 && car_y <= 0 
					|| req_dy == 1 && car_y >= n_blocks * block_size ) {
				car_dx = 0;
				car_dy = 0;
			}


			/*Checking for collisions with houses*/

			if(req_dx == -1 && req_dy == 0 && (ch&32)!=0) {
				req_dx = 0;
				req_dy = 0;	
			}
		}

	}


	private void continueLevel() {

		int dy = 1;
		int random;

		for(int i = 0; i < n_passers; i++) {

			passer_x[i] = 4 * block_size;
			passer_y[i] = 9 * block_size;

			passer_dy[i] = dy;
			passer_dx[i] = 0;

			dy = -dy;

			random = (int) (Math.random() * (currentSpeed + 1));

			if (random > currentSpeed) {
				random = currentSpeed;
			}

			passerSpeed[i] = validSpeeds[random];
		}

		car_x = 10 * block_size;
		car_y = 13 * block_size;

		car_dx = 0; 				//reset direction move
		car_dy = 0;

		req_dx = 0;					//reset direction controls
		req_dy = 0;

		dying = false;

	}

	//Putting the graphics together
		public void paint(Graphics g)
		{
		    super.paintComponent(g);

		    Graphics2D g2d = (Graphics2D) g;

		    g2d.setColor(Color.darkGray);
		    g2d.fillRect(0, 0, d.width, d.height);

		    drawMaze(g2d);
		    drawScore(g2d);

		    if (runGame) {
		        playGame(g2d);
		    } else {
		        showIntroScreen(g2d);
		    }
		    Toolkit.getDefaultToolkit().sync();
		    g2d.dispose();

		}


		/* CONTROLS
		 * SPACE - START 
		 * ESC - EXIT GAME
		 * ARROWS - MOVE */
	class TAdapter extends KeyAdapter {

		@Override
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

			car_x += req_dx * block_size;
			car_y += req_dy * block_size;

		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		repaint();  //calls the paint method
	}
}
