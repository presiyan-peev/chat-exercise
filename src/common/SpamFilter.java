package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import server.Connection;

import client.Client;

public class SpamFilter implements Plugin {
	public static String checkSpam(String text)
	{
	    String temp;
	    // you have to create this file in order to use this feature
	    File file = new File("C:\\Users\\Public\\Documents\\spam.txt");

	    if(file.exists())
	    {
	        try
	        {
	            BufferedReader in = new BufferedReader(new FileReader(file));
	            temp = in.readLine();
	            while (temp != null)
	            {
	            	text = text.trim();
	                if(text.equals(temp))
	                {
	                    // Match was found
	                    // Clean up and return true
	              
	                    text = "SPAM";
	                }

	                temp = in.readLine();
	            }
	            in.close();
	        }
	        catch (FileNotFoundException e)
	        {

	        }
	        catch (IOException e)
	        {

	        }

	    }
		return text;
	}

	@Override
	public String customize(String input) {
		// TODO Auto-generated method stub
		return checkSpam(input);
	}

	@Override
	public String decrypt(String text) {
		// TODO Auto-generated method stub
		return text;
	}

}
