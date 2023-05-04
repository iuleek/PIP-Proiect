package PIP_Proiect.PacmanInspiredGame;

import javax.swing.JFrame;

public class GameStart extends JFrame{
	
	 public GameStart() {
		  add(new GameLogic());
		  System.out.println("Message1");
	  }

  public static void main(String[] args) {
   
	 GameStart game = new GameStart();
	 
	 game.setVisible(true);
	 game.setTitle("DeliveryDriver");
	 game.setSize(380,420);
	 game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 
	 //End the window position in the middle of the screen
	 game.setLocationRelativeTo(null);
	 
  }

}
