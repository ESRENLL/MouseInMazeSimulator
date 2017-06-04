import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class MazeViewCell extends Rectangle {
	public static final int WIDTH = 16;
	public static final int HEIGHT = 16;
	public MazeViewCell(int x, int y) {
		super(x, y, WIDTH, HEIGHT);
		setFill(Color.GRAY);
	}
	public void setImage(String imgName) {
		setFill(ImagePatternMap.instance.get(imgName));
	}
}
