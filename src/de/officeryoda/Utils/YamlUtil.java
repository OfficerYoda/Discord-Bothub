package de.officeryoda.Utils;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public interface YamlUtil {
	
	public File getFile();
	
	public YamlConfiguration getYamlFile();
	
	public boolean contains(String path);
	
	public void save();
	
	public void put(String path, Object value);
	
	public Object get(String path, Object errorValue);
	
}
