package com.future.tictactoe;


import com.codename1.components.*;
import com.codename1.ui.*;
import com.codename1.ui.events.*;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.*;
import com.codename1.ui.util.*;

import java.io.IOException;

public class TicTacToe {

	public static final String GAME_NAME = "Tic Tac Toe";
	public static final String COMPANY = "Future soft";
	public static final String VERSION = "1.3";
	
    private Form current, settings, splash;
    private UIBuilder builder;
    private Player[] playing;
    private GameController gameController;
    private Dialog helpDialog;
    private int level;

    public void init(Object context) {
        try{
            Resources theme = Resources.openLayered("/theme");
            int temp = Display.DENSITY_HD;
            UIManager.getInstance().setThemeProps(theme.getTheme(theme.getThemeResourceNames()[0]));
            Strings.initStrings();
            builder = new UIBuilder();
            UIBuilder.registerCustomComponent("Ads", Ads.class);
            current = (Form) builder.createContainer(theme, "mainForm");
            settings = (Form) builder.createContainer(theme, "settingsForm");
            splash = (Form) builder.createContainer(theme, "splashScreen");
            setHelpDialog((Dialog) builder.createContainer(theme, "help_dialog"));
            level = AIPlayer.NORMAL;
            Display.getInstance().lockOrientation(true);
            /*for(int i=1; i<=9; i++){
            	Container c = (Container) builder.findByName(i+"_cell_container", current);
            	c.setWidth(Display.getInstance().getDisplayWidth()/3);
            	c.setHeight(Display.getInstance().getDisplayHeight()/4);
            }*/
            initHelp();
            Images.initImages(theme, current);
            Ads ads = (Ads) builder.findByName("bottom_add", current);
            //ads.setAppID("FutureSoft_FutureSoft_tictactoe_Nokia");
            //ads.setAppID("FutureSoft_tictactoe_BB");
            ads.setAppID("FutureSoft_tictactoe_Android");
            //ads.setAppID("FutureSoft_tictactoe_iPhone");
            //ads.setAppID("FutureSoft_tictactoe_WP");
            //ads.setAppID("FutureSoft_tictactoe_WP");
            
            ads.setUpdateDuration(40);
            if (Display.getInstance().getDisplayHeight() <=400)
            {
            	//TextArea t = (TextArea) builder.findByName("header_text", current);
            	//t.getStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE));
            	ads.setPreferredH(53);
            }
            else
            	ads.setPreferredH(80);
            //ads.setPreferredH(1);
            Container c = (Container) builder.findByName("top_buttons_container", current);
            //Button b = (Button) builder.findByName("exit_button", current);
            //c.removeComponent(b);
            current.addCommandListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent evt) {
					Display.getInstance().exitApplication();
				}
			});
            current.setBackCommand(new Command(""));
            showSplash();
            gameController = new GameController(this, current, settings);
            playing = new Player[]{ new HumanPlayer(0), new AIPlayer(1, level, gameController)};
            //board.init(new Player[]{ new HumanPlayer(0, board), new AIPlayer(1, board)});
            gameController.init(playing);
       }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    private void initHelp() {
		Button close = (Button) builder.findByName("help_close", helpDialog);
		close.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent evt) {
				helpDialog.dispose();
			}
		});
		helpDialog.setBackCommand(new Command(""));
		TextArea text = (TextArea) builder.findByName("help_text", helpDialog);
		text.setText(Strings.helpText);
	}

	private void showSplash() {
    	Container bottomCont, centerCont, topCont;
		Label topLeft1, topRight1, bottomLeft1, bottomRight1;
		Label topLeft2, topRight2, bottomLeft2, bottomRight2;
		bottomCont = (Container) builder.findByName("splash_south_container", splash);
		centerCont = (Container) builder.findByName("splash_center_container", splash);
		topCont = (Container) builder.findByName("splash_north_container", splash);
		topLeft1 = (Label)builder.findByName("splash_top_left1", splash);
		topRight1 = (Label)builder.findByName("splash_top_right1", splash);
		bottomLeft1 = (Label)builder.findByName("splash_bottom_left1", splash);
		bottomRight1 = (Label)builder.findByName("splash_bottom_right1", splash);
		topLeft2 = (Label)builder.findByName("splash_top_left2", splash);
		topRight2 = (Label)builder.findByName("splash_top_right2", splash);
		bottomLeft2 = (Label)builder.findByName("splash_bottom_left2", splash);
		bottomRight2 = (Label)builder.findByName("splash_bottom_right2", splash);
		bottomCont.setHeight(Display.getInstance().getDisplayHeight()/3);
		topCont.setHeight(Display.getInstance().getDisplayHeight()/3);
		centerCont.setHeight(Display.getInstance().getDisplayHeight()/3);
		topLeft1.setIcon(Images.SQUARE);
		topRight1.setIcon(Images.SQUARE);
		bottomLeft1.setIcon(Images.SQUARE);
		bottomRight1.setIcon(Images.SQUARE);
		topLeft2.setIcon(Images.PLAYER_X);
		topRight2.setIcon(Images.PLAYER_O);		
		bottomLeft2.setIcon(Images.PLAYER_O);
		bottomRight2.setIcon(Images.PLAYER_X);
	}

	public void start() {
        if(current != null){
        	splash.show();
        	Display.getInstance().scheduleBackgroundTask(new Runnable() {
				
				public synchronized void run() {
					try {
						this.wait(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Display.getInstance().callSerially(new Runnable() {
						
						public void run() {
							current.show();
							
						}
					});
				}
			});
            //current.show();
            return;
        }
    }

    public void stop() {
        current = Display.getInstance().getCurrent();
    }
    
    public void destroy() {
    }

	public Player[] getPlaying() {
		return playing;
	}

	public void setPlaying(Player[] playing) {
		this.playing = playing;
	}
	
	public GameController getGameController(){
		return gameController;
	}
	
	public Form getForm(){
		return current;
	}

	public Dialog getHelpDialog() {
		return helpDialog;
	}

	public void setHelpDialog(Dialog helpDialog) {
		this.helpDialog = helpDialog;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
