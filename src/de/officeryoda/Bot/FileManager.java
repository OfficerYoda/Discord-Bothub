package de.officeryoda.Bot;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import de.officeryoda.Main;
import de.officeryoda.Utils.BotSaves;
import de.officeryoda.Utils.Config;
import de.officeryoda.Utils.stats.Stats;

public class FileManager {
	
	private BotSaves botSaves;
	
	private final Bot bot;
	
	private Config config;
	private Stats stats;
	
	public FileManager(Bot bot, String disc) {
		this.botSaves = Main.INSTANCE.getBotSaves();
		
		this.bot = bot;
		
		this.config = new Config(bot.getTrueName(), disc);
		this.stats = new Stats(bot.getTrueName(), disc);
	}
	
	public void changeVisualName(String newName) {
		botSaves.put(bot.getTrueName() + ".botname", newName);
	}
	
	public void deleteDirectory() {
		try {
			FileUtils.deleteDirectory(new File("C:\\YodaCooperation\\" + bot.getTrueName()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Bot getBot() {
		return bot;
	}
	
	public Config getConfig() {
		return config;
	}

	public Stats getStats() {
		return stats;
	}
}
