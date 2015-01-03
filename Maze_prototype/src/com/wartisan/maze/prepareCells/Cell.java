package com.wartisan.maze.prepareCells;

import java.util.HashSet;

public class Cell {

	private int x, y;
	private CellSet cellSet;
	// 任意给出一个点 A,能够得出
	// 1 该点的邻居都有谁
	// 2 第一个不再同一个集合里面的邻居B
	// 2.1 如果B是终点，连接，结束
	// 2.2 否则
	// 3 让两个点A和B连接到一个集合里面
	// 4 B是不是终点
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

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
	
	public String toString() {
		return "("+getX()+","+getY()+")";
	}
	
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
	
	public Cell[] getNeighbors2(int rows, int columns) {
		//System.err.println("rows=" + rows + ", " +"cols=" + columns + ", " + "x="+x+", y=" + y);
		Cell[] result = null;
		if (x == 0) {  // first row
			if (y == 0) { // first column
				result = new Cell[2];
				result[0] = new Cell(x+1,y);
				result[1] = new Cell(x,y+1);
			} else if (y == (columns-1)) { //last column
				result = new Cell[2];
				result[0] = new Cell(x-1,y);
				result[1] = new Cell(x,y+1);
			} else {  // middle
				result = new Cell[3];
				result[0] = new Cell(x-1,y);
				result[1] = new Cell(x+1,y);
				result[2] = new Cell(x,y+1);
			}
		} else if (x == (rows-1)) { // last row
			if (y==0) { // first column
				result = new Cell[2];
				result[0] = new Cell(x+1,y);
				result[1] = new Cell(x,y-1);
			} else if (y == (columns-1)) { //last column
				result = new Cell[2];
				result[0] = new Cell(x-1,y);
				result[1] = new Cell(x,y-1);
			} else {  // middle
				result = new Cell[3];
				result[0] = new Cell(x-1,y);
				result[1] = new Cell(x+1,y);
				result[2] = new Cell(x,y-1);
			}
			
		} else { // middle row
			if (y==0) { // first column
				result = new Cell[3];
				result[0] = new Cell(x+1,y);
				result[1] = new Cell(x,y-1);
				result[2] = new Cell(x,y+1);
			} else if (y == (columns-1)) { //last column
				result = new Cell[3];
				result[0] = new Cell(x-1,y);
				result[1] = new Cell(x,y-1);
				result[2] = new Cell(x,y+11);
			} else {  // middle
				result = new Cell[4];
				result[0] = new Cell(x-1,y);
				result[1] = new Cell(x+1,y);
				result[2] = new Cell(x,y-1);
				result[3] = new Cell(x,y+1);
			}
		}
	
//		for (int i=0; i<result.length;i++) {
//			System.err.println(result.length + " " + result[i]);
//		}
		return result;
		
	}


}
