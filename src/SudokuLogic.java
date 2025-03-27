import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class SudokuLogic {
    int[][] board = new int[9][9];
    int[][] solution = new int[9][9];

    public SudokuLogic() {
        generateSudoku();
    }

    void generateSudoku() {
        fillBoard(solution);
        for (int r = 0; r < 9; r++) {
            System.arraycopy(solution[r], 0, board[r], 0, 9);
        }
        removeNumbers(board);
    }

    boolean fillBoard(int[][] grid) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (grid[r][c] == 0) {
                    List<Integer> numbers = new ArrayList<>(List.of(1,2,3,4,5,6,7,8,9));
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

    void removeNumbers(int[][] grid) {
        int count = 40;
        while (count > 0) {
            int r = (int) (Math.random() * 9);
            int c = (int) (Math.random() * 9);
            if (grid[r][c] != 0) {
                grid[r][c] = 0;
                count--;
            }
        }
    }

    boolean isValid(int[][] grid, int row, int col, int num) {
        int boxRow = (row / 3) * 3, boxCol = (col / 3) * 3;
        for (int i = 0; i < 9; i++) {
            if (grid[row][i] == num || grid[i][col] == num ||
                    grid[boxRow + i / 3][boxCol + i % 3] == num) {
                return false;
            }
        }
        return true;
    }
}
