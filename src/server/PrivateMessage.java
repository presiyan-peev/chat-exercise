package server;

import java.util.Map;

import common.Plugin;

public class PrivateMessage implements Plugin {
	private Connection c=null;
	@Override
	
	public String customize(String text) {
		if (text.length()>5) {
			if(text.substring(0, 6).equals("tx/msg")){
				Connection receiverAddress = null;
				Map<String, Connection> newMap = server.Connection.getUsers();
				String[] splitText = text.split(" ", 3);
				synchronized (newMap){
					System.out.println("zazazaz"+splitText[1]+"zzz");
					if (newMap.containsKey(splitText[1])){
						System.out.println("zzzzzz"+splitText[1]);
						receiverAddress = newMap.get(splitText[1]);
						receiverAddress.send(splitText[2]);
					}
					else
						System.out.println(splitText[1]+" is an incorrect receiver");
				}				
	//			System.out.println(splitText[0]+"..."+splitText[1]+"xxx"+splitText[2]);
	//			c.handlePrivateMessage(splitText[1], splitText[2]);
	//			return text;
			}
		}
		return text;
	}

	@Override
	public String decrypt(String text) {
		// TODO Auto-generated method stub
		return text;
	}

}
