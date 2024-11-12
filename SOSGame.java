package a;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class SOSGame {
    public int TOTALROWS;
    public int TOTALCOLUMNS;

    public enum Cell {
        EMPTY, S, O
    }

    protected Cell[][] grid;
    protected char turn;
    protected int x, y;
    protected int blueScore, redScore;
    protected ArrayList<ArrayList<Integer>> sosInfo;

    // New player-related fields
    private Player bluePlayer;
    private Player redPlayer;

    public boolean isBlueComputer;
    public boolean isRedComputer;


    public void setPlayers(Player bluePlayer, Player redPlayer) {
        this.bluePlayer = bluePlayer;
        this.redPlayer = redPlayer;
    }
    
    public enum GameType {
        Simple, General
    }

    protected GameType currentGameType;

    public enum GameState {
        PLAYING, DRAW, BLUE_WON, RED_WON
    }

    protected GameState currentGameState;

    // SOSGame constructor
    public SOSGame(int n, boolean isBlueHuman, boolean isRedHuman) {
        currentGameType = GameType.Simple;
        grid = new Cell[n][n];
        TOTALROWS = TOTALCOLUMNS = n;
        initGame(isBlueHuman, isRedHuman);
    }

    private void initGame(boolean isBlueHuman, boolean isRedHuman) {
        for (int row = 0; row < TOTALROWS; ++row) {
            for (int col = 0; col < TOTALCOLUMNS; ++col) {
                grid[row][col] = Cell.EMPTY;
            }
        }
        currentGameState = GameState.PLAYING;
        turn = 'B';
        blueScore = redScore = 0;
        sosInfo = new ArrayList<>();

        // Create the blue and red players based on whether they are human or computer
        bluePlayer = isBlueHuman ? new HumanPlayer(this, 'B') : new ComputerOpponent(this, 'B');
        redPlayer = isRedHuman ? new HumanPlayer(this, 'R') : new ComputerOpponent(this, 'R');
    }

    // Reset game state
    public void resetGame(boolean isBlueHuman, boolean isRedHuman) {
        initGame(isBlueHuman, isRedHuman);
    }

    // Getters for game state
    public int getTotalRows() {
        return TOTALROWS;
    }

    public int getTotalColumns() {
        return TOTALCOLUMNS;
    }

    public GameType getCurrentGameType() {
        return currentGameType;
    }

    public void setCurrentGameType(GameType currentGameType) {
        this.currentGameType = currentGameType;
    }

    public ArrayList<ArrayList<Integer>> getSosInfo() {
        return sosInfo;
    }

    public Cell getCell(int row, int column) {
        if (row >= 0 && row < TOTALROWS && column >= 0 && column < TOTALCOLUMNS) {
            return grid[row][column];
        } else {
            return null;
        }
    }

    public char getTurn() {
        return turn;
    }

    public void writeLine(String str) {
        try {
            FileOutputStream o = new FileOutputStream(new File("record.txt"), true);
            o.write(str.getBytes("GBK"));
            o.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Making a move
    public void makeMove(int row, int column, int type) {
        if (row >= 0 && row < TOTALROWS && column >= 0 && column < TOTALCOLUMNS && grid[row][column] == Cell.EMPTY) {
            x = row;
            y = column;
            grid[row][column] = (type == 0) ? Cell.S : Cell.O;
            sosInfo.add(checkSOS());
            updateGameState();
            String input = (type == 0) ? "S" : "O";
            String tu = (turn == 'B') ? "Red" : "Blue";
            writeLine(tu + ": (" + row + ", " + column + ") -> " + input + "\n");
            turn = (turn == 'B') ? 'R' : 'B';
        }
    }

    // Random move for the computer opponent
    public void makeRandomMove() {
        if (currentGameState != GameState.PLAYING)
            return;
        int numberOfEmptyCells = getNumberOfEmptyCells();
        if (numberOfEmptyCells == 0) {
            return;
        }
        Random random = new Random();
        int targetMove = random.nextInt(numberOfEmptyCells);
        int index = 0;
        for (int row = 0; row < TOTALROWS; ++row) {
            for (int col = 0; col < TOTALCOLUMNS; ++col) {
                if (grid[row][col] == Cell.EMPTY) {
                    if (targetMove == index) {
                        boolean r = random.nextBoolean();
                        int t = r ? 0 : 1;
                        makeMove(row, col, t);
                        return;
                    } else
                        index++;
                }
            }
        }
    }

    // Get number of empty cells
    public int getNumberOfEmptyCells() {
        int numberOfEmptyCells = 0;
        for (int row = 0; row < TOTALROWS; ++row) {
            for (int col = 0; col < TOTALCOLUMNS; ++col) {
                if (grid[row][col] == Cell.EMPTY) {
                    numberOfEmptyCells++;
                }
            }
        }
        return numberOfEmptyCells;
    }

    // Check if a SOS pattern has been formed
    public ArrayList<Integer> checkSOS() {
        ArrayList<Integer> res = new ArrayList<>();
        if (turn == 'B') {
            res.add(0);
        } else {
            res.add(1);
        }
        boolean isChanged = false;
        if (grid[x][y] == Cell.O) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (x - i < 0 || x - i >= TOTALROWS || x + i < 0 || x + i >= TOTALROWS || y - j < 0
                            || y - j >= TOTALCOLUMNS || y + j < 0 || y + j >= TOTALCOLUMNS)
                        continue;
                    if (grid[x - i][y - j] == Cell.S && grid[x + i][y + j] == Cell.S) {
                        res.add(x - i);
                        res.add(y - j);
                        res.add(x + i);
                        res.add(y + j);
                        if (turn == 'B')
                            blueScore++;
                        else
                            redScore++;
                        isChanged = true;
                    }
                }
            }
        } else {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (x + 2 * i < 0 || x + 2 * i >= TOTALROWS || y + 2 * j < 0 || y + 2 * j >= TOTALCOLUMNS)
                        continue;
                    if (grid[x + 2 * i][y + 2 * j] == Cell.S && grid[x + i][y + j] == Cell.O) {
                        res.add(x);
                        res.add(y);
                        res.add(x + 2 * i);
                        res.add(y + 2 * j);
                        if (turn == 'B')
                            blueScore++;
                        else
                            redScore++;
                        isChanged = true;
                    }
                }
            }
        }
        if (isChanged)
            turn = (turn == 'B') ? 'R' : 'B';
        return res;
    }

    // Update the game state
    private void updateGameState() {
        int x = hasWon();
        if (x > 0) { // Check for win
            if (x == 1)
                currentGameState = GameState.BLUE_WON;
            else if (x == 2)
                currentGameState = GameState.RED_WON;
            else if (x == 3)
                currentGameState = GameState.DRAW;
        }
    }

    // Check if the grid is full
    private boolean isFull() {
        for (int row = 0; row < TOTALROWS; ++row) {
            for (int col = 0; col < TOTALCOLUMNS; ++col) {
                if (grid[row][col] == Cell.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    // Determine if a player has won
    private int hasWon() {
        if (currentGameType == GameType.Simple) {
            if (blueScore > 0)
                return 1;
            if (redScore > 0)
                return 2;
            if (isFull() && ((blueScore & redScore) == 0))
                return 3;
            return 0;
        } else {
            if (!isFull())
                return 0;
            if (blueScore > redScore)
                return 1;
            else if (blueScore < redScore)
                return 2;
            else
                return 3;
        }
    }

    // Get the current game state
    public GameState getGameState() {
        return currentGameState;
    }

    // Get the current score of blue and red
    public int getBlueScore() {
        return blueScore;
    }

    public int getRedScore() {
        return redScore;
    }

    // Get the blue player
    public Player getBluePlayer() {
        return bluePlayer;
    }

    // Get the red player
    public Player getRedPlayer() {
        return redPlayer;
    }

    
    private void resetBoard() {
        // Reset the game board and other game states (e.g., scores)
        System.out.println("Board reset.");
        // You can add logic to reset the board here
    }

}