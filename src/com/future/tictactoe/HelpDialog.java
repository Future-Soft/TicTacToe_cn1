package com.future.tictactoe;

import com.codename1.ui.*;
import com.codename1.ui.layouts.*;

public class HelpDialog extends Dialog {
	private TextArea text;
	
	public HelpDialog()
	{
		super("Help");
		text = new TextArea();
		text.setEditable(false);
		text.setUIID("DisplayTextArea");
		text.setText(Strings.helpText);
		init();
	}
	
	private void init() {
		setLayout(new FlowLayout());
//		getSelectedStyle().setBgImage(Images.DIALOG_BG);
//		getUnselectedStyle().setBgImage(Images.DIALOG_BG);
		getSelectedStyle().setBgTransparency(255);
		getUnselectedStyle().setBgTransparency(255);
		addComponent(text);
	}

	public void show(){
		showPacked(BorderLayout.CENTER, true);
	}
	
	@Override
	public void pointerReleased(int x, int y){
		dispose();
	}
	
	@Override
	public void pointerReleased(int[] x, int[] y){
		dispose();
	}
}
