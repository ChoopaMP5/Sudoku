import javax.swing.*;
import java.awt.*;

public class SudokuView {
    private final SudokuController controller;

    private final JFrame frame = new JFrame("Sudoku");
    private final JLabel textLabel = new JLabel("Errors: 0");
    private final JPanel boardPanel = new JPanel();
    private final JPanel buttonsPanel = new JPanel();
    private final JButton[] numberButtons = new JButton[9];
    private final boolean[] numberUsed = new boolean[9];

    private JButton numSelected = null;
    private int errors = 0;

    static class Tile extends JButton {
        int r, c;
        Tile(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    public SudokuView(SudokuController controller) {
        this.controller = controller;

        frame.setSize(600, 700);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Складність");

        JMenuItem easy = new JMenuItem("Легка");
        JMenuItem medium = new JMenuItem("Середня");
        JMenuItem hard = new JMenuItem("Складна");

        easy.addActionListener(e -> changeDifficulty(SudokuModel.Difficulty.EASY));
        medium.addActionListener(e -> changeDifficulty(SudokuModel.Difficulty.MEDIUM));
        hard.addActionListener(e -> changeDifficulty(SudokuModel.Difficulty.HARD));

        menu.add(easy);
        menu.add(medium);
        menu.add(hard);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        textLabel.setFont(new Font("Arial", Font.BOLD, 30));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(textLabel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(9, 9));
        frame.add(boardPanel, BorderLayout.CENTER);

        buttonsPanel.setLayout(new GridLayout(1, 9));
        setupButtons();
        frame.add(buttonsPanel, BorderLayout.SOUTH);

        setupTiles();
        frame.setVisible(true);
    }

    private void changeDifficulty(SudokuModel.Difficulty difficulty) {
        controller.changeDifficulty(difficulty);
        resetGame();
    }

    private void setupTiles() {
        boardPanel.removeAll();
        int[][] board = controller.getBoard();

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Tile tile = new Tile(r, c);
                tile.setFont(new Font("Arial", Font.BOLD, 20));
                tile.setBackground(Color.white);
                tile.setFocusable(false);

                int bottom = (r == 2 || r == 5 || r == 8) ? 5 : 1;
                int right = (c == 2 || c == 5) ? 5 : 1;
                tile.setBorder(BorderFactory.createMatteBorder(1, 1, bottom, right, Color.black));

                if (board[r][c] != 0) {
                    tile.setText(String.valueOf(board[r][c]));
                }

                boardPanel.add(tile);

                final int row = r;
                final int col = c;
                tile.addActionListener(e -> {
                    if (numSelected != null && tile.getText().isEmpty()) {
                        int selectedNum = Integer.parseInt(numSelected.getText());
                        if (controller.isCorrect(row, col, selectedNum)) {
                            tile.setText(numSelected.getText());
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

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private void setupButtons() {
        for (int i = 1; i <= 9; i++) {
            JButton button = new JButton(String.valueOf(i));
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setFocusable(false);
            button.setBackground(Color.white);
            buttonsPanel.add(button);
            numberButtons[i - 1] = button;

            int selected = i;
            button.addActionListener(e -> {
                if (numberUsed[selected - 1]) return;

                if (numSelected != null) {
                    int prev = Integer.parseInt(numSelected.getText());
                    if (!numberUsed[prev - 1]) {
                        numSelected.setBackground(Color.white);
                    }
                }

                numSelected = button;
                numSelected.setBackground(Color.lightGray);
                highlightSelectedNumber(selected);
            });
        }
    }

    private void checkCompletion() {
        boolean complete = true;
        int nextSelectable = -1;

        for (int num = 1; num <= 9; num++) {
            int count = 0;
            boolean numComplete = true;
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (controller.getSolution()[r][c] == num) {
                        Tile tile = (Tile) boardPanel.getComponent(r * 9 + c);
                        String tileText = tile.getText().trim();

                        if (!tileText.equals(String.valueOf(num))) {
                            numComplete = false;
                            complete = false;
                        } else {
                            count++;
                        }
                    }
                }
            }

            if (numComplete && count == 9 && !numberUsed[num - 1]) {
                numberButtons[num - 1].setBackground(Color.darkGray);
                numberUsed[num - 1] = true;
                if (numSelected != null && Integer.parseInt(numSelected.getText()) == num) {
                    numSelected = null;
                }
            }

            if (!numberUsed[num - 1] && nextSelectable == -1) {
                nextSelectable = num;
            }
        }

        if (numSelected == null && nextSelectable != -1) {
            numSelected = numberButtons[nextSelectable - 1];
            numSelected.setBackground(Color.lightGray);
            highlightSelectedNumber(nextSelectable);
        }

        autoFillRemainingNumber();

        if (complete) {
            int choice = JOptionPane.showOptionDialog(
                    frame,
                    "Ви виграли!\nКількість помилок: " + errors + "\nБажаєте почати нову гру?",
                    "Гру завершено",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Нова гра", "Вийти"},
                    "Нова гра"
            );

            if (choice == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        }
    }

    private void resetGame() {
        errors = 0;
        textLabel.setText("Errors: 0");
        numSelected = null;
        for (int i = 0; i < 9; i++) {
            numberUsed[i] = false;
            numberButtons[i].setBackground(Color.white);
        }
        controller.generateNewGame();
        setupTiles();
    }

    private void autoFillRemainingNumber() {
        int remaining = -1;
        int remainingCount = 0;

        for (int i = 0; i < 9; i++) {
            if (!numberUsed[i]) {
                remaining = i + 1;
                remainingCount++;
            }
        }

        if (remainingCount == 1) {
            for (Component comp : boardPanel.getComponents()) {
                if (comp instanceof Tile tile && tile.getText().isEmpty()) {
                    if (controller.getSolution()[tile.r][tile.c] == remaining) {
                        tile.setText(String.valueOf(remaining));
                        tile.setBackground(Color.pink);
                    }
                }
            }

            numberUsed[remaining - 1] = true;
            numberButtons[remaining - 1].setBackground(Color.darkGray);
            numSelected = null;

            checkCompletion();
        }
    }

    private void highlightSelectedNumber(int selectedNum) {
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
