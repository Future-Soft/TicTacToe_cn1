package com.future.tictactoe;

import com.codename1.ui.*;

public class BoardCell {
	private Container container;
	private Label markLabel;
	private Player player;
	
	public BoardCell(Label label, Container container){
		this.setMarkLabel(label);
		this.container = container;
		//bgLabel.setIcon(Images.SQUARE);
	}

	public Label getMarkLabel() {
		return markLabel;
	}

	public void setMarkLabel(Label label) {
		this.markLabel = label;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
		if(player == null){
			markLabel.getStyle().setBgImage(null);
		}
		else{
			markLabel.getStyle().setBgImage(player.getImage());
		}
		//bgLabel.setIcon(Images.SQUARE);
		if(container!=null)
			container.setUIID("ContainerCell");
	}
	
	public void setWinningCell(){
		markLabel.getStyle().setBgImage(player.getWinningImage());
		//bgLabel.setIcon(Images.SQUARE_SELECTED2);
		if(container!=null)
			container.setUIID("ContainerCell2");
	}

}
