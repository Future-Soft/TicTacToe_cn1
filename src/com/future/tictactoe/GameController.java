package com.future.tictactoe;

import com.codename1.ui.*;
import com.codename1.ui.events.*;
import com.codename1.ui.util.*;

public class GameController implements ActionListener {
	private Player[] players;
	private Form form;
	private Board board;
	private UIBuilder builder;
	private Button startBtn, helpBtn, settingsBtn, closeBtn;
	private Container centerContainer, topContainer;
	private TicTacToe midlet;
	/*private Label headerLabel, helpLabel, settingsLabel, closeLabel;*/
	private TextArea header; 
	private boolean computerPlays;
	private int turn;
	private boolean draggedPointer;
	private boolean gameOver;
	private ActionListener currentListener;
	private SettingsMenu settings;
	
	public GameController(TicTacToe _midlet, final Form form, Form settings){
		this.setForm(form);
		this.midlet = _midlet;
		draggedPointer = false;
		builder = new UIBuilder();
		this.settings = new SettingsMenu(midlet, settings);
		startBtn = (Button) builder.findByName("start_button", form);
		header = (TextArea) builder.findByName("header_text", form);
		helpBtn = (Button) builder.findByName("help_button", form);
		settingsBtn = (Button) builder.findByName("settings_button", form);
		closeBtn = (Button) builder.findByName("exit_button", form);
		/*helpLabel = (Label) builder.findByName("help_img_label", form);
		settingsLabel = (Label) builder.findByName("settings_img_label", form);
		closeLabel = (Label) builder.findByName("close_img_label", form);*/
		centerContainer = (Container) builder.findByName("main_center_container", form);
		topContainer = (Container) builder.findByName("top_buttons_container", form);
		startBtn.addActionListener(this);
		helpBtn.addActionListener(this);
		settingsBtn.addActionListener(this);
		if (closeBtn != null)
		{
			topContainer.removeComponent(closeBtn);
			closeBtn.addActionListener(this);
		}
		startBtn.setPreferredW(Display.getInstance().getDisplayWidth()/3);
		
		form.addPointerDraggedListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent evt) {
				draggedPointer = true;
			}
		});
		form.addPointerReleasedListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent evt) {
				if(draggedPointer){
					draggedPointer = false;
					return;
				}
				int x = evt.getX(), y = evt.getY();
				Label l;
				Component c = form.getComponentAt(x,  y);
				if(c == null)
					return;
				/*if(c==topContainer || topContainer.contains(c)){
					evt.consume();
					if(c == topContainer)
						c = topContainer.getComponentAt(x, y);
					l = (Label)c;
					if(l == helpLabel){
						midlet.getHelpDialog().show();
					} else if (l == settingsLabel){
						GameController.this.settings.show();
					} else if(l == closeLabel){
						Display.getInstance().exitApplication();
					}
					
				}*/
			}
		});
		
		
	}
	
	public synchronized void init(Player[] players){
		this.setPlayers(new Player[]{players[0], players[1]});
		board = new Board(this, form);
		players[0].setBoard(board);
		players[1].setBoard(board);
		final Player[] p = players;
		if(currentListener!=null){
			form.removePointerReleasedListener(currentListener);
		}
		currentListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent evt) {
				if(draggedPointer){
					draggedPointer = false;
					return;
				}
				int x = evt.getX(), y = evt.getY();
				Label l;
				Component c = form.getComponentAt(x,  y);
				if(c == null)
					return;
				if(c==centerContainer || centerContainer.contains(c)){
					evt.consume();
					if(c == centerContainer)
						c = centerContainer.getComponentAt(x, y);
					if(computerPlays || gameOver)
						return;
					while(c instanceof Container)
						c = ((Container) c).getComponentAt(0);
					l = (Label) c;
					int i = Integer.parseInt(l.getName().substring(0, 1));
					int j = Integer.parseInt(l.getName().substring(1, 2));
					makeMove(new Move(i, j, p[turn]));
					
				}
			}
		};
		form.addPointerReleasedListener(currentListener);
		computerPlays = false;
		gameOver = false;
		turn = 0;
		header.setText("Player "+(turn+1)+"'s turn");
		
		centerContainer.repaint();
		if(players[turn] instanceof AIPlayer){
			computerPlays = true;
		}
		players[turn].play();
	}

	public synchronized void makeMove(Move m) {
		if(!board.isValidMove(m)){
//			Dialog.show("Invalid move", "This spot is already taken", "ok", null);
			return;
		}
		board.updateMove(m);
		
		BoardCell[] sequence;
		if((sequence = board.checkForWin())!=null){
			finishGame(sequence);
			return;
		}
		if(board.isFull()){
			header.setText("Its a tie!");
			form.repaint();
			return;
		}
		turn = 1 - turn;
		header.setText("Player "+(turn+1)+"'s turn");
		centerContainer.repaint();
		computerPlays = false;
		if(players[turn] instanceof AIPlayer){
			computerPlays = true;
		}
		players[turn].play();
	}

	protected void finishGame(BoardCell[] sequence) {
		gameOver = true;
		for(int i=0; i<3; i++){
			sequence[i].setWinningCell();
		}
		header.setText("Player "+(sequence[0].getPlayer().getNum()+1)+" has won!");
		centerContainer.repaint();
	}

	

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == startBtn){
			init(midlet.getPlaying());
			return;
		}
		if(evt.getSource() == helpBtn){
			midlet.getHelpDialog().show();
		} else if (evt.getSource() == settingsBtn){
			GameController.this.settings.show();
		} else if(evt.getSource() == closeBtn){
			Display.getInstance().exitApplication();
		}
	}
	
	public Board getBoard(){
		return board;
	}
	
	public void setBoard(Board board){
		this.board = board;
	}
	
	
}
