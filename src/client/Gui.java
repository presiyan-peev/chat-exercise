package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment.*;

import server.History;
import server.PrivateMessage;
import server.Server;

import common.*;
/**
 * simple swing gui for the chat client
 */
public class Gui extends JFrame implements ChatLineListener {

	private static final long serialVersionUID = 1L;

	protected JTextArea outputTextbox;
	protected JTextField inputField;
	public static JRadioButton rdbtnRot = new JRadioButton("Rot 13");
	public static JRadioButton rdbtnAlphaEnc = new JRadioButton("Alpha enc");
	public static JRadioButton rdbtnOff = new JRadioButton("Off");
	
	static JRadioButton colorBlack = new JRadioButton("Black");   
    static JRadioButton colorRed = new JRadioButton("Red");   
    static JRadioButton colorYellow = new JRadioButton("Yellow");
    static JRadioButton colorBlue = new JRadioButton("Blue");
    static JRadioButton colorGreen = new JRadioButton("Yellow");
	
	private static int rowstextarea = 20;
	private static int colstextarea = 60;
	
	private String farbeAnf="";
	private String farbeEnd="";
	private Client chatClient;
	
	/**
	 * creates layout
	 * 
	 * @param title
	 *            title of the window
	 * @param chatClient
	 *            chatClient that is used for sending and receiving messages
	 */
	public Gui(String title, Client chatClient) {
		System.out.println("starting gui...");

		outputTextbox = new JTextArea(Gui.rowstextarea, Gui.colstextarea);
		outputTextbox.setEditable(false);
		inputField = new JTextField();
		inputField.addActionListener(getInput());
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(outputTextbox)
                    .addComponent(inputField))
        );

        // layout.linkSize(SwingConstants.HORIZONTAL, findButton, cancelButton);

        layout.setVerticalGroup(layout.createSequentialGroup()
                    .addComponent(outputTextbox)
                    .addComponent(inputField));
		
    	// register listener so that we are informed whenever a new chat message
		// is received (observer pattern)
		chatClient.addLineListener(this);

		setTitle(title);
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.chatClient = chatClient;
	
		
	//menu
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenu mnColoring = new JMenu("Coloring");
		mnEdit.add(mnColoring);
		
        mnColoring.add(colorBlack);
        mnColoring.add(colorRed);
        mnColoring.add(colorYellow);
        mnColoring.add(colorBlue);
        mnColoring.add(colorGreen);
        
        ButtonGroup groupCol = new ButtonGroup();
        groupCol.add(colorBlue);
        groupCol.add(colorGreen);
        groupCol.add(colorRed);
        groupCol.add(colorBlack);
        groupCol.add(colorYellow);
		
		JMenu mnEncryption = new JMenu("Encryption");
		mnEdit.add(mnEncryption);

		mnEncryption.add(rdbtnAlphaEnc);
		mnEncryption.add(rdbtnRot);
		mnEncryption.add(rdbtnOff);
		
		ButtonGroup groupEnc = new ButtonGroup();
		groupEnc.add(rdbtnAlphaEnc);
		groupEnc.add(rdbtnRot);
		groupEnc.add(rdbtnOff);
		
	/*	colorRed.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	farbeAnf = "<red>";
                farbeEnd = "</red>";
            }
        });
		
		colorYellow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                farbeAnf = "<yellow>";
                farbeEnd = "</yellow>";
            }
        });
		
		colorBlack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	farbeAnf = "<black>";
                farbeEnd = "</black>";
            }
        });
		
		colorBlue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                farbeAnf = "<blau>";
                farbeEnd = "</blau>";
            }
        });
		
		colorGreen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	farbeAnf = "<blau>";
                farbeEnd = "</blau>";
            }
        });*/
		
	}
	
	private ActionListener getInput() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = inputField.getText();
				chatClient.send("tx"+input);
				inputField.setText("");
			}
		};
	}

	/**
	 * this method gets called every time a new message is received (observer
	 * pattern)
	 */
	public void newChatLine(String line) {
		String lined = new String();
		for(Plugin plugin:chatClient.pluginA) {
			if (plugin instanceof PrivateMessage) continue;
			lined = plugin.customize(line);
		}
		/*for(Plugin plugin:chatClient.pluginA) {
			lined = plugin.decrypt(line);
		}*/
		outputTextbox.append(lined);
	}
}
