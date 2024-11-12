package a;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SOSGameTests {
	private SOSGame game;
	private SOSGUI gui;
	
	@BeforeEach
	public void setUp() {
		//Choosing game size
		game = new SOSGame(3,true,true); 
		
	}
	
	//checks to see if blue's the winner of a simple game by making the row SOS across row 0 with blue being the winning "S"
	@Test
	public void checkBlueSimpleWin() {
		game.makeMove(0, 0, 0);
		game.makeMove(0, 1, 1);
		game.makeMove(0, 2, 0);
		Assertions.assertEquals(game.getGameState(), SOSGame.GameState.BLUE_WON);
	}
	
	//checks to see if red's the winner of a simple game by making the row SOS across row 0 by making red the winning "S"
	@Test
	public void checkRedSimpleWin() {
		game.makeMove(2, 2, 0);
		game.makeMove(0, 0, 0);
		game.makeMove(0, 1, 1);
		game.makeMove(0, 2, 0);
		Assertions.assertEquals(game.getGameState(), SOSGame.GameState.RED_WON);
	}
	
	//checks to see if the simple game is a draw by making all the cells of the 3x3 "S" therefore causing a draw
	@Test
	public void checkSimpleDraw() {
		game.makeMove(0, 0, 0);
		game.makeMove(0, 1, 0);
		game.makeMove(0, 2, 0);
		game.makeMove(1, 0, 0);
		game.makeMove(1, 1, 0);
		game.makeMove(1, 2, 0);
		game.makeMove(2, 0, 0);
		game.makeMove(2, 1, 0);
		game.makeMove(2, 2, 0);
		Assertions.assertEquals(game.getGameState(), SOSGame.GameState.DRAW);
	}
	
	//checks to see if blue's the winner of a general game by having blue be the last "S" of the "SOS" created on the first row
	//making all the other cells be an "S" causing blue to be the only one having an SOS in the game
	@Test
	public void checkBlueGenWin() {
		game.setCurrentGameType(SOSGame.GameType.General);
		game.makeMove(0, 0, 0);
		game.makeMove(0, 1, 1);
		game.makeMove(0, 2, 0);
		game.makeMove(1, 0, 0);
		game.makeMove(1, 1, 0);
		game.makeMove(1, 2, 0);
		game.makeMove(2, 0, 0);
		game.makeMove(2, 1, 0);
		game.makeMove(2, 2, 0);
		Assertions.assertEquals(game.getGameState(), SOSGame.GameState.BLUE_WON);
	}
	
	//checks to see if red's the winner of a general game by having blue be the last "S" of the "SOS" created on the first row
	//making all the other cells be an "S" causing red to be the only one having an SOS in the game
	@Test
	public void checkRedGenWin() {
		game.setCurrentGameType(SOSGame.GameType.General);
		game.makeMove(2, 2, 0);
		game.makeMove(0, 0, 0);
		game.makeMove(0, 1, 1);
		game.makeMove(0, 2, 0);
		game.makeMove(1, 0, 0);
		game.makeMove(1, 1, 0);
		game.makeMove(1, 2, 0);
		game.makeMove(2, 0, 0);
		game.makeMove(2, 1, 0);
		Assertions.assertEquals(game.getGameState(), SOSGame.GameState.RED_WON);
	}
	//checks to see if the general game ends in a draw from every cell having an "S" in it, making blue and red both have 0 "SOS"
	@Test
	public void checkGenDraw() {
		game.setCurrentGameType(SOSGame.GameType.General);
		game.makeMove(0, 0, 0);
		game.makeMove(0, 1, 0);
		game.makeMove(0, 2, 0);
		game.makeMove(1, 0, 0);
		game.makeMove(1, 1, 0);
		game.makeMove(1, 2, 0);
		game.makeMove(2, 0, 0);
		game.makeMove(2, 1, 0);
		game.makeMove(2, 2, 0);
		Assertions.assertEquals(game.getGameState(), SOSGame.GameState.DRAW);
	}
	
	//checks to see if the game can make a valid "S" move at 0,0
	@Test
	public void checkValidSMove() {
		game.makeMove(0, 0, 0);
		Assertions.assertEquals(SOSGame.Cell.S, game.getCell(0, 0));
	}
	
	//checks to see if the game can make a valid "O" move at 0,0
	@Test
	public void checkValidOMove() {
		game.makeMove(0, 0, 1);
		Assertions.assertEquals(SOSGame.Cell.O, game.getCell(0, 0));
	}
	
	//checks to see if the game is not allowed to make invalid moves
	@Test
	public void checkInvalidMove() {
		//first invalid move condition is if the game makes a move out of bounds by placing an "S" at 0,3
		game.makeMove(0, 3, 0);
		Assertions.assertNull(game.getCell(0, 3));
		
		//makes an S move at 0,0 and checks if the game can make an O move to overwrite the S at 0,0
		game.makeMove(0, 0, 0);
		game.makeMove(0, 0, 1);
		Assertions.assertEquals(SOSGame.Cell.S, game.getCell(0, 0));
	}
	
	//checks to see if a the blue computer made a move since blue starts first, and if the turn was red's after the first computer move
	//showing that the blue computer can make a move
	@Test
	public void checkComputerMoveBlue() {
		game.makeRandomMove();
		Assertions.assertEquals(game.getTurn(), 'R');
	}
	
	//checks to see if a the red computer made a move since blue starts first, then red moves, returning the turn to blue showing that the 
	//red computer can make a move
	@Test
	public void checkComputerMoveRed() {
		game.makeRandomMove();
		game.makeRandomMove();
		Assertions.assertEquals(game.getTurn(), 'B');
	}
	
	//checks to see if a the computer can complete a game versus another computer in a general game
	@Test
	public void checkComputerCompletedGenGame() {
		game.setCurrentGameType(SOSGame.GameType.General);
		while(game.getGameState() == SOSGame.GameState.PLAYING) {
			game.makeRandomMove();
		}
		Assertions.assertEquals(game.getNumberOfEmptyCells(), 0);
	}
	
	//checks to see if a the computer can complete a game versus another computer in a simple game
	@Test
	public void checkComputerCompletedSimpGame() {
		
		while(game.getGameState() == SOSGame.GameState.PLAYING) {
			game.makeRandomMove();
		}
		
		Assertions.assertTrue(game.getGameState() == SOSGame.GameState.DRAW 
				|| game.getGameState() == SOSGame.GameState.RED_WON 
				|| game.getGameState() == SOSGame.GameState.BLUE_WON);
	}
	
}