package de.officeryoda.Utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class BotSaves implements YamlUtil {
	
	File file = null;
	YamlConfiguration configFile;
	
	final String disc;

	public BotSaves(String disc) {
		this.configFile = new YamlConfiguration();
		this.disc = disc;
		
		File dir = new File(disc + ":/YodaCooperation/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		file = new File(dir, "savedBots.yml");
		
		//deleting old botSaves.yml file
		File oldBotSave = new File(dir, "botSaves.yml");
		if(oldBotSave.exists())
			if (oldBotSave.delete())

		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		configFile = YamlConfiguration.loadConfiguration(file);
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	public YamlConfiguration getYamlFile() {
		return configFile;
	}	

	@Override
	public boolean contains(String path) {
		return configFile.contains(path);
	}

	@Override
	public void save() {
		try {
			configFile.save(file);
		} catch (IOException e) {}
	}

	@Override
	public void put(String path, Object value) {
		configFile.set(path, value);
		save();
	}

	@Override
	public Object get(String path, Object errorValue) {
		if(contains(path)) 
			return configFile.get(path);
		else
			return errorValue;
	}
}