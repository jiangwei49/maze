package com.wartisan.maze.prepareCells;

import java.util.ArrayList;
import java.util.Random;

public class Prototype {

	private int rows = 0, columns = 0;
	@SuppressWarnings("unused")
	private Cell[][] cells;
	private ArrayList<Cell> allConnections = new ArrayList<Cell>();
	private ArrayList<CellSet> cellSetList = new ArrayList<CellSet>();
	private Cell startCell, endCell;

	public Prototype (int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		initMap();
	}

	public Prototype () {
		this.rows = 3;
		this.columns = 3;
		initMap();
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public void initMap() {
		//		Cell x = new Cell(0,0);
		//		startCell = x;
		//		CellSet y = new CellSet(x);
		//		x.setCellSet(y);
		//		cellSetList.add(y);
		//		
		//		x = new Cell(rows-1, columns-1);
		//		endCell = x;
		//		y = new CellSet(x);
		//		x.setCellSet(y);
		//		cellSetList.add(y);

		for (int i=0; i<rows;i++) {
			for (int j=0; j<columns; j++) {
				createCell(i,j);
			}
		}
		//System.out.println(cellSetList.size());

		startBreakdownWalls();
	}

	private void startBreakdownWalls() {
		int totalCells = rows * columns;

		// pick randomly two numbers in the map
		// check if they are in the same set
		Random random = new Random();
		int numOfCellList = cellSetList.size();
		while (numOfCellList > 1) {

			int y = random.nextInt(numOfCellList);
			CellSet cellSet2 = cellSetList.get(y);

			cellSetList.remove(cellSet2); // remove cellSet2 so next random will pick is again
			numOfCellList = cellSetList.size();

			int x = random.nextInt(numOfCellList);
			CellSet cellSet1 = cellSetList.get(x);

			if (cellSet1.merge(cellSet2)) {
				//cellSetList.remove(cellSet2);
				addConnections(cellSet1.getConnections());
			} else {
				cellSetList.add(cellSet2);  // if can't merge, add cellSet2 back to the list
			}
			numOfCellList = cellSetList.size();
			//System.out.println("Now " + numOfCellList + " cell sets left...");


			if (totalCells < (numOfCellList*4)) {
				if (findRoute()) {
					numOfCellList = 0;
				}
			}
		}

		System.err.println("All done!");

	}

	private boolean findRoute() {
		for (int i=0; i<cellSetList.size();i++) {
			CellSet x = cellSetList.get(i);
			if (x.contains(startCell) && (x.contains(endCell))) {
				System.err.println("提前连接结束！");
				return true;
			}
		}
		return false;
	}

	private void createCell(int x, int y) {
		Cell cell = new Cell(x,y);
		CellSet cellSet = new CellSet(cell);
		cell.setCellSet(cellSet);
		cellSetList.add(cellSet);

		if (x==0 && y==0) {
			startCell = cell;
		}

		if (x==(rows-1) && y==(columns-1)) {
			endCell = cell;
		}
	}

	//	private void startDrawMap() {
	//		Cell currentCell = cells[0][0];
	//		//System.out.println("xxxxxxxxxxxx: " + currentCell);
	//		//mazeMap.add(currentCell);
	//
	//		connectNextCell(currentCell);
	//	}

	//	private void connectNextCell(Cell currentCell) {
	//		System.err.println("xxxxxxxxxxxx: " + currentCell);
	//		if (currentCell == cells[rows-1][columns-1]) {
	//			// we got a match!
	//			for (Cell each : mazeMap) {
	//				System.err.println(each);
	//			}
	//			return;
	//		} else {
	//
	//			Cell[] neighbors = currentCell.getNeighbors(rows, columns);
	//			//		for (int i=0; i<neighbors.length; i++) {
	//			//			System.out.println(neighbors[i]);
	//			//		}
	//			boolean found = false;
	//			for (int i=0; i< neighbors.length;i++) {
	//				if (!found) {
	//					Cell nextCell = neighbors[i];
	//					if (mazeMap.contains(nextCell)) {
	//						// do nothing, next cell is already in the set
	//					} else {
	//						found = true;
	//						mazeMap.add(nextCell);
	//						//System.out.println(nextCell);Cell currentCell = cells[0][0];
	//						connectNextCell(nextCell); // use the nextCell to start a further route
	//					}
	//				}
	//			}
	//
	//			if (!found) {
	//				for (int i=mazeMap.size()-1; i>=0;i--) {
	//					Cell previousCell = mazeMap.get(i-1);
	//					connectNextCell(previousCell);
	//				}
	//			}
	//
	//		} 
	//
	//	}

	private void addConnections(Cell[] cells) {
		allConnections.add(cells[0]);
		allConnections.add(cells[1]);
	}

	public void printConnections() {
		for (int i=0; i<allConnections.size(); i++) {
			Cell c1 = allConnections.get(i);
			Cell c2 = allConnections.get(i+1);
			i++;
			System.out.println(c1 + "-->" + c2);
		}
	}

	public ArrayList<Cell> getConnections() {
		return allConnections;
	}

	public static void main(String[] args) {
		Prototype x = new Prototype(9,9);
		x.printConnections();
		//		System.out.println(x.getRows());
		//		Random random = new Random();
		//		int numOfCellList = 55;
		//		int x = random.nextInt(numOfCellList);
		//		int y = random.nextInt(numOfCellList);
		//		System.out.println("x="+x+", y=" + y);
	}
}
