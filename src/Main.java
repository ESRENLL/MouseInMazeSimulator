import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane mainPane = new BorderPane();
		Scene mainScene = new Scene(mainPane, 100, 100);
		
		Maze maze = MazeFactory.instance.createMaze(51, 51);
		MazeMaster mazeMaster = new MazeMaster(maze);
		MazeView mazeView = new MazeView(maze.getCols(), maze.getRows());
		MazeController mazeController = new MazeController(mazeMaster, mazeView, primaryStage);
		mazeMaster.addObserver(mazeView);
		mazeView.update(mazeMaster, null);
		
		mainPane.setCenter(mazeView);
		mainPane.setRight(mazeController);
		
		primaryStage.setTitle("Maze Simulator");
		int w = maze.MAZE[0].length * MazeViewCell.WIDTH + MazeController.WIDTH;
		int h = maze.MAZE.length * MazeViewCell.HEIGHT + 40;
		primaryStage.setScene(mainScene);
		primaryStage.setWidth(w);
		primaryStage.setHeight(h);
		primaryStage.show();
		
	}

}
