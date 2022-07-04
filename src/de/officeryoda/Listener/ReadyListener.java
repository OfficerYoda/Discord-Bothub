package de.officeryoda.Listener;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import de.officeryoda.Bot.Bot;
import de.officeryoda.Bot.BotHandler;
import de.officeryoda.Utils.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter {
	
	BotHandler handler;
	
	Config config;

	public ReadyListener(Bot bot) {
		handler = bot.getBotHandler();
		
		config = bot.getFileManager().getConfig();
	}
	
	public void onReady(ReadyEvent event) {
		List<Guild> guilds = event.getJDA().getGuilds();
		for(Guild guild : guilds) {
			handler.createLogsChannel(guild);
			//send bot start confimation
			if(guild.getId().equals(/*DC Bot test server id: */"918946466142228501")){
				EmbedBuilder embed = new EmbedBuilder();
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd  HH:mm:ss");  
				LocalDateTime now = LocalDateTime.now();  

				embed.setTitle("**Bot booted succesfully**");
				embed.setColor(Color.LIGHT_GRAY);
				embed.setDescription(":man: **Host:** " + System.getProperty("user.home").replace("C:\\Users\\", "")
						+ "\n:hourglass: **Time:** " + (System.currentTimeMillis() - handler.getBot().getStartTime()) + " ms");
				embed.setFooter(dtf.format(now), guild.getSelfMember().getUser().getAvatarUrl());
				handler.getLogsChannel(guild).sendMessage(embed.build()).queue();
				embed.clear();
			}
			handler.addGuildId(guild);
			handler.addGuild(guild);
			config.put(guild.getId() + ".name", guild.getName());
		}
	}

	public void onGuildJoin(GuildJoinEvent event) {
		Guild guild = event.getGuild();
		handler.addGuildId(guild);
	}

	public void onGuildUpdateName(GuildUpdateNameEvent event) {
		Guild guild = event.getGuild();
		config.put(guild.getId() + ".name", guild.getName());
	}
}