package client;

import java.util.ArrayList;

import server.History;
import server.PrivateMessage;

import common.Encryption;
import common.Plugin;
import common.SpamFilter;

public class ClientStarter {
	public static void main(String args[]) {
		ArrayList <Plugin> pluginA = new ArrayList();
		Coloring col = new Coloring();
		History log = new History();
		Encryption enc = new Encryption();
		SpamFilter sf = new SpamFilter();
		PrivateMessage pm = new PrivateMessage();
		pluginA.add(log);
		pluginA.add(sf);
		pluginA.add(col);
		pluginA.add(enc);
		pluginA.add(pm);
		
		
		Client client = new Client("localhost", 8081, pluginA);
		
		Client.getPass(client);
		new Gui("Chat ", client);

	}
}
