package main;
import javax.swing.SwingUtilities;
import controller.Controller;
import view.GameView;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Controller gameController = new Controller();
                GameView gameView = new GameView(gameController);
                gameView.setVisible(true);
            }
        });
    }
}