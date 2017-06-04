import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

public enum MazeFactory {
	instance;
	
	private Random random = new Random();
	public Maze createMaze(int rows, int cols) { // rows and cols is must odd number
		if (rows%2 == 0) ++rows;
		if (cols%2 == 0) ++cols;
		boolean[][] maze = new boolean[rows][cols];
		Tile[][] tiles = new Tile[rows][cols];
		for(int r=0; r<rows; ++r) {
			Arrays.fill(maze[r], true);
			Arrays.fill(tiles[r], Tile.Wall);
		}

		int cell_row = rows / 2;
		int cell_col = cols / 2;
		boolean[][][] cell = new boolean[cell_row][cell_col][5];
		for (int i = 0; i < cell_row; i++) {
			for (int j = 0; j < cell_col; j++) {
				for (int k = 0; k < 4; k++)
					cell[i][j][k] = true;
				cell[i][j][4] = false;
			}
		}
		int row = 0;
		int col = 0;
		

		//create history
		Stack<IntPoint> history = new Stack<>();
		history.push(new IntPoint(row, col));
		while (!history.isEmpty()) {	// end if can't backtrack
			cell[row][col][4] = true;	//we visited this cell(row*col)
			char[] check = new char[4];				//where is open?
			int check_num = 0;
			if (col > 0 && cell[row][col - 1][4] == false) {
				check[check_num] = 0;	//left
				check_num++;
			}
			if (row > 0 && cell[row - 1][col][4] == false) {
				check[check_num] = 1;	//up
				check_num++;
			}
			if (col < cell_col-1 && cell[row][col + 1][4] == false) {
				check[check_num] = 2;	//right
				check_num++;
			}
			if (row < cell_row-1 && cell[row + 1][col][4] == false) {
				check[check_num] = 3;//down
				check_num++;
			}

			if (check_num != 0) {	//go if you can
				history.push(new IntPoint(row, col));	// stack where you are on history
				char move_direction = check[random.nextInt(check_num)];	//random select direction
				cell[row][col][3] = false;
				switch (move_direction) {
				case 0:	//left
					cell[row][col][2] = false;
					col--;
					cell[row][col][3] = false;
					break;

				case 1:	//up
					cell[row][col][1] = false;
					row--;
					cell[row][col][3] = false;
					break;
				case 2:	//right
					cell[row][col][3] = false;
					col++;
					cell[row][col][2] = false;
					break;
				case 3:	//down
					cell[row][col][3] = false;
					row++;
					cell[row][col][1] = false;
					break;
				}
			}
			else {//backtrack
				row = history.peek().x;
				col = history.peek().y;
				history.pop();
			}
			/*//debug
			for (int i = 0; i < cell_row; i++) {
				for (int j = 0; j < cell_col; j++) {
					cout << cell[i][j][4] << " ";
				}
				cout << endl;
			}
			cout << endl;
			*/
		}
		//write on maze
		for (int i = 0; i < cell_row; i++) {
			for (int j = 0; j < cell_col; j++) {
				if (!cell[i][j][0])
					maze[i * 2][j * 2] = false;
				if (!cell[i][j][1])
					maze[i * 2][j * 2+1] = false;
				if (!cell[i][j][2])
					maze[i * 2+1][j * 2] = false;
				if (!cell[i][j][3])
					maze[i * 2+1][j * 2+1] = false;
			}
		}
		maze[0][1] = false;	//starting point
		//create end point
		int end_floor = random.nextInt(4);	// direction of end point(0: left, 1: up, 2: right, 3: down)
		boolean end = false;	// if make end point?
		while (!end) {
			int temp;			// randomly point 
			switch (end_floor) {
			case 0:
				temp = random.nextInt(rows);
				if (!maze[temp][1]) {
					maze[temp][0] = false;
					end = true;
				}
				break;
			case 1:
				temp = random.nextInt(cols);
				if (!maze[1][temp]) {
					maze[0][temp] = false;
					end = true;
				}
				break;
			case 2:
				temp = random.nextInt(rows);
				if (!maze[temp][cols-2]) {
					maze[temp][cols-1] = false;
					end = true;
				}
				break;
			case 3:
				temp = random.nextInt(cols);
				if (!maze[rows-2][temp]) {
					maze[rows-1][temp] = false;
					end = true;
				}
				break;
			}
		}

		
		
		int punchNum = (int)(0.16*(double)rows*cols);	// punch times
		for (int i = 0; i < punchNum; i++) {
			int pr = -1;
			int pc = -1;
			while (pr < 1 || pr > rows - 2) {
				pr = random.nextInt(rows);
			}
			while (pc < 1 || pc > cols - 2) {
				pc = random.nextInt(cols);
			}
			maze[pr][pc] = false;
		}
		
		

		for(int r=0; r<rows; ++r) {
			for(int c=0; c<cols; ++c) {
				tiles[r][c] = maze[r][c]? Tile.Wall : Tile.Empty;
			}
		}
		ArrayList<IntPoint> list = new ArrayList<>();
		int[] rl = {0, rows-1};
		int[] cl = {0, cols-1};
		for(int r : rl) {
			for(int c=0; c<cols; ++c) {
				if(tiles[r][c]==Tile.Empty)
					list.add(new IntPoint(c,r));
			}
		}
		for(int c : cl) {
			for(int r=1; r<rows-1; ++r) {
				if(tiles[r][c]==Tile.Empty)
					list.add(new IntPoint(c,r));
			}
		}
		if(list.size()!=2) return null;
		return new Maze(tiles, list.get(0), list.get(1));
	}
}
