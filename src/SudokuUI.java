import java.awt.*;
import javax.swing.*;

public class SudokuUI {
    private final SudokuLogic logic;
    private final JFrame frame = new JFrame("Sudoku");
    private final JLabel textLabel = new JLabel("Errors: 0");
    private final JPanel textPanel = new JPanel();
    private final JPanel boardPanel = new JPanel();
    private final JPanel buttonsPanel = new JPanel();
    private JButton numSelected = null;
    private int errors = 0;
    private final JButton[] numberButtons = new JButton[9];
    private final boolean[] numberUsed = new boolean[9];

    static class Tile extends JButton {
        int r, c;
        Tile(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    public SudokuUI() {
        logic = new SudokuLogic();
        frame.setSize(600, 650);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 30));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(9, 9));
        setupTiles();
        frame.add(boardPanel, BorderLayout.CENTER);

        buttonsPanel.setLayout(new GridLayout(1, 9));
        setupButtons();
        frame.add(buttonsPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    void setupTiles() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Tile tile = new Tile(r, c);
                if (logic.board[r][c] != 0) {
                    tile.setFont(new Font("Arial", Font.BOLD, 20));
                    tile.setText(String.valueOf(logic.board[r][c]));
                    tile.setBackground(Color.white);
                } else {
                    tile.setFont(new Font("Arial", Font.BOLD, 20));
                    tile.setBackground(Color.white);
                }
                tile.setBorder(BorderFactory.createLineBorder(Color.black));
                tile.setFocusable(false);

                boardPanel.add(tile);

                int bottom = (r == 2 || r == 5 || r == 8 ) ? 5 : 1;
                int right = (c == 2 || c == 5) ? 5 : 1;
                tile.setBorder(BorderFactory.createMatteBorder(1, 1, bottom, right, Color.black));

                final int row = r;
                final int col = c;
                tile.addActionListener(e -> {
                    if (numSelected != null && tile.getText().isEmpty()) {
                        String numSelectedText = numSelected.getText();
                        int selectedNum = Integer.parseInt(numSelectedText);
                        if (logic.solution[row][col] == selectedNum) {
                            tile.setText(numSelectedText);
                            tile.setBackground(Color.pink);
                            checkCompletion();
                        } else {
                            errors++;
                            textLabel.setText("Errors: " + errors);
                        }
                    }
                });
            }
        }
    }

    void setupButtons() {
        for (int i = 1; i <= 9; i++) {
            JButton button = new JButton(String.valueOf(i));
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setFocusable(false);
            button.setBackground(Color.white);
            buttonsPanel.add(button);
            numberButtons[i - 1] = button;

            numberButtons[i - 1].addActionListener(e -> {
                if (numSelected != null && !numberUsed[Integer.parseInt(numSelected.getText()) - 1]) {
                    numSelected.setBackground(Color.white);
                }
                numSelected = (JButton) e.getSource();
                numSelected.setBackground(Color.lightGray);

                highlightSelectedNumber(Integer.parseInt(numSelected.getText()));
            });
        }
    }

    void checkCompletion() {
        boolean gameComplete = true;

        for (int num = 1; num <= 9; num++) {
            int count = 0;
            boolean numComplete = true;

            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (logic.solution[r][c] == num) {
                        Tile tile = (Tile) boardPanel.getComponent(r * 9 + c);
                        String tileText = tile.getText().trim();

                        if (!tileText.equals(String.valueOf(num))) {
                            numComplete = false;
                            gameComplete = false;
                        } else {
                            count++;
                        }
                    }
                }
            }

            if (numComplete && count == 9) {
                numberButtons[num - 1].setBackground(Color.darkGray);
                numberUsed[num - 1] = true;
            }
        }

        if (gameComplete) {
            JOptionPane.showMessageDialog(frame, "Ви виграли!");
            frame.dispose();
        }
    }

    void highlightSelectedNumber(int selectedNum) {
        for (Component component : boardPanel.getComponents()) {
            if (component instanceof Tile tile) {
                if (!tile.getText().isEmpty()) {
                    int tileNumber = Integer.parseInt(tile.getText());
                    tile.setBackground(tileNumber == selectedNum ? Color.PINK : Color.white);
                }
            }
        }
    }
}
