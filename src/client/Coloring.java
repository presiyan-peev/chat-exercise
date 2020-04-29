package client;

import server.Connection;
import common.Plugin;
import client.Gui;

public class Coloring implements Plugin {

	@Override
	public String customize(String input) {
		String text = new String();
		if (Gui.colorBlack.isSelected()) text = input+"</black>";
		else if (Gui.colorRed.isSelected()) text = input+"</red>";
		else if (Gui.colorYellow.isSelected()) text = input+"</yellow>";
		else if (Gui.colorGreen.isSelected()) text = input+"</Green>";
		else if (Gui.colorBlue.isSelected()) text = input+"</Blue>";
		else text = input;
		return text;
	}

	@Override
	public String decrypt(String text) {
		// TODO Auto-generated method stub
		return text;
	}
}
