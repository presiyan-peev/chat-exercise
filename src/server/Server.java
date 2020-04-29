package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import client.Coloring;

import common.*;
/**
 * server's main class. accepts incoming connections and allows broadcasting
 */
public class Server {
	ArrayList<Plugin> pluginA = new ArrayList();
	Coloring col = new Coloring();
	History log = new History();
	Encryption enc = new Encryption();
	SpamFilter sf = new SpamFilter();
	PrivateMessage pm = new PrivateMessage();
	
	public static void main(String args[]) throws IOException {
		if (args.length != 1)
			throw new RuntimeException("Syntax: ChatServer <port>");
		new Server(Integer.parseInt(args[0]));
	}

	/**
	 * awaits incoming connections and creates Connection objects accordingly.
	 * 
	 * @param port
	 *            port to listen on
	 */
	public Server(int port) throws IOException {
		ServerSocket server = new ServerSocket(port);
		while (true) {
			System.out.println("Waiting for Connections...");
			Socket client = server.accept();
			System.out.println("Accepted from " + client.getInetAddress());
			Connection c = connectTo(client);
			c.start();
		}
	}

	/**
	 * creates a new connection for a socket
	 * 
	 * @param socket
	 *            socket
	 * @return the Connection object that handles all further communication with
	 *         this socket
	 */
	public Connection connectTo(Socket socket) {
		pluginA.add(log);
		pluginA.add(sf);
		pluginA.add(col);
		pluginA.add(enc);
		pluginA.add(pm);
		Connection conn = new Connection(socket, this, pluginA);
		connections.add(conn);
		return conn;
	}

	/**
	 * list of all known connections
	 */
	protected HashSet connections = new HashSet();

	/**
	 * send a message to all known connections
	 * 
	 * @param text
	 *            content of the message
	 */
	public void broadcast(String text) {
		synchronized (connections) {
			for (Iterator iterator = connections.iterator(); iterator.hasNext();) {
				Connection connection = (Connection) iterator.next();
				connection.send(text);
			}
		}
	}

	/**
	 * remove a connection so that broadcasts are no longer sent there.
	 * 
	 * @param connection
	 *            connection to remove
	 */
	public void removeConnection(Connection connection) {
		connections.remove(connection);
	}
	

	public static void log(String content){
		try {
			File logFile = new File("C:\\Users\\Public\\Documents\\log.txt");
			if(!logFile.exists()) {
			    logFile.createNewFile();
			} 
			String gotContent = new String(content);
			BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\Public\\Documents\\log.txt", true));
			bw.write(gotContent);
			bw.newLine();
			bw.close();
		}
		catch (IOException ioe)
		{
		 ioe.printStackTrace();
		}
	}
}
