package a;

public abstract class Player {
    protected char playerColor; // 'B' for blue, 'R' for red
    protected SOSGame game;

    public Player(SOSGame game, char playerColor) {
        this.game = game;
        this.playerColor = playerColor;
    }

    // Abstract method that subclasses need to implement
    public abstract void makeMove();
}