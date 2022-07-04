package de.officeryoda.Commands.Public;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.officeryoda.Bot.Bot;
import de.officeryoda.Commands.Structure.PublicCommand;
import de.officeryoda.Music.MusicController;
import de.officeryoda.Music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class cmdQueue {
	
	public static class TrackInfo implements PublicCommand {
		
		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd  HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();  

			MusicController controller = bot.getPlayerManager().getController(channel.getGuild().getId());
			AudioPlayer player = controller.getPlayer();
			AudioTrack track;

			if((track = player.getPlayingTrack()) == null) {
				channel.sendMessage("Es läuft gerade kein Song.").queue();
				return;
			}
			AudioTrackInfo info = track.getInfo();

			String trackAuthor = info.author;
			String title = info.title;
			String url = info.uri;
			boolean isStream = info.isStream;
			long lenght = info.length;
			long position = track.getPosition();

			long curSeconds = position / 1000;
			long curMinutes = curSeconds / 60;
			long curHours = curMinutes / 60;
			curSeconds %= 60;
			curMinutes %= 60;

			long maxSeconds = lenght / 1000;
			long maxMinutes = maxSeconds / 60;
			long maxHours = maxMinutes / 60;
			maxSeconds %= 60;
			maxMinutes %= 60;
			
			String time = ((curHours > 0) ? curHours + ":" : "") + curMinutes + ":" + curSeconds + " / " + ((maxHours > 0) ? maxHours + ":" : "") + maxMinutes + ":" + maxSeconds;

			EmbedBuilder embed = new EmbedBuilder();

			embed.setTitle(title, url);
			embed.setAuthor(trackAuthor);
			embed.setDescription(isStream ? ":red_circle: STREAM" : "-> " + time);
			embed.setFooter(dtf.format(now), guild.getSelfMember().getUser().getAvatarUrl());
			
			if(url.startsWith("https://www.youtube.com/watch?v=")) {
				String videoID = url.replace("https://www.youtube.com/watch?v=", "");
				
				InputStream file;
				try {
					file = new URL("https://img.youtube.com/vi/" + videoID + "/hqdefault.jpg").openStream();
					embed.setImage("attachment://thumbnail.png");
					
					channel.sendFile(file,  "thumbnail.png").embed(embed.build()).queue();
					
				} catch (IOException e) {}
			} else {
				channel.sendMessage(embed.build()).queue();
			}
		}
	}
	
	public static class Shuffle implements PublicCommand {
		
		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {
			GuildVoiceState state;
			VoiceChannel vc;

			if((state = author.getVoiceState()) == null || (vc = state.getChannel()) == null) {
				channel.sendMessage("You must be in a voice channel to use that!").queue();
				return;
			}

			MusicController controller = bot.getPlayerManager().getController(vc.getGuild().getId());

			controller.getQueue().shuffle();

			channel.sendMessage("Shuffled the queue.").queue();
		}
	}
	
	public static class Skip implements PublicCommand {
		
		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {
			GuildVoiceState state;
			VoiceChannel vc;

			if((state = author.getVoiceState()) == null || (vc = state.getChannel()) == null) {
				channel.sendMessage("You must be in a voice channel to use that!").queue();
				return;
			}

			MusicController controller = bot.getPlayerManager().getController(vc.getGuild().getId());
			
			if(controller.getQueue().getLength() > 0)
				channel.sendMessage("Skipping the current song.").queue(msg -> {
					controller.getQueue().next();
					msg.editMessage("Skipped the current song").queue();
				});
			else
				channel.sendMessage("There are no songs to skip to. Use `" + bot.getPrefix() + "stop` to stop the song.").queue();
		}
	}
	
	public static class Loop implements PublicCommand {
		
		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {
			GuildVoiceState state;
			@SuppressWarnings("unused")
			VoiceChannel vc;
			if((state = author.getVoiceState()) == null || (vc = state.getChannel()) == null) {
				channel.sendMessage("You must be in a voice channel to use that!").queue();
				return;
			}
			
			PlayerManager playerManager = bot.getPlayerManager();
			if(playerManager.setLooping(guild, !playerManager.isLooping(guild)))
				channel.sendMessage("Looping ``enabled``.").queue();
			else
				channel.sendMessage("Looping ``disabled``.").queue();
		}
	}
	
	public static class Queue implements PublicCommand {
		
		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {
			MusicController controller = bot.getPlayerManager().getController(guild.getId());
			de.officeryoda.Music.Queue queue = controller.getQueue();
			if(args.length >= 2) {
				switch (args[1].toLowerCase()) {
				case "clear":
					queue.setQueueList(new ArrayList<>());
					channel.sendMessage("Queue cleared.").queue();
					break;
				case "shuffle":
					((PublicCommand) bot.getCmdHandler().getCommand("shuffle").getCmdClass()).executeCommand(bot, message, args, author, guild, channel);
					break;
				case "move":
					if(args.length < 4) {
						channel.sendMessage("Usage: " + bot.getPrefix() + "queue <from> <to>").queue();
						return;
					}
					
					int from = Integer.parseInt(args[2]);
					int to = Integer.parseInt(args[3]);
			    	if(from > queue.getLength() || to > queue.getLength()) {
			    		channel.sendMessage("The Queue is not that long").queue();
			    		return;
			    	}
			    	if(from < 0 || to < 0) {
			    		channel.sendMessage("Can't move something from the negatives").queue();
			    		return;
			    	}
			    	if(from == to) {
			    		channel.sendMessage("Done").queue();
			    		return;
			    	}
					
			    	queue.move(from, to);
			    	
					channel.sendMessage("Done").queue();
			    	
					break;
				default:
					int page;
					try {
						page = Integer.parseInt(args[1]);
						sendQueuePage(bot, channel, controller, page);
					} catch (NumberFormatException e) {
						channel.sendMessage("Invalid argument ``(" + args[1] + ")``!").queue();
					}
					break;
				}
			} else {
				sendQueuePage(bot, channel, controller, 0);
			}
		}
		
		private void sendQueuePage(Bot bot, TextChannel channel, MusicController controller, int page) {
			List<AudioTrack> queue = controller.getQueue().getQueueList();
			EmbedBuilder embed = new EmbedBuilder();
			StringBuilder builder = new StringBuilder();
			embed.setFooter(bot.getEmbedFooterTime(), bot.getProfilePictureUrl());

			embed.setTitle(":notes: **Current Queue | ** " + (queue.size() == 1 ? queue.size() + " song" : queue.size() + " songs"));
			//currently playing
			embed.addField(":arrow_forward: Currently playing",
					(controller.getPlayer().getPlayingTrack() != null) ? controller.getPlayer().getPlayingTrack().getInfo().title : "*No Song playing*", false);

			if(queue.size() > 0) {
				int queueSize = queue.size();
				int maxPage = (int) Math.ceil(queueSize / 10 + 1);
				if(page <= 0) page = 1;
				if(page > queueSize) page = maxPage;

				//get the part of the queue needed
				int startValue = (page - 1) * 10;
				int endValue = startValue + 10;
				//get start value right
				if(startValue > queueSize)
					startValue = queueSize - 10;
				if(startValue < 0)
					startValue = 0;
				//get end value right
				if(endValue > queueSize)
					endValue = queueSize; 

				//build the queue
				for(int i = startValue; i < endValue; i++) {
					builder.append("**[" + (i+1) + "]** " + queue.get(i).getInfo().title + "\r\n");
				}
				embed.setFooter("page " + page + "/" + maxPage);
			} else {builder.append("*Queue is empty*");}	

			embed.addField("**QUEUE**", builder.toString(), false);
			channel.sendMessage(embed.build()).queue();
		}
	}
}
