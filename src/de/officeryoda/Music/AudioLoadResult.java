 package de.officeryoda.Music;

import java.awt.Color;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.officeryoda.Bot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class AudioLoadResult implements AudioLoadResultHandler {
	
	private final Bot bot;
	private final MusicController controller;
	private final String sUrl;
	private final boolean looping;
	
	public AudioLoadResult(MusicController controller, String sUrl, boolean looping) {
		this.bot = controller.getBot();
		this.controller = controller;
		this.sUrl = sUrl;
		this.looping = looping;
	}
	
	@Override
	public void trackLoaded(AudioTrack track) {
		Queue queue = controller.getQueue();
		if(looping)
			controller.getPlayer().playTrack(track);
		else
			queue.addTrackToQueue(track, false);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		Queue queue = controller.getQueue();
		if(sUrl.startsWith("ytsearch: ")) {
			queue.addTrackToQueue(playlist.getTracks().get(0), false);
			return;
		}
		
		int added = 0;
		for(AudioTrack track : playlist.getTracks()) {
			queue.addTrackToQueue(track, true);
			added++;
		}
		
		Bot bot = controller.getBot();
		EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED)
				.setDescription("added " + added + " tracks to queue.")
				.setFooter(bot.getEmbedFooterTime(), bot.getProfilePictureUrl());
		
		TextChannel channel = controller.getQueue().getCmdChannel();
		channel.sendMessage(embed.build()).queue();
	}

	@Override
	public void noMatches() {
		controller.getQueue().getCmdChannel().sendMessage("No Video or Song found.").queue();
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		bot.getBotHandler().getErrorHandler().musicFailedToLoad(exception, bot, controller.getGuild());
	}

}
