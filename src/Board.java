package project;

import java.util.ArrayList;

import dsUtils.TTTPosition;

public class Board{
	private int[][] board;
	private int turn=1;
	
	public Board() {this.board = new int[3][3]; this.clear();}
	
	public void clear() {
		for(int x = 0; x < 3; x++) {
			for(int y = 0; y < 3; y++) {
				board[x][y]=0;
			}
		}
		turn=1;
	}

	public boolean isEmpty() {return turn==1;}
	
	public void next() { turn++;}
	public int getTurn() {return turn;}
	
	public boolean isFull() {
		for(int x = 0; x < 3; x++) {
			for(int y = 0; y < 3; y++) {
				if(board[x][y]==0) {return false;}
			}
		}
		return true;
	}
	
	public void display() {
		//String player;
		//if(turn%2==0) {player = "Player 2";}
		//else { player = "Player 1";}
		//System.out.println(player+"'s Turn");
		for(int x = 0; x < 3; x++) {
			for(int y=0; y<3; y++) {
				String pos = " ";
				if(board[x][y]==-1) {pos="O";}
				else if(board[x][y]==1) {pos="X";}
				System.out.printf("| %s |", pos);
			}
			System.out.println("");
		}
		//System.out.println("\n");
	}
	
	public void put(TTTPosition p, int i) { board[p.getRow()][p.getCol()] = i;}
	//public void undo(TTTPosition p) { put(p,0); }
	public void putAll(Board other) {
		TTTPosition p;
		for(int x=0; x < 3; x++) {
			for(int y=0; y < 3; y++) {
				p = new TTTPosition(x,y);
				this.put(p, other.get(p));
				this.next();
			}
		}
	}
	public int get(TTTPosition p) {	return board[p.getRow()][p.getCol()];}
	
	public ArrayList<Board> nextBoards(int turn){
		ArrayList<Board> nextBoards = new ArrayList<Board>();
		
		ArrayList<TTTPosition> arr = validPo();
		Board temp;
		for(TTTPosition p : arr) {
			temp = new Board();
			temp.putAll(this);
			temp.put(p,turn);
			nextBoards.add(temp);
		}
		return nextBoards;
	}
	public void displayNextBoards(int turn) {
		ArrayList<Board> nextBoards = nextBoards(turn);
		for (Board b : nextBoards) {
			b.display();
			System.out.println();
		}
	}
	public ArrayList<TTTPosition> validPo() {
		ArrayList<TTTPosition> val = new ArrayList<TTTPosition>();
		TTTPosition p;
		//if(this.winCombo()!=0) {return null;}   -- test for insurance 
		for(int x=0; x<3; x++) {
			for(int y=0; y<3; y++) {
				p = new TTTPosition(x,y);
				if(this.get(p)==0) {
					//System.out.print(" {"+x + "," +y+"} ");
					val.add(p);}
			}
			//System.out.println();
		}
		//System.out.println("DONE");
		return val;
	}
	public TTTPosition difference(Board other) {
		for(int x=0; x<3;x++) {
			for(int y=0; y<3; y++) {
				if(other.board[x][y]!=this.board[x][y]) {return new TTTPosition(x,y);}
			}
		}
		return null;
	}
	

	public int winCombo() {
		int sum;

		//CHECK ROWS
		for(int x=0; x<3; x++) {
			sum=0;
			for(int y=0; y<3; y++) { sum+=get(new TTTPosition(x,y));}
			if(sum==-3) {return -1;}
			else if(sum==3) {return 1;}
		}
		//CHECK COLUMNS
		for(int x=0; x<3; x++) {
			sum=0;
			for(int y=0; y<3; y++) { sum+=get(new TTTPosition(y,x));}
			if(sum==-3) {return -1;}
			else if(sum==3) {return 1;}
		}
		//CHECK DIAGONALS
		sum = get(new TTTPosition(0,0)) + get(new TTTPosition(1,1)) + get(new TTTPosition(2,2));
		if(sum==-3) {return -1;}
		if(sum==3) {return 1;}
		
		sum = get(new TTTPosition(2,0))+ get(new TTTPosition(1,1)) + get(new TTTPosition(0,2));
		if(sum==-3) {return -1;}
		if(sum==3) {return 1;}

		return 0;
	}
	
	@Override
	public boolean equals(Object o) {
		Board other = (Board) o;
		for(int x = 0; x < 3; x++) {
			for(int y = 0; y < 3; y++) {
				if(this.board[x][y]!=other.board[x][y]) {return false;}
			}
		}
		return true;
	}
	@Override
	public int hashCode() { 
		int[] prime = {23,31,41,53,61,71,83,97};
		int hash=0;
		for(int x=0;x<3;x++) {
			for(int y=0;y<3;y++) {
				hash+= board[x][y]*prime[x]; x++;
			}
		}
		return hash;
	}
	
	/*@Override
	public int hashCode() { //Arrays class's hashcode function for multi-dimensional arrays of primitive types
		return java.util.Arrays.deepHashCode(board);    //hashcode for int[] will have the same values in groups of 0:1, -1:-30, 1:32 
	}*/
	
	/*@Override
	public int hashCode(){  //bijective algorithm for each pos in table
	 	int h = (y +  ((x+1)/2));
	    return = x + (h*h);

	 }*/

}


















