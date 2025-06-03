import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SudokuModelTest {

    private SudokuModel model;

    @BeforeEach
    void setUp() {
        model = new SudokuModel();
    }

    @Test
    void getBoard_ShouldReturn9x9Grid() {
        int[][] board = model.getBoard();
        assertEquals(9, board.length);
        for (int[] row : board) {
            assertEquals(9, row.length);
        }
    }

    @Test
    void getSolution_ShouldReturnValidFilledGrid() {
        int[][] solution = model.getSolution();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int num = solution[r][c];
                assertTrue(num >= 1 && num <= 9, "Solution contains invalid number");
            }
        }
    }

    @Test
    void setDifficulty_ShouldChangePuzzleComplexity() {
        model.setDifficulty(SudokuModel.Difficulty.EASY);
        model.generateSudoku();
        int holesEasy = countZeros(model.getBoard());

        model.setDifficulty(SudokuModel.Difficulty.HARD);
        model.generateSudoku();
        int holesHard = countZeros(model.getBoard());

        assertTrue(holesHard > holesEasy, "HARD should have more empty cells than EASY");
    }

    @Test
    void generateSudoku_ShouldPreserveSolution() {
        model.setDifficulty(SudokuModel.Difficulty.MEDIUM);
        model.generateSudoku();

        int[][] board = model.getBoard();
        int[][] solution = model.getSolution();

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] != 0) {
                    assertEquals(solution[r][c], board[r][c], "Board value must match solution for non-zero cells");
                }
            }
        }
    }

    @Test
    void isCorrect_ShouldReturnTrueForCorrectInput() {
        int[][] solution = model.getSolution();
        int row = 0, col = 0;
        int correctNum = solution[row][col];
        assertTrue(model.isCorrect(row, col, correctNum));
    }

    @Test
    void isCorrect_ShouldReturnFalseForIncorrectInput() {
        int[][] solution = model.getSolution();
        int row = 0, col = 0;
        int incorrectNum = (solution[row][col] % 9) + 1;
        if (incorrectNum == solution[row][col]) {
            incorrectNum = (incorrectNum % 9) + 1;
        }
        assertFalse(model.isCorrect(row, col, incorrectNum));
    }

    private int countZeros(int[][] grid) {
        int count = 0;
        for (int[] row : grid) {
            for (int val : row) {
                if (val == 0) count++;
            }
        }
        return count;
    }
}
