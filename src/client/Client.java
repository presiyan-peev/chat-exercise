package client;

import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import server.*;

import common.*;

/**
 * simple chat client
 */
public class Client implements Runnable {
	public static boolean history = true;
	public static boolean encryption = true;
	public static boolean coloring = true;
	public static boolean pass = true;
	public Plugin plugin;
	protected ObjectInputStream inputStream;
	public static ArrayList<Plugin> pluginA;
	protected ObjectOutputStream outputStream;

	protected Thread thread;

	public Client(String host, int port, ArrayList<Plugin> pluginA) {
		try {
			System.out.println("Connecting to " + host + " (port " + port
					+ ")...");
			Socket s = new Socket(host, port);
			this.outputStream = new ObjectOutputStream((s.getOutputStream()));
			this.inputStream = new ObjectInputStream((s.getInputStream()));
			this.pluginA = pluginA;
			thread = new Thread(this);
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * main method. waits for incoming messages.
	 */
	
	public void run() {
		try {
			Thread thisthread = Thread.currentThread();
			while (thread == thisthread) {
				try {
					Object msg = inputStream.readObject();
					handleIncomingMessage(msg);
				} catch (EOFException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			thread = null;
			try {
				outputStream.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * decides what to do with incoming messages
	 * 
	 * @param msg
	 *            the message (Object) received from the sockets
	 */
	protected void handleIncomingMessage(Object msg) {
		if (msg instanceof TextMessage) {
			String text = ((TextMessage) msg).getContent();

			if(text.equals("$¹")) System.out.println("Correct Password");
			else if(text.equals("¹$")) System.exit(0);
			else fireAddLine(text + "\n");
		}
	}

	public void send(String line) {
		System.out.println("1"+line);
	//	if (history) Server.log(line);
		for(Plugin plugin:this.pluginA) {
			if (plugin instanceof PrivateMessage) continue;
			line = plugin.customize(line);
		}
	/*	if (Gui.rdbtnRot.isSelected()) send(new TextMessage(Encryption.rot13encoding(line)));
		else if (Gui.rdbtnAlphaEnc.isSelected()) send(new TextMessage(Encryption.alphaencoding(line)));
		else */ 
		System.out.println("2"+line);
		send(new TextMessage(line));
	}

	public void send(TextMessage msg) {
		try {
			System.out.println("3"+msg.toString());
			outputStream.writeObject(msg);
			outputStream.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
			this.stop();
		}
	}

	/**
	 * listener-list for the observer pattern
	 */
	private ArrayList listeners = new ArrayList();

	/**
	 * addListner method for the observer pattern
	 */
	public void addLineListener(ChatLineListener listener) {
		listeners.add(listener);
	}

	/**
	 * removeListner method for the observer pattern
	 */
	public void removeLineListener(ChatLineListener listner) {
		listeners.remove(listner);
	}

	/**
	 * fire Listner method for the observer pattern
	 */
	public void fireAddLine(String line) {
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
			ChatLineListener listener = (ChatLineListener) iterator.next();
			listener.newChatLine(line);
		}
	}
	
	public static void getPass(Client cl){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,2));
		JLabel usernameLbl = new JLabel("Your Username:");
		JLabel passwordLbl = new JLabel("Server Password:");
		JTextField username = new JTextField();
		JTextField passwordFld = new JTextField();
		panel.add(usernameLbl);
		panel.add(username);
		panel.add(passwordLbl);
		panel.add(passwordFld);
		int input = JOptionPane.showConfirmDialog(null, panel, "Enter your password:"
                ,JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		String enteredUsername = username.getText();
		String enteredPassword = passwordFld.getText();
		String passString = "ps"+enteredPassword+" "+enteredUsername;
		cl.send(new TextMessage(passString));
//		return Connection.isPassCorrect;
	}
	
	public void stop() {
		thread = null;
	}
	
/*	static boolean authentifizierung(){
		if (pass) { 
			if(Connection.check_Pass(getPass())) return true;
			else return false;
		} else return true;

	}*/
}
