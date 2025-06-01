import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SudokuModel model = new SudokuModel();
            SudokuController controller = new SudokuController(model);
            new SudokuView(controller);
        });
    }
}
