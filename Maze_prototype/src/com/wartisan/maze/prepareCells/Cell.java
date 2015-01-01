package com.wartisan.maze.prepareCells;

public class Cell {

	private int x, y;
	private CellSet cellSet;
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// tell which cell set this cell belongs to
	public void setCellSet(CellSet cellSet) {
		this.cellSet = cellSet; 
	}
	
	public CellSet getCellSet() {
		return cellSet;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	// for print only, no more needed
	public String toString() {
		return "("+getX()+","+getY()+")";
	}
	
	// for current node, we can tell if the given cell c2 is adjacent
	public boolean adjacent(Cell c2) {
		int x2 = c2.getX();
		int y2 = c2.getY();
		if ( (x==x2 && y==(y2-1)) ||
				(x==x2 && y==(y2+1)) ||
				(x==(x2-1) && y==y2) ||
				(x==(x2+1) && y==y2) ) {
			return true;
		}
		return false;
	}
	
}
