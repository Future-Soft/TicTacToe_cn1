package com.future.tictactoe;

import com.codename1.ui.*;
import com.codename1.ui.util.*;

public class Images {

	public static Image PLAYER_O;
	public static Image PLAYER_O_SCALED;
	public static Image PLAYER_O_WIN;
	public static Image PLAYER_X;
	public static Image PLAYER_X_SCALED;
	public static Image PLAYER_X_WIN;
	public static Image SQUARE;
	public static Image SQUARE_SELECTED;
	public static Image SQUARE_SELECTED2;
	public static Image DIALOG_BG;
	
	public static void initImages(Resources res, Form mainForm){
		UIBuilder builder = new UIBuilder();
		Container c = (Container) builder.findByName("main_center_container", mainForm);
		Button startBtn = (Button) builder.findByName("start_button", mainForm);
		int scaledSize = Display.getInstance().getDisplayWidth()/3 - 5;
		PLAYER_O = res.getImage("oplayer.png");
		/*if(PLAYER_O.getWidth()>Display.getInstance().getDisplayWidth() || PLAYER_O.getHeight()>scaledSize){
			PLAYER_O = PLAYER_O.scaled(scaledSize, scaledSize);
		}*/
		PLAYER_O_SCALED = PLAYER_O.scaled(scaledSize, scaledSize);
		PLAYER_X = res.getImage("xplayer.png");
		/*if(PLAYER_X.getWidth()>Display.getInstance().getDisplayWidth() || PLAYER_X.getHeight()>scaledSize){
			PLAYER_X = PLAYER_X.scaled(scaledSize, scaledSize);
		}*/
		PLAYER_X_SCALED = PLAYER_X.scaled(scaledSize, scaledSize);
		PLAYER_O_WIN = res.getImage("oplayer_win.png");
		PLAYER_O_WIN = PLAYER_O_WIN.scaled((int)(PLAYER_O_WIN.getWidth()*0.9), (int)(PLAYER_O_WIN.getHeight()*0.9));
		PLAYER_X_WIN = res.getImage("xplayer_win.png");
		PLAYER_X_WIN = PLAYER_X_WIN.scaled((int)(PLAYER_X_WIN.getWidth()*0.9), (int)(PLAYER_X_WIN.getHeight()*0.9));
		SQUARE = res.getImage("square.png");
		SQUARE_SELECTED = res.getImage("square_selected.png");		
		DIALOG_BG = res.getImage("dialog_bg.png");
	}
	
}
