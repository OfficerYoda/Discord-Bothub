package de.officeryoda.Music;

import java.util.HashMap;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import de.officeryoda.Bot.Bot;
import de.officeryoda.Bot.FileManager;
import de.officeryoda.Utils.Config;
import de.officeryoda.Utils.stats.Stats;
import de.officeryoda.Utils.stats.StatsType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.managers.AudioManager;

public class TrackScheduler extends AudioEventAdapter {

	Bot bot;
	HashMap<Guild, String> lastUri;

	Config config;
	Stats stats;

	public TrackScheduler(Bot bot) {
		this.bot = bot;
		lastUri = new HashMap<>();

		FileManager fileHandler = bot.getFileManager();
		config = fileHandler.getConfig();
		stats = fileHandler.getStats();
	}

	@Override
	public void onPlayerResume(AudioPlayer player) {
		//		Main.getMainGui().updateConsole();
	}

	@Override
	public void onPlayerPause(AudioPlayer player) {
		//		Main.getMainGui().updateConsole();
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		stats.add(StatsType.MUSIC_SONGS.getPath(), 1);
		//		Main.getMainGui().updateConsole();

		String guildId = bot.getPlayerManager().getGuildIdByPlayerHash(player.hashCode());
		Guild guild = bot.getBotHandler().getGuildById(guildId);
		MusicController controller = bot.getPlayerManager().getController(guildId);
		Queue queue = controller.getQueue();
		queue.setPlaying(true);

		setLastUri(guild, track.getInfo().uri);
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		stats.add(StatsType.MUSIC_TIME.getPath(), (int) (track.getPosition() / 1000));
		//		Main.getMainGui().updateConsole();
		if(!endReason.mayStartNext) return;

		PlayerManager playerManager = bot.getPlayerManager();
		String guildId = playerManager.getGuildIdByPlayerHash(player.hashCode());
		Guild guild = bot.getBotHandler().getGuildById(guildId);
		MusicController controller = playerManager.getController(guildId);
		Queue queue = controller.getQueue();

		if(playerManager.isLooping(guild)) {
			String lastUri = getLastUri(guild);
			bot.getAudioPlayerManager().loadItem(lastUri, new AudioLoadResult(controller, lastUri, playerManager.isLooping(guild)));
			return;
		}

		if(queue.next())
			return;

		queue.setPlaying(false);
		player.stopTrack();
		AudioManager manager = guild.getAudioManager();
		String path = guildId + ".music.leaveOnTrackEnd";
		if(!config.contains(path)) {
			config.put(path, false);
		} else {
			if ((boolean) config.get(path, false)) {
				manager.closeAudioConnection();
				guild.getAudioManager().closeAudioConnection();
			}
		}
	}

	public String getLastUri(Guild guild) {
		return lastUri.containsKey(guild) ? lastUri.get(guild) : "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley";
	}

	public void setLastUri(Guild guild, String value) {
		lastUri.put(guild, value);
	}
}
