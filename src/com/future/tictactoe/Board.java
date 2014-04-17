package com.future.tictactoe;

import java.util.Vector;

import com.codename1.ui.*;
import com.codename1.ui.util.*;

public class Board {
	private BoardCell[][] board;
	private GameController game;
	private History history;
	private int m_rows;
	private int m_cols;
	private boolean empty;
	private int moves;
	
	public Board(GameController game, Form form){
		UIBuilder builder = new UIBuilder();
		board = new BoardCell[3][];
		for (int i = 0; i < board.length; i++) {
			board[i] = new BoardCell[3];
		}
		m_rows = 3;
		m_cols = 3;
		moves = 0;
		this.game = game;
		empty = true;
		int count = 1;
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++, count++){
				Label mark = (Label) builder.findByName(i+""+j+"_cell_label", form);
				mark.setPreferredW((int) (form.getWidth()*0.8/3));
				mark.setPreferredH((int) (form.getWidth()*0.8/3));
				Container c = (Container) builder.findByName(count+"_cell_container", form);
				board[i][j] = new BoardCell(mark, c);
				board[i][j].setPlayer(null);
			}
		}
		history = new History();
	}
	
	public Board(Board other){
		board = new BoardCell[3][];
		for (int i = 0; i < board.length; i++) {
			board[i] = new BoardCell[3];
		}
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				board[i][j] = new BoardCell(new Label(), null);
				board[i][j].setPlayer(other.board[i][j].getPlayer());
			}
		}
		moves = other.moves;
		m_rows = other.m_rows;
		m_cols = other.m_cols;
		game = other.game;
		empty = other.empty;
		history = new History(other.history);
	}
	
	public boolean isValidMove(Move m) {
		int i=m.getRow(), j = m.getCol();
		return board[i][j].getPlayer() == null;
	}
	
	public void updateMove(Move m) {
		int i = m.getRow(), j = m.getCol();
		board[i][j].setPlayer(m.getPlayer());
		empty = false;
		moves++;
		history.newTurn();
	}
	
	public int[][][] getStat(){
		return history.toArray();
	}
	
	public boolean isEmpty(){
		return empty;
	}
	
	public boolean isFull(){
		return moves == 9;
	}
	
	protected BoardCell[] checkForWin() {
		Sequence sequence;
		if((sequence = checkColumns())!=null){
			return sequence.getPieces();
		}
		if((sequence = checkDiagonalDown())!=null){
			return sequence.getPieces();
		}
		if((sequence = checkDiagonalUp())!=null){
			return sequence.getPieces();
		}
		if((sequence = checkRows())!=null){
			return sequence.getPieces();
		}
		return null;
	}

	/*private BoardCell[] checkForDiagonalSequence() {
		int player=-1;
		BoardCell[] sequence = new BoardCell[3];
		int count = 0;
		for(int j=0; j<3; j++){
			if(board[j][j].getPlayer()!=null){
				if(player==-1)
					player = board[j][j].getPlayer().getNum();
				else{
					if(player!=board[j][j].getPlayer().getNum())
						break;
				}
				sequence[j] = board[j][j];
				count++;
			}
		}
		if(count==3){
			return sequence;
		}
		player = -1;
		count = 0;
		for(int j=0; j<3; j++){
			if(board[j][2-j].getPlayer()!=null){
				if(player==-1)
					player = board[j][2-j].getPlayer().getNum();
				else{
					if(player!=board[j][2-j].getPlayer().getNum())
						break;
				}
				sequence[j] = board[j][2-j];
				count++;
			}
		}
		if(count==3){
			return sequence;
		}
		return null;
	}

	private BoardCell[] checkForColSequence() {
		boolean found = false;
		BoardCell[] sequence = new BoardCell[3];
		for(int i=0; i<3; i++){
			int player=-1;
			int count = 0;
			for(int j=0; j<3; j++){
				if(board[j][i].getPlayer()!=null){
					if(player==-1)
						player = board[j][i].getPlayer().getNum();
					else{
						if(player!=board[j][i].getPlayer().getNum())
							break;
					}
					sequence[j] = board[j][i];
					count++;
				}
			}
			if(count==3){
				found = true;
				break;
			}
		}
		if(found)
			return sequence;
		return null;
	}

	private BoardCell[] checkForRowSequence() {
		boolean found = false;
		BoardCell[] sequence = new BoardCell[3];
		for(int i=0; i<3; i++){
			int player=-1;
			int count = 0;
			for(int j=0; j<3; j++){
				if(board[i][j].getPlayer()!=null){
					if(player==-1)
						player = board[i][j].getPlayer().getNum();
					else{
						if(player!=board[i][j].getPlayer().getNum())
							break;
					}
					sequence[j] = board[i][j];
					count++;
				}
			}
			if(count==3){
				found = true;
				break;
			}
		}
		if(found)
			return sequence;
		return null;
	} */
	
	
	private Sequence checkDiagonalUp(){
		//from southeast to northwest
		BoardCell cell;
		int x = 0;
		int y = 0;
		while (y<m_rows){
			Sequence s = null;
			boolean open = false;
			for(int i=0 ; y-i>=0 ; i++){
				cell = board[x+i][y-i];
				if (cell.getPlayer()!=null){
					if(s==null){
						s = new Sequence(cell.getPlayer().getNum(), cell);
						if (open) s.setOpen();
						open = false;
					}
					else{
						if (s.getColor()==cell.getPlayer().getNum()){
							if (s.grow(cell)) return s;
						}
						else{
							if(s.isOpen() && s.getLength()>1) history.addSequence(s);
							s = new Sequence(cell.getPlayer().getNum(), cell);
						}
					}
				}
				else{
					if(s!=null && s.getLength()>1) history.addSequence(s);
					open = true;
					s = null;
				}
			}
			if (s!=null && s.isOpen() && s.getLength()>1) history.addSequence(s);
			y++;
		}
		return null;
	}//checkDiagonalFromDown

	/*********************************************************/

	private Sequence checkDiagonalDown(){
		//from southwest to north east  
		BoardCell cell;
		int x = 0;
		int y = m_rows;
		while (y>=0){
			Sequence s = null;
			boolean open = false;
			for(int i=0 ; y+i<m_rows ; i++ ){
				cell = board[x+i][y+i];
				if (cell.getPlayer()!=null){
					if(s==null){
						s = new Sequence(cell.getPlayer().getNum(), cell);
						if (open) s.setOpen();
						open = false;
					}
					else{
						if (s.getColor()==cell.getPlayer().getNum()){
							if (s.grow(cell)) return s;
						} 
						else{
							if(s.isOpen() && s.getLength()>1) history.addSequence(s);
							s = new Sequence(cell.getPlayer().getNum(), cell);
						}
					}
				}
				else{
					if(s!=null && s.getLength()>1) history.addSequence(s);
					open = true;
					s = null;
				}
			}
			if (s!=null && s.isOpen() && s.getLength()>1) history.addSequence(s);
			y--;
		}
		return null;
	}//checkDiagonalFromUp

	/*********************************************************/

	private Sequence checkColumns(){
		BoardCell cell;
		int i=0;
		while (i<m_rows){
			Sequence s = null;
			boolean open = false;
			for (int j=0;j<m_cols;j++) {
				cell=board[j][i];
				if (cell.getPlayer()!=null) {
					if (s==null){
						s = new Sequence(cell.getPlayer().getNum(), cell);
						if (open) s.setOpen();
						open = false;
					}
					else{
						if (s.getColor()==cell.getPlayer().getNum()){
							if (s.grow(cell)) return s;		//victory!!
						}
						else{
							if(s.isOpen() && s.getLength()>1) history.addSequence(s);
							s = new Sequence(cell.getPlayer().getNum(), cell);
						}
					}
				}
				else {
					if(s!=null && s.getLength()>1)
						history.addSequence(s);
					open = true;
					s = null;
				}
			}
			if (s!=null && s.isOpen() && s.getLength()>1)
				history.addSequence(s);
			i++;
		}
		return null;
	}//checkRows
	
	/*********************************************************/

	private Sequence checkRows(){
		BoardCell cell;
		int i=0;
		while (i<m_cols) {
			Sequence s = null;
			for (int j=0 ; j<m_rows ; j++) {
				cell=board[i][j];
				if (cell.getPlayer()!=null) {
					if (s==null)	s = new Sequence(cell.getPlayer().getNum(), cell);
					else{
						if (s.getColor()==cell.getPlayer().getNum()){
							if (s.grow(cell)) return s;		//victory!!
						}
						else{
							//if (s.getLength()>1) m_history.reduceSequence(s);
							s = new Sequence(cell.getPlayer().getNum(), cell);
						}
					}
				}
				else {
					if (s!=null && s.getLength()>1) history.addSequence(s);
					break;
				}
			}
			i++;
		}
		return null;
	}//checkColumns

	
