package com.future.tictactoe;

import com.codename1.ui.*;
import com.codename1.ui.events.*;
import com.codename1.ui.layouts.*;
import com.codename1.ui.util.*;

public class SettingsMenu extends Dialog{
	private CheckBox player1Checkbox, player2Checkbox;
	private ComboBox levelCombo;
	private Button submitBtn;
	private UIBuilder builder;
	private TicTacToe game;
	
	public SettingsMenu(TicTacToe game, Form form){
		super("Settings");
		this.game = game;
		builder = new UIBuilder();
		player1Checkbox = (CheckBox) builder.findByName("settings_player1_comp", form);
		player2Checkbox = (CheckBox) builder.findByName("settings_player2_comp", form);
		levelCombo = (ComboBox) builder.findByName("ai_level_combo", form);
		levelCombo.getUnselectedStyle().setBgTransparency(255);
		levelCombo.getSelectedStyle().setBgTransparency(255);
		levelCombo.getPressedStyle().setBgTransparency(255);
		player2Checkbox.setSelected(true);
		levelCombo.setSelectedIndex(2);
		submitBtn = (Button) builder.findByName("settings_submit", form);
		
		submitBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent evt) {
				Player[] players = new Player[2];
				int level = levelCombo.getSelectedIndex();
				if(!player1Checkbox.isSelected()){
					players[0] = new HumanPlayer(0);
				}
				else
					players[0] = new AIPlayer(0, level, SettingsMenu.this.game.getGameController());
				if(!player2Checkbox.isSelected())
					players[1] = new HumanPlayer(1);
				else
					players[1] = new AIPlayer(1, level, SettingsMenu.this.game.getGameController());
				SettingsMenu.this.game.setPlaying(players);
				SettingsMenu.this.dispose();
			}
		});
		//((Dialog)form).setBackCommand(new Command(""));
		setBackCommand(new Command(""));
		this.addCommandListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent evt) {
				dispose();
			}
		});
		setLayout(new BorderLayout());
//		getSelectedStyle().setBgImage(Images.DIALOG_BG);
//		getUnselectedStyle().setBgImage(Images.DIALOG_BG);
		getSelectedStyle().setBgTransparency(255);
		getUnselectedStyle().setBgTransparency(255);
		addComponent(BorderLayout.CENTER, form);
	}
	/*
	public void show(){
		showPacked(BorderLayout.CENTER, true);
	}*/
}
