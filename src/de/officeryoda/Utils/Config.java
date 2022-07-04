package de.officeryoda.Utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config implements YamlUtil {

	File file;
	YamlConfiguration configFile;

	final String botname;
	final String disc;


	public Config(String botname, String disc) {
		this.configFile = new YamlConfiguration();
		this.disc = disc;
		this.botname = botname.substring(0, 1).toUpperCase() + botname.substring(1).toLowerCase();

		File dir = new File(disc + ":/YodaCooperation/" + botname + "/");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		file = new File(dir, "general.yml");

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
