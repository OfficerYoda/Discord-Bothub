package de.officeryoda;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import de.officeryoda.Bot.Bot;
import de.officeryoda.Bot.BotBase;
import de.officeryoda.Bot.BotCreater;
import de.officeryoda.Bot.BotNetwork;
import de.officeryoda.GUI.BotListGui;
import de.officeryoda.Utils.BotSaves;

public class Main {

	private static final boolean developerMode = false;
	
	public static Main INSTANCE;
	
	private List<Bot> bots;
	private List<Bot> onlineBots;
	private BotCreater botCreater;
	private BotSaves botSaves;

	public static void main(String[] args) {
		try {
			new Main();
		} catch (LoginException e) {}
	}

	public Main() throws LoginException {
		//runs on shutdown
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				shutdown();
			}
		});	    

		INSTANCE = this;

		bots = new ArrayList<>();
		onlineBots = new ArrayList<>();

		botSaves = new BotSaves("C");
		
		botCreater = new BotCreater(this, botSaves);
		
		if(developerMode) {
			botCreater.loadDevBots();
			startAllBots();
		} else {
			if(botSaves.getYamlFile().getConfigurationSection("").getKeys(false).size() < 1)
				botCreater.createBaseBots();
			else
				botCreater.loadBotsFromSaves();
			
			new BotNetwork();
			
			BotListGui botListGui = new BotListGui(this);
			botListGui.open();
		}
	}

	private void shutdown() {
		stopAllBots();
		botCreater.saveAllBots();
		System.out.println("shoutdown");
	}

	public void startAllBots() {
		for(Bot bot : bots)
			bot.start();
	}

	public boolean startBot(BotBase botBase) {
		for(Bot bot : bots) {
			if(bot.getBotBase() == botBase) {
				bot.start();
				return true;
			}
		}

		return false;
	}

	public void stopAllBots() {
		for(Bot bot : bots)
			stopBot(bot);
	}

	public void stopBot(Bot bot) {
		bot.stop();
	}

	public boolean stopBot(BotBase botBase) {
		for(Bot bot : bots) {
			if(bot.getBotBase() == botBase) {
				stopBot(bot);
				return true;
			}
		}

		return false;
	}

	public List<Bot> getBots() {
		return bots;
	}

	public List<Bot> getOnlineBots() {
		return this.onlineBots;
	}

	public void addOnlineBot(Bot bot) {
		onlineBots.add(bot);
	}

	public void removeOnlineBot(Bot bot) {
		onlineBots.remove(bot);
	}

	public void addBot(Bot bot) {
		bots.add(bot);
	}

	public void removeBot(Bot bot) {
		bots.remove(bot);
	}

	public Bot getBotByTrueName(String botname) {
		for(Bot bot : bots)
			if(bot.getTrueName().toLowerCase().equals(botname.toLowerCase()))
				return bot;
		return null;
	}

	public Bot getBotByVisualName(String botname) {
		for(Bot bot : bots)
			if(bot.getVisualName().toLowerCase().equals(botname.toLowerCase()))
				return bot;
		return null;
	}
	public BotSaves getBotSaves() {
		return botSaves;
	}

	public int getFreeId() {
		int id = 0;
		while (!isFreeId(id))
			id++;
		return id;
	}

	public boolean isFreeId(int id) {
		for (Bot bot : bots)
			if(bot.getBotBase().getId() == id)
				return false;
		return true;
	}

	public static boolean isDeveloperMode() {
		return developerMode;
	}
}
