package de.officeryoda.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.officeryoda.Bot.Bot;
import de.officeryoda.Utils.Config;
import net.dv8tion.jda.api.entities.Guild;

public class MusicController {
	
	private Bot bot;
	private Config config;
	
	private Guild guild;
	private AudioPlayer player;
	private Queue queue;
	
	
	public MusicController(Bot bot, Guild guild) {
		this.bot = bot;
		this.config = bot.getFileManager().getConfig();
		
		this.guild = guild;
		this.player = bot.getAudioPlayerManager().createPlayer();
		this.queue = new Queue(this);
		
		this.guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
		this.player.addListener(new TrackScheduler(bot));
		this.player.setVolume(getVolume());
	}
	
	public Guild getGuild() {
		return guild;
	}
	
	public AudioPlayer getPlayer() {
		return player;
	}
	
	public void setVolume(int volume) {
		config.put(guild.getId() + ".music.volume", volume);
		this.player.setVolume(getVolume());
	}
	
	public int getVolume() {
		return (int) config.get(guild.getId() + ".music.volume", 10); //Standartm‰ﬂig auf 100, wenn man lautst‰rke nihct manuell ‰ndert
	}
	
	public Queue getQueue()  {
		return this.queue;
	}
	
	public Bot getBot() {
		return bot;
	}
}