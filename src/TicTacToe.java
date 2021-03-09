package project;
import java.util.ArrayList;


import dsUtils.TTTGUI;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class TicTacToe {
	private TTTGUI GUI;
    private Player p1;
    private Player p2;
    private Board board;
    
	public TicTacToe() {
		this.p1 = new Player(1);
		this.p2 = new Player(-1);
		
		this.GUI = new TTTGUI(150);
		this.board = new Board();
		
	}

	
	public static void main(String[] args) {
		TicTacToe TTT = new TicTacToe();
		StdOut.printf("Player 1: %c     Player 2: %c%n", TTT.p1.type, TTT.p2.type);
		//program
		do {
			//robot training option
			int count=1;
			if(TTT.p1.type!='H'&&TTT.p2.type!='H') {
				while(true){
				    try {
						StdOut.printf("How many games would you like to play?\t");
						count = Integer.parseInt(StdIn.readLine());
				        break;
				    } catch (NumberFormatException e) {
				        System.out.println("Please enter a valid integer.");
				    }
				}
			}
			//game
			do{
				TTT.GUI.clearBoard();
				TTT.board.clear();
				boolean cont = true;

				//turns
				do{
					if(TTT.isDraw()) {cont=false;}
					else{cont=TTT.turn();}
				}while(cont);
				
				if(TTT.p1.type=='L') {TTT.updateMoves(TTT.p1);}
				if(TTT.p2.type=='L') {TTT.updateMoves(TTT.p2);}
				TTT.calcGameStuff(TTT.p1); TTT.calcGameStuff(TTT.p2);

				count --;
			}while(count>0);
			if(TTT.p1.type!='H'&&TTT.p2.type!='H') {TTT.displayGameStuff();}
		}while(TTT.GUI.askPlayAgain());
		System.exit(0);
	}
	private void calcGameStuff(Player p) {
		p.gamesPlayed++;
		if(p.outcome>0) {p.totalWins++;}
		else if(p.outcome<0) {p.totalLosses++;}
		else {p.totalTies++;}
		p.outcome=0;
	}
	private void displayGameStuff() {
		System.out.println("Results");
		System.out.printf("\nPlayer 1: \n\tGames Played: %d | Games Won: %d | Games Lost: %d | Games Tied: %d\n",p1.gamesPlayed, p1.totalWins, p1.totalLosses, p1.totalTies);
		System.out.printf("\nPlayer 2: \n\tGames Played: %d | Games Won: %d | Games Lost: %d | Games Tied: %d\n",p2.gamesPlayed, p2.totalWins, p2.totalLosses, p2.totalTies);
	}
	
	private boolean turn() {
	    Player p;
	    int turn = board.getTurn();
		if(turn%2==0) {p=p2;}
		else {p=p1;}
		GUI.makeMove(p.move(board,GUI), p.s);
		board.next();
		
		int winner = board.winCombo();
		if(winner!=0) {
			winner(p); //announce winner to stdout
			if(turn%2==0) {p2.outcome=1;p1.outcome=-1;}
			else {p2.outcome=-1;p1.outcome=1;}
			return false;
		}
		return true;
	}
	
	
	
	private boolean isDraw() {
		if(board.isEmpty()) {return false;}
		if(board.isFull()) {
			if(p1.type=='H'||p2.type=='H') {System.out.println("Draw! Try again next time");}
			return true;
		}
		return false;
	}
	private void winner(Player p) {
		if(p.type=='H') {System.out.printf("Good job, Player %d. You won. But are you winning at life?\n", p.s);}
		if(-p.no==-1&&p2.type=='H') {System.out.printf("Sorry Player %d. You lost. Try harder next time.\n", p2.s);}
		else if(-p.no==1&&p1.type=='H') {System.out.printf("Sorry Player %d. You lost. Try harder next time.\n", p1.s);}
	}
	
	//learning player moves get updated 
	private void updateMoves(Player pl) {
		for(Board p:pl.moves) {
			if(pl.boardData.get(p)==null) {pl.boardData.put(p, pl.outcome);}
			else{pl.boardData.put(p, pl.boardData.get(p)+pl.outcome);}
		}
		pl.moves = new ArrayList<Board>();
	}
	

}
