package com.future.tictactoe;

public class Strings {
	public static String helpText;
	
	public static void initStrings(){
		helpText = "Game name: " + TicTacToe.GAME_NAME + "\n";
		helpText += "Company: " + TicTacToe.COMPANY + "\n";
		helpText += "Version: " + TicTacToe.VERSION;
		helpText += "\n\n";
		helpText += "Rules:\n";
		helpText += "Tic Tac Toe is a classic game. The goal of the game is to form a straight row"+
				"/column/diagonal of the same shape.\nEach player in turn puts its shape on an available spot"+
				" on the board, and the winner is the first one that accomplishes the goal."+
				"\n\n"+
				"Tip: you can change the computer's skills in the settings menu";
	}
}
