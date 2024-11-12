package a;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class SOSGUI extends JFrame {

    public static final int CELL_SIZE = 50;
    public static final int GRID_WIDTH = 1;
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;

    public static final int CELL_PADDING = CELL_SIZE / 6;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
    public static final int SYMBOL_STROKE_WIDTH = 8;

    private GameBoardCanvas gameBoardCanvas;
    private JLabel gameStatusBar;

    private SOSGame game;

    private boolean flag;

    JFrame jf;
    JRadioButton blueButton1;
    JRadioButton blueButton2;
    JRadioButton redButton1;
    JRadioButton redButton2;
    JRadioButton blueHuman;
    JRadioButton blueComputer;
    JRadioButton redHuman;
    JRadioButton redComputer;

    public SOSGUI() {
        this(new SOSGame(5, true, true));
        jf = this;
    }

    public SOSGUI(SOSGame game) {
        this.game = game;
        setContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setTitle("SOS");
        setVisible(true);
        jf = this;
        flag = false;
    }

    private void setContentPane() {
        gameBoardCanvas = new GameBoardCanvas();
        gameBoardCanvas.setPreferredSize(new Dimension(CELL_SIZE * game.getTotalRows(), CELL_SIZE * game.getTotalColumns()));
        gameStatusBar = new JLabel("  ");
        gameStatusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
        gameStatusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        JPanel p = new JPanel();
        p.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        p.setLayout(new BorderLayout());
        p.add(gameBoardCanvas, BorderLayout.CENTER);
        p.add(gameStatusBar, BorderLayout.SOUTH);
        contentPane.add(p, BorderLayout.CENTER);

        JPanel p1 = new JPanel();
        p1.setBackground(new Color(255, 255, 255));
        JLabel sosLabel = new JLabel("SOS");
        sosLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        JRadioButton sosButton1 = new JRadioButton("Simple game", true);
        sosButton1.setBackground(new Color(255, 255, 255));
        JRadioButton sosButton2 = new JRadioButton("General game");
        sosButton2.setBackground(new Color(255, 255, 255));
        JLabel boardLabel = new JLabel("Board Size");
        JTextArea text = new JTextArea("");
        text.setBackground(new Color(192, 192, 192));
        text.setFont(new Font("Tahoma", Font.PLAIN, 13));
        text.setPreferredSize(new Dimension(20, 20));
        JButton confirm = new JButton("Apply Size");

        confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer.valueOf(text.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid entry please try again.");
                    return;
                }

                if (Integer.valueOf(text.getText()) < 3) {
                    JOptionPane.showMessageDialog(null, "Please enter a size that's at least 3.");
                    return;
                }

                // Assuming both players are human by default, you can modify as needed
                boolean isBlueHuman = true;  // Default for blue player
                boolean isRedHuman = true;   // Default for red player

                // Dispose of the current window
                jf.dispose();

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        // Create the game with the chosen board size and default human players
                        SOSGUI gui = new SOSGUI(new SOSGame(Integer.valueOf(text.getText()), isBlueHuman, isRedHuman));
                    }
                });
            }
        });

        ButtonGroup sosGroup = new ButtonGroup();
        sosGroup.add(sosButton1);
        sosGroup.add(sosButton2);
        sosButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.setCurrentGameType(SOSGame.GameType.Simple);
            }
        });
        sosButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.setCurrentGameType(SOSGame.GameType.General);
            }
        });
        p1.add(sosLabel);
        p1.add(sosButton1);
        p1.add(sosButton2);
        p1.add(boardLabel);
        p1.add(text);
        p1.add(confirm);
        contentPane.add(p1, BorderLayout.NORTH);

        JPanel p2 = new JPanel();
        p2.setBackground(new Color(255, 255, 255));
        blueHuman = new JRadioButton("Human", true);
        blueHuman.setFont(new Font("Tahoma", Font.PLAIN, 20));
        blueHuman.setBackground(new Color(255, 255, 255));
        blueComputer = new JRadioButton("Computer");
        blueComputer.setFont(new Font("Tahoma", Font.PLAIN, 20));
        blueComputer.setBackground(new Color(255, 255, 255));
        blueComputer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.isBlueComputer = blueComputer.isSelected();
            }
        });
        blueButton1 = new JRadioButton("S", true);
        blueButton1.setFont(new Font("Tahoma", Font.PLAIN, 20));
        blueButton1.setBackground(new Color(255, 255, 255));
        blueButton2 = new JRadioButton("O");
        blueButton2.setFont(new Font("Tahoma", Font.PLAIN, 20));
        blueButton2.setBackground(new Color(255, 255, 255));
        ButtonGroup bluePlayerGroup = new ButtonGroup();
        bluePlayerGroup.add(blueButton1);
        bluePlayerGroup.add(blueButton2);
        ButtonGroup blueGroup = new ButtonGroup();
        blueGroup.add(blueHuman);
        blueGroup.add(blueComputer);
        p2.setLayout(null);
        p2.add(blueHuman);
        p2.add(blueButton1);
        p2.add(blueButton2);
        p2.add(blueComputer);

        contentPane.add(p2, BorderLayout.WEST);
        p2.setPreferredSize(new Dimension(200, 400));

        JLabel blueLabel = new JLabel("Blue Player");
        blueLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        blueLabel.setBounds(10, 84, 103, 31);
        p2.add(blueLabel);
        JButton recordButton = new JButton("   Record   ");
        recordButton.setBounds(10, 369, 148, 21);
        p2.add(recordButton);
        recordButton.setFont(new Font("Tahoma", Font.PLAIN, 20));

        JPanel p3 = new JPanel();
        p3.setBackground(new Color(255, 255, 255));
        redHuman = new JRadioButton("Human", true);
        redHuman.setFont(new Font("Tahoma", Font.PLAIN, 20));
        redHuman.setBackground(new Color(255, 255, 255));
        redComputer = new JRadioButton("Computer");
        redComputer.setFont(new Font("Tahoma", Font.PLAIN, 20));
        redComputer.setBackground(new Color(255, 255, 255));
        redComputer.setBounds(0, 234, 142, 21);
        redComputer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.isRedComputer = redComputer.isSelected();
            }
        });
        redButton1 = new JRadioButton("S", true);
        redButton1.setFont(new Font("Tahoma", Font.PLAIN, 20));
        redButton1.setBackground(new Color(255, 255, 255));
        redButton2 = new JRadioButton("O");
        redButton2.setFont(new Font("Tahoma", Font.PLAIN, 20));
        redButton2.setBackground(new Color(255, 255, 255));
        ButtonGroup redPlayerGroup = new ButtonGroup();
        redPlayerGroup.add(redButton1);
        redPlayerGroup.add(redButton2);
        ButtonGroup redGroup = new ButtonGroup();
        redGroup.add(redHuman);
        redGroup.add(redComputer);
        p3.setLayout(null);
        p3.add(redHuman);
        p3.add(redButton1);
        p3.add(redButton2);
        p3.add(redComputer);

        contentPane.add(p3, BorderLayout.EAST);
        p3.setPreferredSize(new Dimension(200, 400));

        JLabel redLabel = new JLabel("Red Player");
        redLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        redLabel.setBounds(10, 84, 103, 31);
        p3.add(redLabel);

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
        startButton.setBounds(36, 372, 148, 23);
        p3.add(startButton);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create players based on selection
                Player bluePlayer;
                if (blueHuman.isSelected()) {
                    bluePlayer = new HumanPlayer(game, 'B');
                } else {
                    bluePlayer = new ComputerOpponent(game, 'B');
                }

                Player redPlayer;
                if (redHuman.isSelected()) {
                    redPlayer = new HumanPlayer(game, 'R');
                } else {
                    redPlayer = new ComputerOpponent(game, 'R');
                }

                // Initialize game with players
                game.setPlayers(bluePlayer, redPlayer);

             
            }
        });

        recordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Logic for recording gameplay
            }
        });
    }

    public class GameBoardCanvas extends JComponent {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Drawing logic for the game board
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SOSGUI();
            }
        });
    }
}