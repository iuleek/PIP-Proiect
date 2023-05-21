package PIP_Proiect.PacmanInspiredGame;

import javax.swing.JFrame;
/**
 * 
 * @author Ana
 *
 */
/**
 * Class for instantiate the game and start it.
 */
public class GameStart extends JFrame{
	
	 public GameStart() {
		/**
		 *  Add the game logic component to the frame
		 */
		  add(new GameLogic());
	  }

  public static void main(String[] args) {
   
	  /**
	   * Create an instance of the game.
	   */
	 GameStart game = new GameStart();
	 
	 game.setVisible(true);             /** * Make the frame visible */
	 game.setTitle("DeliveryDriver");   /** * Set the title of the game window */
	 game.setSize(370,420);             /** * Set the size of the game window */
	 game.setDefaultCloseOperation(EXIT_ON_CLOSE); /** * Define the close operation for the window */
	 
	 /** 
	  * End the window position in the middle of the screen
	  */
	 game.setLocationRelativeTo(null);
	 
  }

}