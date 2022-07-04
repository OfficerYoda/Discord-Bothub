package de.officeryoda.Bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import de.officeryoda.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class BotNetwork {

	public static BotNetwork INSTANCE;

	List<Bot> onlineBots;

	public BotNetwork() {
		INSTANCE = this;

		onlineBots = sortBotsById(Main.INSTANCE.getOnlineBots(), true);
	}

	private List<Bot> sortBotsById(List<Bot> botsToSort, boolean lowToHigh) {

		Map<String, Integer> sortMap = new HashMap<>();

		for (Bot bot : botsToSort)
			sortMap.put(bot.getTrueName(), bot.getBotBase().getId());

		sortMap = sortByValue(sortMap, lowToHigh);

		botsToSort = new ArrayList<>();
		for(String botname : sortMap.keySet())
			botsToSort.add(Main.INSTANCE.getBotByTrueName(botname));

		return botsToSort;
	}

	private Map<String, Integer> sortByValue(Map<String, Integer> unsortMap, boolean lowToHigh) {
		List<Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

		// Sorting the list based on values
		list.sort((o1, o2) -> lowToHigh ? o1.getValue().compareTo(o2.getValue()) == 0
				? o1.getKey().compareTo(o2.getKey())
						: o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
						? o2.getKey().compareTo(o1.getKey())
								: o2.getValue().compareTo(o1.getValue()));
		return list.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b, LinkedHashMap::new));
	}

	public boolean canPlay(Bot callingBot, Guild guild, Member member) {
		//if its the most superior bot return true
		if(getBotsInVoiceChats(guild) == 0)
			return mostSuperiorInGuild(callingBot, guild);

		GuildVoiceState cbGvs = guild.getSelfMember().getVoiceState();
		if(cbGvs.getChannel() != null) {
			//already in a call with the cmd.-executer?
			if(cbGvs.getChannel() == member.getVoiceState().getChannel())
				return true;
			//is already in a call with smb.?
			if(cbGvs.getChannel().getMembers().size() >= 2)
				return false;				
		}

		for (Bot bot : onlineBots) {
			if(bot == callingBot) continue;
			GuildVoiceState gvs = bot.getBotHandler().getGuildById(guild.getId()).getSelfMember().getVoiceState();
			if(gvs.getChannel() == null) continue;
			if(gvs.getChannel().getIdLong() == member.getVoiceState().getChannel().getIdLong())
				return false;
		}

		//all superior are in a vc
		if(getAvaliableSuperiorBots(callingBot, guild, member.getVoiceState().getChannel()) == 0) {
			//goes through all superior bots
			for(Bot bot : onlineBots) {
				if(bot.getBotBase().getId() >= callingBot.getBotBase().getId()) continue; //checks if superior
				GuildVoiceState gvs = bot.getBotHandler().getGuildById(guild.getId()).getSelfMember().getVoiceState();
				if(gvs.getChannel() != null)
					if(gvs.getChannel().getMembers().size() <= 1) //if superior bot is alone in a call
						return false;
			}
			//all superior bots are not alone in a call
			return true;
		}

		return false;
	}

	private boolean mostSuperiorInGuild(Bot callingBot, Guild guild) {
		List<Bot> bots = new ArrayList<>();
		bots.addAll(onlineBots);

		//get the bots in the guild
		for (Bot bot : onlineBots)
			if (bot.getBotHandler().getGuildById(guild.getId()) == null)
				bots.remove(bot);

		int id = callingBot.getBotBase().getId();
		for(Bot bot : bots)
			if(bot.getBotBase().getId() < id)
				id = bot.getBotBase().getId();

		return id >= callingBot.getBotBase().getId();
	}

	private int getBotsInVoiceChats(Guild guild) {
		int count = 0;
		for (Bot bot : onlineBots)
			if(bot.getBotHandler().getGuildById(guild.getId()).getSelfMember().getVoiceState().getChannel() != null)
				count++;
		return count;
	}

	private int getAvaliableSuperiorBots(Bot callingBot, Guild guild, VoiceChannel channel) {
		List<Bot> bots = new ArrayList<>();
		bots.addAll(onlineBots);

		//get the bots in the guild
		for (Bot bot : onlineBots)
			if (bot.getBotHandler().getGuildById(guild.getId()) == null)
				bots.remove(bot);

		final int id = callingBot.getBotBase().getId();
		int availableBots = 0;
		for(Bot bot : bots) {
			GuildVoiceState gvs = bot.getBotHandler().getGuildById(guild.getId()).getSelfMember().getVoiceState();
			if(bot.getBotBase().getId() < id && gvs.getChannel() != null)
				if(gvs.getChannel().getMembers().size() <= 1)
					availableBots++;

		}
		return availableBots;
	}
}
