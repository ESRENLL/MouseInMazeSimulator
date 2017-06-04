
public class Maze {

    public final Tile[][] MAZE;
    public final IntPoint ENTRANCE;
    public final IntPoint EXIT;

    public Maze(Tile[][] maze, IntPoint entrance, IntPoint exit) {
    	MAZE = maze;
    	ENTRANCE = entrance;
    	EXIT = exit;
    }
    
    public boolean isOutOfBound(int x, int y) {
        return x<0 || y<0 || x>=getCols() || y>=getRows();
    }

    public int getRows() {
    	return MAZE[0].length;
    }

    public int getCols() {
    	return MAZE.length;
    }

}