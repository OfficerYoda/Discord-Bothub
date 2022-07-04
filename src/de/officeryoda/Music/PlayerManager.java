package de.officeryoda.Music;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.officeryoda.Bot.Bot;
import net.dv8tion.jda.api.entities.Guild;

public class PlayerManager {

	private Bot bot;
	public ConcurrentHashMap<String, MusicController> controller;
	public Map<Guild, Boolean> looping;

	public PlayerManager(Bot bot) {
		this.bot = bot;
		this.controller = new ConcurrentHashMap<>();
		this.looping = new HashMap<>();
	}

	public MusicController getController(String guildId) {
		MusicController mc = null;

		if(this.controller.containsKey(guildId)) {
			mc = this.controller.get(guildId);
		} else {
			mc = new MusicController(bot, bot.getBotHandler().getGuildById(guildId));

			this.controller.put(guildId, mc);
		}

		return mc;
	}

	public String getGuildIdByPlayerHash(int hash) {
		for(MusicController controller : this.controller.values())
			if(controller.getPlayer().hashCode() == hash)
				return controller.getGuild().getId();

		return "";
	}

	public boolean isLooping(Guild guild) {
		return looping.containsKey(guild) ? looping.get(guild) : false;
	}

	public boolean setLooping(Guild guild, boolean value) {
		looping.put(guild, value);
		return value;
	}

	public Bot getBot() {
		return bot;
	}
}
