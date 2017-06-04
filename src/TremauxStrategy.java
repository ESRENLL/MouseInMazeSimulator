import java.util.Random;
import java.util.Stack;

public class TremauxStrategy implements PathFindingStrategy {

	private static final int[][] dirs = {
			{-1, 0}, {0, -1}, {1, 0}, {0, 1}	
	};
	private Stack<IntPoint> prevPointStk = new Stack<>();
	
	@Override
	public IntPoint nextPoint(Mouse mouse) {
		IntPoint p = mouse.getPosition();
		for(int d=0; d<dirs.length; ++d) {
			int mx = p.x + dirs[d][0];
			int my = p.y + dirs[d][1];
			if(mouse.mapAt(mx, my)==Tile.Empty) {
				prevPointStk.push(new IntPoint(mx, my));
				return prevPointStk.peek();
			}
		}
		return prevPointStk.pop();
	}

	@Override
	public void init(Mouse mouse) {
		Mouse.MouseLog[] mLog = mouse.getLog();
		for(Mouse.MouseLog ml : mLog)
			prevPointStk.push(ml.prevPoint);
	}

}