private class History{
		
		Vector m_turns;
		
		/********************CONSTRUCTORS*****************/
		
		public History(){
			m_turns = new Vector();
			m_turns.addElement(new Stat());
		}
		public History(History h){
			
			m_turns = new Vector();
			//for (int i=0;i<h.m_turns.size();++i)
				//m_turns.addElement(h.m_turns.elementAt(i));
			m_turns.addElement(h.m_turns.lastElement());
		}

		
		/********************************************/
		
		public void addSequence(Sequence s){
			Stat stat = (Stat)(m_turns.lastElement());
			stat.addSequence(s.getColor(), s.getLength());
		}
		
		public void reduceSequence(Sequence s){
			Stat stat = (Stat)(m_turns.lastElement());
			stat.reduceSequence(s.getColor(), s.getLength());
		}
		
		public void newTurn(){
			m_turns.addElement(new Stat());
		}
		
		/***************************************************/

		public int getOpenSeq(int player, int length, int turn){
			Stat s = (Stat)(m_turns.elementAt(turn));
			if (s==null) return 0;
			return s.getStats(player, length);
		}
		
		/***************************************************/

		public int[][][] toArray(){
			/*int[][][] ans = new int[m_turns.size()][2][2];
			Enumeration it = m_turns.elements();
			int i=0;
			
			while(it.hasMoreElements()){
				Stat stat = (Stat)(it.nextElement());
				ans[i++] = stat.getArray();
			}
			
			return ans;*/
			int[][][] ans = new int[1][][];
			for (int i = 0; i < ans.length; i++) {
				ans[i] = new int[2][];
				for (int j = 0; j < ans[i].length; j++) {
					ans[i][j] = new int[2];
				}
			}
			//Enumeration it = m_turns.elements();
			//int i=0;
			
			//while(it.hasMoreElements()){
				Stat stat = (Stat)(m_turns.lastElement());
				ans[0] = stat.getArray();
			//}
			
			return ans;
		}
		
