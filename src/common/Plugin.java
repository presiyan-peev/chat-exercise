package common;

import server.Connection;

public interface Plugin {
	public String customize(String text);
	public String decrypt(String text);
}
