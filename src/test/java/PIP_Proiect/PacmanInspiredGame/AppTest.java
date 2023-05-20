package PIP_Proiect.PacmanInspiredGame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.mockito.Mockito;

import java.awt.Color;
import java.awt.Graphics2D;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testCollisionCar()
    {
    	// Create an instance of the class that contains the moveCar() method
        GameLogic carInstance = new GameLogic();

        // Set up initial values for car position and request
        carInstance.car_x = 10;
        carInstance.car_y = 10;
        carInstance.req_dx = 1; // Moving right
        carInstance.req_dy = 0; // Not moving vertically

        // Set up a collision scenario in levelData
        int newPos = (carInstance.car_x + carInstance.req_dx * carInstance.block_size) / carInstance.block_size + carInstance.n_blocks * ((carInstance.car_y + carInstance.req_dy * carInstance.block_size) / carInstance.block_size);
        carInstance.levelData[newPos] = 0; // Make the new position a house, causing a collision

        // Call the moveCar() method
        carInstance.moveCar();

        // Assert that the car position remains the same after a collision
        assertEquals(10, carInstance.car_x);
        assertEquals(10, carInstance.car_y);
    }
    @Test
    public void testCollisionPassers() {
        // Create an instance of the class that contains the movePassers() method
        GameLogic passersInstance = new GameLogic();
        
        passersInstance.n_passers = 6; // Assuming there are 6 passers
        passersInstance.passer_x = new int[]{10, 20, 30, 40, 50, 60}; // Sample passer positions
        passersInstance.passer_y = new int[]{10, 20, 30, 40, 50, 60};
        passersInstance.passer_dx = new int[]{0, 0, 0, 0, 0, 0}; // Sample passer directions
        passersInstance.passer_dy = new int[]{0, 0, 0, 0, 0, 0};
        passersInstance.passerSpeed = new int[]{1, 1, 1, 1, 1, 1}; // Sample passer speeds
        passersInstance.car_x = 15; // Car position near a passer
        passersInstance.car_y = 15;
        passersInstance.runGame = true;

        // Create a mock Graphics2D object
        Graphics2D g2d = mock(Graphics2D.class);

        // Call the movePassers() method with the mock Graphics2D object
        passersInstance.movePassers(g2d);

        // Assert that a collision occurred (dying flag is set to true)
        assertTrue(passersInstance.dying);
    }
    
    @Test
    public void testDrawScore() {
        // Create a mock Graphics2D object
        Graphics2D g2d = Mockito.mock(Graphics2D.class);

        // Create an instance of GameLogic
        GameLogic gameLogic = new GameLogic();

        // Call the drawScore method
        gameLogic.drawScore(g2d);

        // Verify that the setFont, setColor, drawString, and drawImage methods are called with the expected arguments
        verify(g2d).setFont(gameLogic.smallFont);
        verify(g2d).setColor(new Color(221, 160, 221));
        verify(g2d).drawString("Score: " + gameLogic.score, gameLogic.screen_size / 2 + 96, gameLogic.screen_size + 16);

        for (int i = 0; i < gameLogic.lives; i++) {
            verify(g2d).drawImage(gameLogic.heart, i * 28 + 8, gameLogic.screen_size + 1, gameLogic);
        }

        if (gameLogic.display) {
            verify(g2d).setColor(Color.pink);
            verify(g2d).drawString("All packages delivered!", (gameLogic.screen_size) / 4, 40);
        }
    }
    
    
    
}
