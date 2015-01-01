package com.wartisan.maze.prepareCells;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class Prototype {

	private int rows = 0, columns = 0;
	private ArrayList<Cell> allConnections;
	private ArrayList<CellSet> cellSetList = new ArrayList<CellSet>();

	public Prototype (int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		initMap();
	}

	public Prototype () {  // for test only, 3x3 is too small to play
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
		for (int i=0; i<rows;i++) {
			for (int j=0; j<columns; j++) {
				Cell x = new Cell(i,j);
				CellSet y = new CellSet(x);
				x.setCellSet(y);
				cellSetList.add(y); // now we got n*m cell sets, each contains only one cell
			}
		}
		//System.out.println(cellSetList.size());
		startBreakdownWalls();
	}

	private void startBreakdownWalls() {
		// pick randomly two cell sets
		// check if they can connect
		Random random = new Random();
		int numOfCellList = cellSetList.size();
		while (numOfCellList > 1) {
			int x = random.nextInt(numOfCellList);
			int y = 0;
			if (x==0) {
				y = 1;
			} else {
				y = x-1;
			}
			CellSet cellSet1 = cellSetList.get(x);
			CellSet cellSet2 = cellSetList.get(y);
			if (cellSet1.merge(cellSet2)) {
				cellSetList.remove(cellSet2); // cellSet2 no more needed, as all cells go into cellSet1
				addConnections(cellSet1.getConnections());
			}
			numOfCellList = cellSetList.size();
			System.out.println("now " + numOfCellList + " cell sets left...");
		}

		System.err.println("All done!");

	}

	private void addConnections(Cell[] cells) {
		allConnections.add(cells[0]);
		allConnections.add(cells[1]);
		System.out.println("added " + cells.length + " cells");
	}
	
	public ArrayList<Cell> getConnections() {
		return allConnections;
	}

	public static void main(String[] args) {
		Prototype x = new Prototype(9,9);
		//		System.out.println(x.getRows());
		//		Random random = new Random();
		//		int numOfCellList = 55;
		//		int x = random.nextInt(numOfCellList);
		//		int y = random.nextInt(numOfCellList);
		//		System.out.println("x="+x+", y=" + y);
	}
}
