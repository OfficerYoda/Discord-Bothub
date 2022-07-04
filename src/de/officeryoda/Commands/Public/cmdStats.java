package de.officeryoda.Commands.Public;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import de.officeryoda.Bot.Bot;
import de.officeryoda.Bot.FileManager;
import de.officeryoda.Commands.Structure.PublicCommand;
import de.officeryoda.Utils.stats.StatsType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class cmdStats {
	
	public static class Stats implements PublicCommand {
		
		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {
			if(message.getMentionedUsers().size() <= 0) 
				generalStats(bot, channel, guild);
			else
				personalStats(bot, message, channel);
		}
		
		private void generalStats(Bot bot, TextChannel channel, Guild guild) {
			FileManager fileManager = bot.getFileManager();
			de.officeryoda.Utils.stats.Stats stats = fileManager.getStats();
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("Stats", "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley");

			//Music
			embed.addField(":notes: **Music**", ":hourglass: **Listening Time:** " + getTimeFromSeconds((int) stats.get(StatsType.MUSIC_TIME.getPath(), 0)) + "\r\n"
					+ ":1234: **Songs Listend to:** " + stats.get("Music.songs", 0), false);

			embed.addBlankField(false);

			//Commands
			StringBuilder builder = new StringBuilder();
			for(String id : stats.getYamlFile().getConfigurationSection("Commands").getKeys(false)) {
				if(guild.getMemberById(id) == null) continue;
				builder.append("*" + guild.getMemberById(id).getEffectiveName() + ":*  " + stats.get("Commands." + id + ".uses", 0) + "\r\n");
			}

			embed.addField(":computer: Commands", ":1234: **Uses**\r\n" + builder.toString(), false);

			embed.addBlankField(false);

			//Footer
			embed.setFooter(bot.getEmbedFooterTime(), bot.getProfilePictureUrl());

			channel.sendMessage(embed.build()).queue();
		}

		private void personalStats(Bot bot, Message message, TextChannel channel) {
			de.officeryoda.Utils.stats.Stats stats = bot.getFileManager().getStats();
			
			for(User user : message.getMentionedUsers()) {
				String id = user.getId();
				String username = user.getName();
				String basePath = "Commands." + id + ".cmdUses";

				StringBuilder builder = new StringBuilder();
				EmbedBuilder embed = new EmbedBuilder();
				embed.setTitle("Stats", "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley");
				embed.setDescription("Stats from the User: **" + username + "**.\r\n"
						+ "Total uses: **" + stats.get("Commands." + id + ".uses", 0) + "**.");

				if(stats.contains("Commands." + id)) {

					//Commands
					if(stats.contains("Commands." + id + ".cmdUses")) {
						Map<String, Integer> commandMap = new HashMap<>();
						
						for(String command : stats.getYamlFile().getConfigurationSection(basePath).getKeys(false)) {
							commandMap.put(".*" + command.toLowerCase() + "*: ", (Integer) stats.get(basePath + "." + command, 0));
//							builder.append("*" + command.substring(0, 1) + command.substring(1).toLowerCase() + "*: " +  stats.get(basePath + "." + command, 0) + "\r\n");
						}
						
						commandMap = sortByValue(commandMap, false);
						
						for(String command : commandMap.keySet()) {
							builder.append(command + commandMap.get(command) + "\r\n");
						}
						
						embed.addField("Commands", builder.toString(), false);
					}

				} else {
					builder.append("The stats file does NOT ");
					builder.append("contain any stats for this user");
					embed.addField("No Stats found", builder.toString(), false);
				}

				//Footer
				embed.setFooter(bot.getEmbedFooterTime(), bot.getProfilePictureUrl());

				channel.sendMessage(embed.build()).queue();

			}
		}

		public String getTimeFromSeconds(int seconds) {
			String time = "";

			int minutes = seconds / 60;
			int hours = minutes / 60;
			int days = hours / 24;
			seconds %= 60;
			minutes %= 60;
			hours %= 60;
			days %= 24;
			
			//Days
			if(days > 0)
				time += hours + ":";
			
			//Hours
			if(hours < 10 && days > 0)
				time += "0" + hours + ":";
			else
				time += hours + ":";
			
			//Minutes
			if(minutes < 10 && hours > 0)
				time += "0" + minutes + ":";
			else
				time += minutes + ":";
			
			//Seconds
			if(seconds < 10)
				time += "0" + seconds;
			else
				time += seconds + "";

			return time;
		}
		
	    private Map<String, Integer> sortByValue(Map<String, Integer> unsortMap, final boolean lowToHigh) {
	        List<Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

	        // Sorting the list based on values
	        list.sort((o1, o2) -> lowToHigh ? o1.getValue().compareTo(o2.getValue()) == 0
	                ? o1.getKey().compareTo(o2.getKey())
	                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
	                ? o2.getKey().compareTo(o1.getKey())
	                : o2.getValue().compareTo(o1.getValue()));
	        return list.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b, LinkedHashMap::new));

	    }
	}
}
