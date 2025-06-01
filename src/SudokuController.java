public class SudokuController {
    private final SudokuModel model;

    public SudokuController(SudokuModel model) {
        this.model = model;
    }

    public void changeDifficulty(SudokuModel.Difficulty difficulty) {
        model.setDifficulty(difficulty);
        model.generateSudoku();
    }

    public int[][] getBoard() {
        return model.getBoard();
    }

    public int[][] getSolution() {
        return model.getSolution();
    }

    public boolean isCorrect(int row, int col, int num) {
        return model.isCorrect(row, col, num);
    }

    public void generateNewGame() {
        model.generateSudoku();
    }
}
