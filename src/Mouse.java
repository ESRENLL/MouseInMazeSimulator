import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Mouse {
	
	public class MouseLog {
		public final IntPoint prevPoint;
		public final IntPoint[] liftedFog;
		public final boolean revisit;
		public MouseLog(IntPoint prevPoint, IntPoint[] liftedFog, boolean revisit) {
			this.prevPoint = prevPoint;
			this.liftedFog = liftedFog;
			this.revisit = revisit;
		}
	}

    private IntPoint currPos;
    private Tile map[][];
    private PathFindingStrategy strategy;
    private ArrayList<MouseLog> log = new ArrayList<>();
    private final Maze maze;

    public Mouse(Maze maze, PathFindingStrategy strategy) {
    	this.maze = maze;
    	currPos = maze.ENTRANCE;
    	int rows = maze.getRows();
    	int cols = maze.getCols();
    	map = new Tile[rows][cols];
    	for(int r=0; r<rows; ++r) {
    		Arrays.fill(map[r], Tile.Fog);
    	}
    	for(int y=currPos.y-1; y<=currPos.y+1; ++y) {
    		for(int x=currPos.x-1; x<=currPos.x+1; ++x) {
    			if(!isOutOfBound(x,y)) {
    				map[y][x] = maze.MAZE[y][x];
    			}
    		}
    	}
    	map[currPos.y][currPos.x] = Tile.Mouse;
    	
    	this.strategy = strategy;
    }

    public void move() {
    	IntPoint nextPoint = strategy.nextPoint(this);
    	ArrayList<IntPoint> liftedFogList = new ArrayList<>();
    	for(int y=nextPoint.y-1; y<=nextPoint.y+1; ++y) {
    		for(int x=nextPoint.x-1; x<=nextPoint.x+1; ++x) {
    			if(!isOutOfBound(x,y) && map[y][x]==Tile.Fog) {
    				liftedFogList.add(new IntPoint(x, y));
    				map[y][x] = maze.MAZE[y][x];
    			}
    		}
    	}
    	IntPoint[] liftedFog = new IntPoint[liftedFogList.size()];
    	liftedFogList.toArray(liftedFog);
    	boolean revisit = map[nextPoint.y][nextPoint.x]==Tile.Check;
    	log.add(new MouseLog(currPos, liftedFog, revisit));
    	map[currPos.y][currPos.x] = Tile.Check;
    	map[nextPoint.y][nextPoint.x] = Tile.Mouse;
    	currPos = nextPoint;
    }

    public void back() {
    	if(log.isEmpty()) return;
    	MouseLog l = log.get(log.size()-1);
    	map[currPos.y][currPos.x] = l.revisit? Tile.Check : Tile.Empty;
    	currPos = l.prevPoint;
    	map[currPos.y][currPos.x] = Tile.Mouse;
    	for(IntPoint p : l.liftedFog)
    		map[p.y][p.x] = Tile.Fog;
    	log.remove(log.size()-1);
    }

    public Tile mapAt(int x, int y) {
    	if(isOutOfBound(x,y)) return Tile.None;
    	return map[y][x];
    }
    
    public boolean isOutOfBound(int x, int y) {
    	return x<0 || y<0 || y>=map.length || x>=map[0].length;
    }

    public IntPoint getPosition() {
    	return currPos;
    }
    
    public PathFindingStrategy getStrategy() {
    	return strategy;
    }

    public void setStrategy(PathFindingStrategy strategy) {
    	this.strategy = strategy;
    	this.strategy.init(this);
    }
    
    public MouseLog[] getLog() {
    	MouseLog[] ret = new MouseLog[log.size()];
    	log.toArray(ret);
    	return ret;
    }

}