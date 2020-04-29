package common;

import client.Gui;

public class Encryption implements Plugin{
	public static String rot13encoding(String input) {
		if (client.Client.encryption){
		   StringBuilder sb = new StringBuilder();
		   for (int i = 0; i < input.length(); i++) {
		       char c = input.charAt(i);
		       if       (c >= 'a' && c <= 'm') c += 13;
		       else if  (c >= 'A' && c <= 'M') c += 13;
		       else if  (c >= 'n' && c <= 'z') c -= 13;
		       else if  (c >= 'N' && c <= 'Z') c -= 13;
		       sb.append(c);
		   }
		   return sb.toString();
		} else return input;
	}
	public static String alphaencoding(String input) {
		if (client.Client.encryption){
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < input.length(); i++) {
				char c = input.charAt(i);
				if (c>='A' && c<='z')
					c = (char) (c + 100);
					sb.append(c);
			}
			return sb.toString();
		} else return input;
	}
	public static String alphadecoding(String input) {
		if (client.Client.encryption){
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c>='A' && c<='z')
			c = (char) (c - 100);
			sb.append(c);
		}
			return sb.toString();
		} else return input;
	}
	@Override
	public String customize(String text) {
		if (client.Client.encryption){
			if (Gui.rdbtnRot.isSelected()) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < text.length(); i++) {
					char c = text.charAt(i);
					if       (c >= 'a' && c <= 'm') c += 13;
					else if  (c >= 'A' && c <= 'M') c += 13;
					else if  (c >= 'n' && c <= 'z') c -= 13;
					else if  (c >= 'N' && c <= 'Z') c -= 13;
					sb.append(c);
				}
				text = sb.toString();
		}
			else if (Gui.rdbtnAlphaEnc.isSelected()) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < text.length(); i++) {
					char c = text.charAt(i);
					if (c>='A' && c<='z')
						c = (char) (c + 100);
					sb.append(c);
				}
				text = sb.toString();
			}
		}
		return text;
	}
	@Override
	public String decrypt(String text) {
		if (client.Client.encryption){
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c>='A' && c<='z')
			c = (char) (c - 100);
			sb.append(c);
		}
			return sb.toString();
		} else return text;
	}
}