		/***************************************************/
		
		private class Stat{
			
			private int[][] m_array;
			
			/***********CONSTRUCTORS********************/
			
			public Stat(){
				m_array = new int[2][];
				for (int i = 0; i < m_array.length; i++) {
					m_array[i] = new int[2];
				}
			}
			
			/********************************************/
			
			public void addSequence(int color, int length){
				m_array[color][length-2]++;
			}
			
			public void reduceSequence(int color, int length){
				m_array[color][length-2]--;
			}
			
			public int getStats(int color, int length){
				return m_array[color][length-2];
			}
			
			public int[][] getArray(){
				return m_array;
			}
			
		}//class Stat
		
		/***************************************************/
		
	}//class History
	
	private class Sequence{
		private BoardCell[] m_members;
		private int m_length;
		private int m_color;
		private boolean m_open;
		
		public Sequence(int m_color, BoardCell first){
			this.m_color = m_color;
			m_length=1;
			m_open = false;
			m_members = new BoardCell[3];
			m_members[0] = first;
		}
		
		public BoardCell[] getPieces(){
			return m_members;
		}
		
		public void setOpen(){ m_open = true; }
		
		public boolean isOpen() { return m_open; }
		
		public int getLength(){ return m_length; }
		
		public boolean grow(BoardCell piece){ 
			m_members[m_length] = piece;
			m_length++;
			return (m_length==3);
		}
		
		public int getColor(){ return m_color; }
		
	}

	
}
