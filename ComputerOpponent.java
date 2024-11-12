package a;

public class ComputerOpponent extends Player {

    public ComputerOpponent(SOSGame game, char playerColor) {
        super(game, playerColor);
    }

    @Override
    public void makeMove() {
        // Logic for the computer to make a move automatically.
    	// ComputerOpponent established and incorporated with SOSGame and SOSGUI
        game.makeRandomMove();  // Make a random move as a placeholder
    }
}