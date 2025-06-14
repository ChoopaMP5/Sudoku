import java.util.*;

public class SudokuModel {
    public enum Difficulty {
        EASY(30), MEDIUM(40), HARD(50);
        final int holes;
        Difficulty(int holes) { this.holes = holes; }
    }

    private int[][] board = new int[9][9];
    private int[][] solution = new int[9][9];
    private Difficulty difficulty = Difficulty.MEDIUM;

    public SudokuModel() {
        generateSudoku();
    }

    public int[][] getBoard() {
        return board;
    }

    public int[][] getSolution() {
        return solution;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void generateSudoku() {
        clearGrid(solution);
        fillBoard(solution);
        for (int r = 0; r < 9; r++) {
            System.arraycopy(solution[r], 0, board[r], 0, 9);
        }
        removeNumbers(board, difficulty.holes);
    }

    private void clearGrid(int[][] grid) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                grid[r][c] = 0;
            }
        }
    }

    private boolean fillBoard(int[][] grid) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (grid[r][c] == 0) {
                    List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
                    Collections.shuffle(numbers);
                    for (int num : numbers) {
                        if (isValid(grid, r, c, num)) {
                            grid[r][c] = num;
                            if (fillBoard(grid)) return true;
                            grid[r][c] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private void removeNumbers(int[][] grid, int count) {
        while (count > 0) {
            int r = (int) (Math.random() * 9);
            int c = (int) (Math.random() * 9);
            if (grid[r][c] != 0) {
                grid[r][c] = 0;
                count--;
            }
        }
    }

    private boolean isValid(int[][] grid, int row, int col, int num) {
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int i = 0; i < 9; i++) {
            if (grid[row][i] == num || grid[i][col] == num || grid[boxRow + i / 3][boxCol + i % 3] == num) {
                return false;
            }
        }
        return true;
    }

    public boolean isCorrect(int row, int col, int num) {
        return solution[row][col] == num;
    }
}
