import java.util.Observable;
import java.util.Observer;

import javafx.scene.Group;

public class MazeView extends Group implements Observer {
	MazeViewCell[][] cells;
	boolean ignoreFog = false;
	
	public MazeView(int width, int height) {
		resetMaze(width, height);
	}
	
	public void resetMaze(int width, int height) {
		getChildren().clear();
		cells = new MazeViewCell[height][width];
		for(int y=0; y<height; ++y) {
			for(int x=0; x<width; ++x) {
				cells[y][x] = new MazeViewCell(x*MazeViewCell.WIDTH, y*MazeViewCell.HEIGHT);
				getChildren().add(cells[y][x]);
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof MazeMaster) {
			MazeMaster master = (MazeMaster)o;
			int rows = cells.length;
			int cols = cells[0].length;
			for(int y=0; y<rows; ++y) {
				for(int x=0; x<cols; ++x) {
					String imgName = null;
					switch(master.mouseKnownAt(x, y)) {
					case Empty: imgName = "file:resrc/empty.png"; break;
					case Wall: imgName = "file:resrc/wall.png"; break;
					case Mouse: imgName = "file:resrc/mouse.png"; break;
					case Fog:
						if(ignoreFog) {
							if(master.mazeAt(x,  y)==Tile.Empty) imgName = "file:resrc/empty.png";
							else imgName = "file:resrc/wall.png";
						}
						else imgName = "file:resrc/fog.png";
						break;
					case Check: imgName = "file:resrc/check.png"; break;
					}
					cells[y][x].setImage(imgName);
				}
			}
		}
	}
	
	public void setIgnoreFog(boolean ignore) {
		ignoreFog = ignore;
	}
	
	
}
