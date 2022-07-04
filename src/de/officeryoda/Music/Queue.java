package de.officeryoda.Music;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.officeryoda.Bot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;

public class Queue {

	private Bot bot;
	private List<AudioTrack> queueList;
	private MusicController controller;
	private boolean playing;
	private TextChannel cmdChannel;
	private PrivateChannel prCmdChannel;
	private boolean isPrCmdChannel;

	public Queue(MusicController controller) {
		this.bot = controller.getBot();
		this.controller = controller;
		this.queueList = new ArrayList<>();
	}

	public boolean next() {
		if(this.queueList.size() == 0) return false;
		AudioTrack track = queueList.remove(0);

		if(track == null) return false;

		if(!isPlaying())
			sendPlayEmbed(track);

		this.controller.getPlayer().playTrack(track);

		return true;
	}

	public void addTrackToQueue(AudioTrack track, boolean isPlaylist) {
		this.queueList.add(track);

		if(controller.getPlayer().getPlayingTrack() == null)
			next();
		else if(!isPlaylist)
			controller.getQueue().getCmdChannel().sendMessage(":notes: Added **" + track.getInfo().title + "** to queue.").queue();;
	}

	public MusicController getController() {
		return controller;
	}

	public void setController(MusicController controller) {
		this.controller = controller;
	}

	public List<AudioTrack> getQueueList() {
		return queueList;
	}

	public void setQueueList(List<AudioTrack> queueList) {
		this.queueList = queueList;
	}

	public int getLength() {
		return this.queueList.size();
	}

	public void shuffle() {
		Collections.shuffle(queueList);
	}

	public void move(int from, int to) {
		List<AudioTrack> list = this.queueList;

		if(from < to) {
			List<AudioTrack> subList = list.subList(from-1, to);
			Collections.rotate(subList, -1);
		} else {
			List<AudioTrack> subList = list.subList(to-1, from);
			Collections.rotate(subList, 1);
		}

		return;
	}

	private void sendPlayEmbed(AudioTrack track) {
		if(controller.getPlayer().getPlayingTrack() == null) {
			AudioTrackInfo info = track.getInfo();
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(Color.decode("#00e640"));
			embed.setTitle(":notes: playing: **" + info.title + "**", info.uri);

			String time = "";

			long seconds = info.length / 1000;
			long minutes = seconds / 60;
			long hours = minutes / 60;
			seconds %= 60;
			minutes %= 60;
			hours %= 60;

			//Hours
			if(hours > 0)
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

			String url = info.uri;
			embed.addField(info.author, "[" + info.title + "](" + url + ")", false);
			embed.addField("Lenght: ", info.isStream ? ":red_circle: STREAM" : time, true);
			embed.setFooter(bot.getEmbedFooterTime(), bot.getProfilePictureUrl());

			if(url.startsWith("https://www.youtube.com/watch?v=")) {
				String videoID = url.replace("https://www.youtube.com/watch?v=", "");

				InputStream file;
				try {
					file = new URL("https://img.youtube.com/vi/" + videoID + "/hqdefault.jpg").openStream();
					embed.setImage("attachment://thumbnail.png");

					cmdChannel.sendFile(file,  "thumbnail.png").embed(embed.build()).queue();

				} catch (IOException e) {}
			} else {
				if(isPrCmdChannel) {
					prCmdChannel.sendMessage(embed.build()).queue();
					isPrCmdChannel = false;
				} else {
					cmdChannel.sendMessage(embed.build()).queue();
				}
			}
		}
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public void setCmdChannel(TextChannel cmdChannel) {
		this.isPrCmdChannel = false;
		this.cmdChannel = cmdChannel;
	}

	public TextChannel getCmdChannel() {
		return cmdChannel;
	}

	public void setPrCmdChannel(PrivateChannel prCmdChannel) {
		this.isPrCmdChannel = true;
		this.prCmdChannel = prCmdChannel;
	}
}
