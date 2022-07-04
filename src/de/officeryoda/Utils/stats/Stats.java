package de.officeryoda.Utils.stats;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import de.officeryoda.Utils.YamlUtil;

public class Stats implements YamlUtil {
	
	File file = null;
	YamlConfiguration configFile;
	
	final String botname;
	final String disc;
	
	
	public Stats(String botname, String disc) {
		this.configFile = new YamlConfiguration();
		this.disc = disc;
		this.botname = botname;
		
		File dir = new File(disc + ":/YodaCooperation/" + botname + "/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		file = new File(dir, "stats.yml");

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
	
	public void add(String path, int value) {
		configFile.set(path, (int) get(path, 0) + value);
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