package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import client.Client;
import client.Gui;

import common.*;
/**
 * class for an individual connection to a client. allows to send messages to
 * this client and handles incoming messages.
 */
public class Connection extends Thread {
	protected Socket socket;
	protected ObjectInputStream inputStream;
	protected ObjectOutputStream outputStream;
	private static Server server;
	public static boolean isPassCorrect = false;
	static Map<String, Connection> users = new HashMap<String, Connection>();
	public static ArrayList<Plugin> pluginA;
	
	public Connection(Socket s, Server server, ArrayList<Plugin> pluginA) {
		this.socket = s;
		this.pluginA = pluginA;
		try {
			inputStream = new ObjectInputStream((s.getInputStream()));
			outputStream = new ObjectOutputStream((s.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.server = server;
	}

	/**
	 * waits for incoming messages from the socket
	 */
	public void run() {
		String clientName = socket.getInetAddress().toString();
		try {
			server.broadcast(clientName + " has joined.");
			Object msg = null;
			while ((msg = inputStream.readObject()) != null) {
				String msgString = ((TextMessage) msg).getContent();
				System.out.println("full msg "+msgString);
				if(msgString.substring(0,2).equals("ps")) {
					String text = ((TextMessage) msg).getContent().split(" ")[0];
					String uName = ((TextMessage) msg).getContent().split(" ")[1];
					System.out.println("BASS"+uName+"BASS");
					handlePassword(text);
					handleUsername(uName, clientName);
				}
				else {
					if (msgString.length()>5) {
						if(msgString.substring(0,6).equals("tx/msg")) {
							System.out.println("11"+msgString);
							for(Plugin plugin:this.pluginA) {
								if(plugin instanceof PrivateMessage){
									msgString = plugin.customize(msgString);
									System.out.println("    instanceof");
								}
								else 
									System.out.println("not-instanceof");
							}
							System.out.println("22"+msgString);
						}
						else 
							handleIncomingMessage(clientName, msg);
					}
					else {
						handleIncomingMessage(clientName, msg);
					}
				}
			}
		} catch (SocketException e) {
		} catch (EOFException e) {
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			server.removeConnection(this);
			server.broadcast(clientName + " has left.");
			try {
				socket.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * decides what to do with incoming messages
	 * 
	 * @param name
	 *            name of the client
	 * @param msg
	 *            received message
	 */
	private void handleIncomingMessage(String name, Object msg) {
		if (msg instanceof TextMessage){
			
//			Map<User, Connection> connections = getUsers();
//			connections.get(key)
			
			String text = ((TextMessage) msg).getContent();
			if (text.substring(0,2).equals("tx") || text.substring(0,2).equals("gk")) text=text.substring(2);
				if (Gui.rdbtnRot.isSelected()) server.broadcast(name + " - " + Encryption.rot13encoding(text));
				else if (Gui.rdbtnAlphaEnc.isSelected()) server.broadcast(name + " - " + Encryption.alphadecoding(text));
				else server.broadcast(name + " - " + text);
		}
	}
	
	void handlePrivateMessage(String username, String msg){
		Map<String, Connection> newMap = getUsers();
		Connection receiverAddress = null;
		if (newMap.containsKey(username)){
			System.out.println("PM: "+username);
			receiverAddress = newMap.get(username);
			receiverAddress.send(msg);
		}
		else
			System.out.println("Incorrect receiver");
	}
	
	void handleUsername(String name, String address){
		System.out.println("UNO"+name);
		if(!name.matches("^(?i)[a-z0-9_-]{1,15}$")) name = "invalideName";
		putUsers(name, this);
		System.out.println("UN"+name);
	}
	
	void handlePassword(String msg) {
//		String text = ((TextMessage) msg).getContent();
//		System.out.println("00000000000000"+text);
		if (msg.equals("ps1234")){
			send("$¹");					
		} else {
			send("¹$");
	//		System.out.println(isPassCorrect);
		}//System.out.println(isPassCorrect);
	}
	
	public static boolean isPassCorr(){
		return isPassCorrect;
	}
	/**
	 * sends a message to the client
	 * 
	 * @param line
	 *            text of the message
	 */
	public void send(String line) {
		if (Gui.rdbtnRot.isSelected()) send(new TextMessage(Encryption.rot13encoding(line)));
		else if (Gui.rdbtnAlphaEnc.isSelected()) send(new TextMessage(Encryption.alphaencoding(line)));
		else  send(new TextMessage(line));
	}

	public void send(TextMessage msg) {
		try {
			synchronized (outputStream) {
				outputStream.writeObject(msg);
			}
			outputStream.flush();
		} catch (IOException ex) {
		}
	}
	
	public static Map<String, Connection> getUsers() {
		return new HashMap<String, Connection>(users);
	}
	
	public void putUsers(String user, Connection connection) {
		Map<String, Connection> aMap = new HashMap<String, Connection>();
		aMap.put(user, connection);
		users = Collections.unmodifiableMap(aMap);
	}

}
