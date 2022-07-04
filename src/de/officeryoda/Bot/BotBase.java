package de.officeryoda.Bot;

import de.officeryoda.Main;

public class BotBase {
	
	private String trueName;
	private String visualName;
	private String token;
	private String prefix;
	private int id;
	
	public BotBase(String trueName, String visualName, String TOKEN, String prefix) {
		this.trueName = trueName;
		this.visualName = visualName;
		this.token = TOKEN;
		this.prefix = prefix;
		this.id = Main.INSTANCE.getFreeId();
	}

	public String getTrueName() {
		return trueName;
	}

	public String getVisualName() {
		return visualName;
	}

	public void setVisualName(String visualName) {
		this.visualName = visualName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
