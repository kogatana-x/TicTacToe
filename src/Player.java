package project;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import dsUtils.TTTGUI;
import dsUtils.TTTPosition;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Player{
		public char type;
		public int no;
		public int s=1;
		
		public ArrayList<Board> moves;
		public HashMap<Board, Integer> boardData;
		public int outcome = 0;
	    
	    //interesting elements that are not needed for functionality
		public int totalWins = 0;
		public int totalLosses = 0;
		public int totalTies = 0;
		public int gamesPlayed = 0;
	    
		public Player(int i) {
			this.no=i;
			this.type = getType();
			this.boardData = new HashMap<Board,Integer>();
			
			//obtain Perfect Player's Move-Score DB
			if(this.type=='P') {initBoardData();} 
			
			if(i==-1) {this.s=2;}
			this.moves= new ArrayList<Board>(); 
	}
		private char getType() {
			char c;
			do {
				StdOut.printf("Player %d (H)uman, (R)andomAI, (L)earnedAI, or (P)PerfectAI ?\t", s);
				c = StdIn.readLine().charAt(0);
			}while(!validType(c));
			return c;
		}
		private boolean validType(char c) {
			if(c!='H' && c!='R' && c!='L' && c!='P') {System.out.println("Enter H, R, L, or P"); return false;}
			return true;
		}
		private void initBoardData() {
			min2max(new Board(),1); //X will always go first
		}
		
		private int min2max(Board board, int turn) {
			Integer score = boardData.get(board);
			//avoid re-evaluating board state
			if(score==null) {
				//determine game outcome
				score = board.winCombo();
				if(score==0 && !board.isFull()) {//look at the board's children to determine the score
			    	score=-turn; 
					for(Board next : board.nextBoards(turn)) {
						int nextV = min2max(next,-turn);
						//takes best value -- "X" maximizes, "O" minimizes 
						if(((nextV>score)&&(turn==1)) || ((nextV<score)&&(turn==-1))) {score=nextV;}
					}
				}
				//add this board with the best value from its children
				//or if its terminal state, add the terminal state
				boardData.put(board,score);
			}
			return score;
		}
					
		public TTTPosition move(Board board, TTTGUI GUI) {
			TTTPosition p;
			if(type=='H') {
				do{ 
					p = GUI.getMove();
				}while(board.get(p)!=0);
			}else if(type=='R'){ p = randomMove(board);}
			else if(type=='L') {p = learnedMove(board);}
			else {p = optimalMove(board);}
			board.put(p,no);
			//System.out.printf("Player %d - Moved: %d,%d\n",pl.s,po.getRow(),po.getCol());
			//board.display();
			return p;
		}
		private TTTPosition randomMove(Board board) {
			Random r = new Random();
			//obtain a, valid, random move
			ArrayList<TTTPosition> valid = board.validPo(); 
			return valid.get(r.nextInt(valid.size()));
		}

		private TTTPosition learnedMove(Board board) {
			TTTPosition best = randomMove(board); //generate random value as fallback
			
			int bestVal=0; int curVal = 0;
			Board compTable = new Board();
			ArrayList<TTTPosition> arr = board.validPo();
			
			//obtain the results of playing all the next possible moves
			if(!this.boardData.isEmpty()) {
				for(TTTPosition p : arr){
					compTable.putAll(board);
					compTable.put(p, this.no);
					//select the best move from what this player knows
					if(this.boardData.get(compTable)!=null) {
						curVal = this.boardData.get(compTable);
						if(bestVal<curVal){bestVal=curVal; best=p;}
					}
					
				}
			}
			compTable.putAll(board);
			compTable.put(best, this.no);
			//update the database
			this.moves.add(compTable); 
			return best;
		}

		 private TTTPosition optimalMove(Board board) {
			 	ArrayList<Board> nextMoves = board.nextBoards(this.no);
				int bestS=-this.no; Board bestM = new Board();
							
				for(Board next : nextMoves) {
					int currentS=boardData.get(next);
								
					if((no>0 && bestS<currentS) || ((no<0) && (bestS>currentS))) {
						bestS=currentS;
						bestM=next;
					}
		 		}
				
				return bestM.difference(board); 
			
		 }
		
		public void displayBoardData() {
			for(Map.Entry<Board,Integer> set: boardData.entrySet()) {
				set.getKey().display();
				System.out.println("SCORE: " + set.getValue()+"\n");
			}
		}
	}