import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MazeController extends VBox {

	private class TimerHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent event) {
			if(!mazeMaster.isInGoal()) {
				mazeMaster.move();
				++moved;
				updateFields();
			}
			else {
				autoPlayTimer.stop();
				autoPlaying = false;
			}
		}
	}
	
	public static int WIDTH = 250;
	
	public static final String[] strategys = {
		"Random Strategy",
		"Tremaux Strategy"
	};
	
	private MazeMaster mazeMaster;
	private MazeView mazeView;
	private Stage primaryStage;
	
	private Button newMazeButton = new Button("New Maze");

	private CheckBox ignoreFogCheck = new CheckBox("blacksheep wall");
	private TextField playIntervalField = new TextField();
	private ComboBox<String> strategySelect = new ComboBox<>();
	private Button strategySelectButton = new Button("OK");
	
	private Button restartButton = new Button("<<");
	private Button backButton = new Button("<");
	private Button playButton = new Button("▷");
	private Button nextButton = new Button(">");
	private Button skipButton = new Button(">>");
	
	private TextField widthField = new TextField();
	private TextField heightField = new TextField();
	private TextField movedField = new TextField();
	private TextField strategyField = new TextField();
	private int moved = 0;
	private double playInterval = 0.2;
	private boolean autoPlaying = false;
	private Timeline autoPlayTimer = new Timeline();
	
	public MazeController(MazeMaster mazeMaster, MazeView mazeView, Stage primaryStage) {
		this.mazeMaster = mazeMaster;
		this.mazeView = mazeView;
		this.primaryStage = primaryStage;
		
		allocate();
		registerEvent();
		updateFields();
	}
	
	public void registerEvent() {
		newMazeButton.setOnAction((event)->{
			int rows = Integer.parseInt(heightField.getText());
			int cols = Integer.parseInt(widthField.getText());
			Maze newMaze = MazeFactory.instance.createMaze(rows, cols);
			stopTimer();
			mazeView.resetMaze(cols, rows);
			mazeMaster.resetMaze(newMaze);
			mazeMaster.setMouseStrategy(selectedStrategy());
			moved = 0;
			updateFields();
			primaryStage.setWidth(mazeMaster.mazeCols() * MazeViewCell.WIDTH + MazeController.WIDTH);
			primaryStage.setHeight(Math.max(mazeMaster.mazeRows() * MazeViewCell.HEIGHT + 40
					, 480));
		});
		
		ignoreFogCheck.setOnAction((event)->{
			mazeView.setIgnoreFog(ignoreFogCheck.isSelected());
			mazeView.update(mazeMaster, null);
		});
		playIntervalField.textProperty().addListener((observable, oldValue, newValue) -> {
			double d = Double.parseDouble(newValue);
			if(d>1E-5) {
				boolean played = autoPlaying;
				if(autoPlaying)
					stopTimer();
				playInterval = d;
				autoPlayTimer.getKeyFrames().clear();
				autoPlayTimer.getKeyFrames().add(new KeyFrame(Duration.millis(playInterval*1000.0), new TimerHandler()));
				autoPlayTimer.setDelay(Duration.millis(playInterval*1000.0));
				if(played)
					playTimer();
			}
		});
		strategySelectButton.setOnAction((event)->{
			PathFindingStrategy newStrategy = selectedStrategy();
			if(newStrategy.getClass()!=mazeMaster.getMouseStrategy().getClass())
				mazeMaster.setMouseStrategy(selectedStrategy());
			int selected = strategySelect.getSelectionModel().getSelectedIndex();
			strategyField.setText(strategys[selected]);
		});
		
		restartButton.setOnAction((event)->{
			stopTimer();
			mazeMaster.resetMouse();
			mazeMaster.setMouseStrategy(selectedStrategy());
			moved = 0;
			updateFields();
		});
		backButton.setOnAction((event)->{
			stopTimer();
			mazeMaster.back();
			--moved;
			updateFields();
		});
		playButton.setOnAction((event)->{
			if(mazeMaster.isInGoal()) return;
			if(autoPlaying) {
				stopTimer();
			}
			else {
				playTimer();
			}
		});
		nextButton.setOnAction((event)->{
			stopTimer();
			if(!mazeMaster.isInGoal()) {
				mazeMaster.move();
				++moved;
				updateFields();
			}
		});
		skipButton.setOnAction((event)->{
			stopTimer();
			while(!mazeMaster.isInGoal()) {
				mazeMaster.move();
				++moved;
			}
			updateFields();
		});

		autoPlayTimer.setCycleCount(Animation.INDEFINITE);
		autoPlayTimer.getKeyFrames().add(new KeyFrame(Duration.millis(playInterval*1000.0), new TimerHandler()));
		autoPlayTimer.setDelay(Duration.millis(playInterval*1000.0));

	}
	
	private PathFindingStrategy selectedStrategy() {
		int selected = strategySelect.getSelectionModel().getSelectedIndex();
		if(selected==0)
			return new RandomStrategy();
		else if(selected==1)
			return new TremauxStrategy();
		return null;
	}
	
	private void stopTimer() {
		autoPlayTimer.stop();
		playButton.setText("▷");
		autoPlaying = false;
	}
	
	private void playTimer() {
		autoPlayTimer.play();
		playButton.setText("ll");
		autoPlaying = true;
	}
	
	private void allocate() {
		setSpacing(45);
		setPadding(new Insets(10,10,10,10));
		setAlignment(Pos.CENTER);
		this.setMinWidth(150);
		this.setMinHeight(400);
		
		HBox mazeBox = new HBox();
		VBox optionBox = new VBox();
		HBox playBox = new HBox();
		HBox displayBox = new HBox();

		HBox[] boxes = {mazeBox, playBox, displayBox};
		for(HBox b : boxes) {
			b.setAlignment(Pos.CENTER);
			b.setSpacing(10);
		}
		optionBox.setAlignment(Pos.CENTER);
		optionBox.setSpacing(10);
		
		HBox playIntervalBox = new HBox();
		playIntervalBox.setSpacing(5);
		playIntervalBox.setAlignment(Pos.CENTER);
		playIntervalBox.getChildren().addAll(new Label("재생 간격"), playIntervalField, 
				new Label("초"));
		playIntervalField.setText("" + playInterval);
		playIntervalField.setPrefWidth(50);
		playIntervalField.setAlignment(Pos.CENTER);
		
		HBox strategyBox = new HBox();
		strategyBox.setSpacing(10);
		strategyBox.setAlignment(Pos.CENTER);
		strategyBox.getChildren().addAll(strategySelect, strategySelectButton);
		strategySelect.getItems().addAll(strategys);
		strategySelect.getSelectionModel().selectFirst();
		
		playButton.setMinWidth(30);
		
		VBox displayLabelBox = new VBox();
		displayLabelBox.setSpacing(18);
		displayLabelBox.setAlignment(Pos.CENTER);
		displayLabelBox.getChildren().addAll(
				new Label("Maze Width"),
				new Label("Maze Height"),
				new Label("Mouse Moved"),
				new Label("Strategy")
		);
		TextField[] textFields = {widthField, heightField, movedField, strategyField};
		for(TextField tf : textFields) {
			tf.setMaxWidth(120);
			tf.setEditable(false);
			tf.setText("0");
		}
		widthField.setEditable(true);
		heightField.setEditable(true);
		strategyField.setText(strategys[0]);
		VBox displayFieldBox = new VBox(textFields);
		displayFieldBox.setSpacing(10);
		
		mazeBox.getChildren().add(newMazeButton);
		optionBox.getChildren().addAll(ignoreFogCheck, playIntervalBox, strategyBox);
		playBox.getChildren().addAll(restartButton, backButton, playButton, nextButton, skipButton);
		displayBox.getChildren().addAll(displayLabelBox, displayFieldBox);
		
		getChildren().addAll(mazeBox, optionBox, playBox, displayBox);
	}
	
	public void updateFields() {
		widthField.setText(""+mazeMaster.mazeCols());
		heightField.setText(""+mazeMaster.mazeRows());
		movedField.setText(""+moved);
	}
}
