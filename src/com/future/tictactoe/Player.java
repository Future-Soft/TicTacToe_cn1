package com.future.tictactoe;

import com.codename1.ui.*;

public abstract class Player {
	public static final int PLAYER_X = 0;
	public static final int PLAYER_O = 1;
	
	protected int num;
	protected Board board;
	
	public Player(int num, Board board){
		this.setNum(num);
		this.setBoard(board);
	}
	
	public Player(int num){
		this.num = num;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}
	
	public abstract void play();

	public Image getImage() {
		try {
			if(num == PLAYER_O){
				return Images.PLAYER_O;
			}
			return Images.PLAYER_X;
		}
		catch(Exception e){
			return null;
		}
	}

	public Image getWinningImage() {
		try {
			if(num == PLAYER_O){
				return Images.PLAYER_O_WIN;
			}
			return Images.PLAYER_X_WIN;
		}
		catch(Exception e){
			return null;
		}
	}
}
