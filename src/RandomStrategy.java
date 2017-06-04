import java.util.Random;

public class RandomStrategy implements PathFindingStrategy {

	private static final int[][] dirs = {
			{-1, 0}, {0, -1}, {1, 0}, {0, 1}	
	};
	Random random = new Random();
	
	@Override
	public IntPoint nextPoint(Mouse mouse) {
		IntPoint pos = mouse.getPosition();
		int nextX = pos.x;
		int nextY = pos.y;
		while(nextX==pos.x && nextY==pos.y) {
			int d = random.nextInt(dirs.length);
			int tx = pos.x + dirs[d][0];
			int ty = pos.y + dirs[d][1];
			Tile t = mouse.mapAt(tx, ty);
			if(t==Tile.Empty || t==Tile.Check) {
				nextX = tx;
				nextY = ty;
			}
		}
		return new IntPoint(nextX, nextY);
	}

	@Override
	public void init(Mouse mouse) {
	}

}
