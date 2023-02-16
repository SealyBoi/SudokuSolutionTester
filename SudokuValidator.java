import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.util.Arrays;

class Row extends Thread {
	int[][] sol;
	BufferedWriter bw;
	int numOfCorrectRows = 0;
	Row (int[][] sol, BufferedWriter bw){
		this.sol = sol;
		this.bw = bw;
	}
	public void run() {
		int[] row = new int[9];
		try {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					row[j] = sol[i][j];
				}
				Arrays.sort(row);
				for (int k = 1; k < 10; k++) {
					if (row[k-1] != k) {
						k = 10;
						bw.write("[Thread 1] Row " + i + ": Invalid\n");
					} else if (k == 9) {
						bw.write("[Thread 1] Row " + i + ": Valid\n");
						numOfCorrectRows++;
					}
				}
			}
		} catch (IOException e) {
			System.out.println("An error occured");
			e.printStackTrace();
		}
	
	}

	public int getRows() {
		return numOfCorrectRows;
	}
}

class Column extends Thread {
	int[][] sol;
	BufferedWriter bw;
	int numOfCorrectCols = 0;
	Column (int[][] sol, BufferedWriter bw) {
		this.sol = sol;
		this.bw = bw;
	}
	public void run() {
		int[] col = new int[9];
		try {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					col[j] = sol[j][i];
				}
				Arrays.sort(col);
				for (int k = 1; k < 10; k++) {
					if (col[k-1] != k) {
						k = 10;
						bw.write("[Thread 2] Column " + i + ": Invalid\n");
					} else if (k == 9) {
						bw.write("[Thread 2] Column " + i + ": Valid\n");
						numOfCorrectCols++;
					}
				}
			}
		} catch (IOException e) {
			System.out.println("An error occured");
			e.printStackTrace();
		}
	}

	public int getCols() {
		return numOfCorrectCols;
	}
}

class Grid extends Thread {
	int[][] sol;
	BufferedWriter bw;
	int numOfCorrectGrids = 0;
	Grid (int[][] sol, BufferedWriter bw) {
		this.sol = sol;
		this.bw = bw;
	}
	public void run() {
		int[] grid;
		try {
			for (int i = 0; i < 9; i+=3) {
				for (int j = 0; j <9; j+=3) {
					grid = new int[]{sol[i][j],sol[i][j+1],sol[i][j+2],sol[i+1][j],sol[i+1][j+1],sol[i+1][j+2],sol[i+2][j],sol[i+2][j+1],sol[i+2][j+2]};
					Arrays.sort(grid);
					for (int k = 1; k < 10; k++) {
						if (grid[k-1] != k) {
							k = 10;
							bw.write("[Thread 3] Subgrid R" + (i+1) + (i+2) + (i+3) + "C" + (j+1) + (j+2) + (j+3) + ": Invalid\n");
						} else if (k == 9) {
							bw.write("[Thread 3] Subgrid R" + (i+1) + (i+2) + (i+3) + "C" + (j+1) + (j+2) + (j+3) + ": Valid\n");
							numOfCorrectGrids++;
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println("An error occured");
			e.printStackTrace();
		}
	}

	public int getGrids() {
		return numOfCorrectGrids;
	}
}

public class SudokuValidator {
	public static void main(String[] args) throws IOException {
		BufferedReader br;
		int[][] sol = new int[9][9];
		try {
			br = new BufferedReader(new FileReader(args[0]));
			String line;
			String[] row = new String[9];

			for (int i = 0; i < 9; i++) {
				line = br.readLine();
				row = line.split(" ");
				for (int j = 0; j < 9; j++) {
					sol[i][j] = Integer.parseInt(row[j]);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File outputFile = new File(args[1]);
		outputFile.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(args[1]));
		Row rowThread = new Row(sol,bw);
		Column colThread = new Column(sol,bw);
		Grid gridThread = new Grid(sol,bw);
		rowThread.start();
		colThread.start();
		gridThread.start();
		while(rowThread.isAlive() || colThread.isAlive() || gridThread.isAlive()) {
			//wait for threads to finish
		}
		bw.write("Valid Rows: " + rowThread.getRows() + "\n");
		bw.write("Valid Columns: " + colThread.getCols() + "\n");
		bw.write("Valid Grids: " + gridThread.getGrids() + "\n");
		if (rowThread.getRows() == 9 && colThread.getCols() == 9 && gridThread.getGrids() == 9) {
			bw.write("This Sudoku solution is: Valid\n");
		} else {
			bw.write("This Sudoku solution is: Invalid\n");
		}
		bw.close();
	}
}
