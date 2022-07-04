package de.officeryoda.Bot;

import de.officeryoda.Main;
import de.officeryoda.Tokens;
import de.officeryoda.Utils.BotSaves;

public class BotCreater {

	public static BotCreater INSTANCE;

	private Main main;
	private BotSaves botSaves;
	
	public BotCreater(Main main, BotSaves botSaves) {
		INSTANCE = this;

		this.main = main;
		this.botSaves = botSaves;
		
	}

	public void createBaseBots() {
		createBot("Bane", "Bane", Tokens.BANE, (String) botSaves.get("BANE.prefix", "."));
//		createBot("Boba", "Boba", Tokens.BOBA, (String) botSaves.get("BOBA.prefix", ","));
	}

	public void createBot(String trueName, String visualName, String token, String prefix) {
		Bot bot = new Bot(new BotBase(trueName, visualName, token, prefix));
		main.addBot(bot);
		saveBot(bot);
	}

	public void createBot(Bot bot) {
		main.addBot(bot);
		saveBot(bot);
	}

	public void loadBotsFromSaves() {
		for (String botName : botSaves.getYamlFile().getConfigurationSection("").getKeys(false))
			createBot(loadBot(botName));
	}
	
	public void loadDevBots() {
		createBot("Boba", "Boba", Tokens.BOBA, ".");
	}
	
	public void saveAllBots() {
		for (Bot bot : main.getBots()) {
			saveBot(bot);
		}
	}

	public Bot loadBot(String trueName) {
		String visualName = (String) botSaves.get(trueName + ".botname", "BotNameError");
		
		String token = (String) botSaves.get(trueName + ".token", "");

		String prefix = (String) botSaves.get(trueName + ".prefix", ".");

		return new Bot(new BotBase(trueName, visualName, token, prefix));
	}

	public void saveBot(Bot bot) {
		String trueName = bot.getTrueName();
		botSaves.put(trueName + ".botname", trueName);
		
		botSaves.put(trueName + ".token", bot.getToken());

		botSaves.put(trueName + ".prefix", bot.getPrefix());
	}
}
