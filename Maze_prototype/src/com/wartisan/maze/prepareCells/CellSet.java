package com.wartisan.maze.prepareCells;

import java.util.HashSet;

public class CellSet {

	private HashSet<Cell> cells = new HashSet<Cell>();
	private Cell[] cellPositions = new Cell[2];
	boolean canMerge = false;

	public CellSet(Cell x) {
		cells.add(x);  // when create/initialize this cell set, x is its first cell
		x.setCellSet(this);  // the x now belongs to current cell set
	}

	public HashSet<Cell> getCellSet() {
		return cells;
	}

	public Cell[] getConnections() {
		if (canMerge) {
			return cellPositions;
		}
		return null;
	}

	public boolean merge(CellSet cellSet) {
		canMerge = false;
		for (Cell c1 : this.getCellSet()) {
			for (Cell c2 : cellSet.getCellSet()) {
				if (c1.adjacent(c2) && 
						(c1.getCellSet() != c2.getCellSet())
						&& !canMerge) {
					System.out.println("("+c1.getX()+","+c1.getY() + ") connected to (" + c2.getX() + "," + c2.getY()+")");
					canMerge = true;
					cellPositions[0] = c1;
					cellPositions[1] = c2;
				} else {
					// do nothing
				}
			}
		}
		
		if (canMerge) {
			System.out.println("Merged two sets...");
			for (Cell c2 : cellSet.getCellSet()) {
				cells.add(c2);  // current cell set add cell c2 into own set
				c2.setCellSet(this); // c2 claims it belongs to this cell set
				// todo: need a arraylist to store all these connections
			}
			return true;
		}
		return false;
	}

	public boolean find(Cell x) {
		return contains(x);
	}

	public boolean contains(Cell x) {
		return cells.contains(x);
	}
	
}
