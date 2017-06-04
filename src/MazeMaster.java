import java.util.Observable;

public class MazeMaster extends Observable {
	
    private Mouse mouse = null;
    private Maze maze = null;
    
    public MazeMaster(Maze maze) {
    	resetMaze(maze);
    }
    
    public void move() {
    	if(!isInGoal()) {
	    	mouse.move();
	    	setChanged();
	    	notifyObservers();
    	}
    }
    
    public void back() {
    	mouse.back();
    	setChanged();
    	notifyObservers();
    }

    public Tile mazeAt(int x, int y) {
        return maze.MAZE[y][x];
    }
    
    public int mazeRows() {
    	return maze.getRows();
    }

    public int mazeCols() {
    	return maze.getCols();
    }

    public Tile mouseKnownAt(int x, int y) {
    	return mouse.mapAt(x, y);
    }

    public PathFindingStrategy getMouseStrategy() {
    	return mouse.getStrategy();
    }
    public void setMouseStrategy(PathFindingStrategy strategy) {
    	mouse.setStrategy(strategy);
    }

    public void resetMaze(Maze maze) {
    	this.maze = maze;
    	resetMouse();
    }

    public void resetMouse() {
    	mouse = new Mouse(maze, new RandomStrategy());
    	setChanged();
    	notifyObservers();
    }

    public boolean isInGoal() {
        return mouse.getPosition().x==maze.EXIT.x
        		&& mouse.getPosition().y==maze.EXIT.y;
    }
    
}