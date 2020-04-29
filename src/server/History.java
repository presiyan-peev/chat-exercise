package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import common.Plugin;

public class History implements Plugin{
	public String customize(String text) {
		try {
			File logFile = new File("C:\\Users\\Public\\Documents\\log.txt");
			if(!logFile.exists()) {
			    logFile.createNewFile();
			} 
			String gotContent = new String(text);
			BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\Public\\Documents\\log.txt", true));
			bw.write(gotContent);
			bw.newLine();
			bw.close();
		}
		catch (IOException ioe)
		{
		 ioe.printStackTrace();
		}
		return text;
	}

	@Override
	public String decrypt(String text) {
		// TODO Auto-generated method stub
		return text;
	}
}
