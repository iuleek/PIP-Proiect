package PIP_Proiect.PacmanInspiredGame;

import javax.swing.JFrame;

public class GameStart extends JFrame{
	
	 public GameStart() {
		  add(new GameLogic());
	  }

  public static void main(String[] args) {
   
	 GameStart game = new GameStart();
	 
	 game.setVisible(true);
	 game.setTitle("DeliveryDriver");
	 game.setSize(370,420);
	 game.setDefaultCloseOperation(EXIT_ON_CLOSE);
	 
	 //End the window position in the middle of the screen
	 game.setLocationRelativeTo(null);
	 
  }

}
