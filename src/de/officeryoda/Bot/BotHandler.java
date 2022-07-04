package de.officeryoda.Bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.Presence;

public class BotHandler {
	
	Bot bot;
	
	ErrorHandler errorHandler;
	
	List<Guild> guilds;
	Map<String, Guild> guildIds;
	Map<Guild, TextChannel> logs;
	
	public BotHandler(Bot bot) {
//		super(bot.getBotBase());
		this.bot = bot;
		errorHandler = new ErrorHandler();
		
		guilds = new ArrayList<>();
		guildIds = new HashMap<>();
		logs = new HashMap<>();
	}
	
	public Bot getBot() {
		return bot;
	}
	
	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}

	public TextChannel createLogsChannel(Guild guild) {
		try {
			setLogs(guild, guild.getTextChannelsByName("logs", true).get(0));
			return getLogsChannel(guild);			
		} catch (Exception e) {
			guild.createTextChannel("logs").queue();
			try {Thread.sleep(1000);} catch (InterruptedException e1) {} // wait 1 second because channel needs time to create
			setLogs(guild, guild.getTextChannelsByName("logs", true).get(0));
			return getLogsChannel(guild);
		}
	}

	public TextChannel getLogsChannel(Guild guild) {
		if(logs.containsKey(guild))
			return logs.get(guild);
		return createLogsChannel(guild);
	}
	
	private void setLogs(Guild guild, TextChannel channel) {
		logs.put(guild, channel);
	}
	
	public Guild getGuildById(String id) {
		if(!guildIds.containsKey(id))
			return null;
		return guildIds.get(id);
	}
	
	public void addGuildId(Guild guild) {
		if(guildIds.containsKey(guild.getId()))
			return;
		guildIds.put(guild.getId(), guild);
	}

	public void addGuild(Guild guild) {
		guilds.add(guild);
	}
	
	public void setActivity(String activityType, String activityValue) {
		Presence jdaPresence = bot.getJda().getPresence();
		if(activityType.equals("competing")) {
			jdaPresence.setActivity(Activity.competing(activityValue));;

		} else if(activityType.equals("playing")) {
			jdaPresence.setActivity(Activity.playing(activityValue));;

		} else if(activityType.equals("listening")) {
			jdaPresence.setActivity(Activity.listening(activityValue));;

		} else if(activityType.equals("watching")) {
			jdaPresence.setActivity(Activity.watching(activityValue));;
		} else {
			jdaPresence.setActivity(Activity.listening("Activity change error"));
			bot.getBotHandler().getErrorHandler().couldNotChangeActivity(activityType, activityValue);
		}
	}
}
