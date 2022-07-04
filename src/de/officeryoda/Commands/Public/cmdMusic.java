package de.officeryoda.Commands.Public;

import java.util.ArrayList;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import de.officeryoda.Bot.Bot;
import de.officeryoda.Bot.BotNetwork;
import de.officeryoda.Commands.Structure.PublicCommand;
import de.officeryoda.Music.AudioLoadResult;
import de.officeryoda.Music.MusicController;
import de.officeryoda.Music.Queue;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class cmdMusic {

	public static class Play implements PublicCommand {

		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {
			GuildVoiceState state;
			VoiceChannel vc;

			if(args.length <= 1) {
				channel.sendMessage("Usage: " + bot.getPrefix() + "play <url/song name>").queue();
				return;
			}

			if((state = author.getVoiceState()) == null || (vc = state.getChannel()) == null) {
				channel.sendMessage("You must be in a voice channel to use that!").queue();
				return;
			}


			MusicController controller = bot.getPlayerManager().getController(guild.getId());
			Queue queue = controller.getQueue();
			AudioPlayerManager apm = bot.getAudioPlayerManager();
			AudioManager manager = guild.getAudioManager();
			
			if(queue.getLength() == 0)
				if(!BotNetwork.INSTANCE.canPlay(bot, guild, author))
					return;
			
			if(queue.getLength() != 0)
				if(guild.getSelfMember().getVoiceState().getChannel() != author.getVoiceState().getChannel())
					channel.sendMessage("I'm not in your voiceChannel").queue();
			
			queue.setCmdChannel(channel);
			manager.openAudioConnection(vc);

			StringBuilder builder = new StringBuilder();
			for(int i = 1; i < args.length; i++)
				builder.append(args[i] + " ");

			String url = builder.toString().trim();
			if(!url.startsWith("http")) {
				url = "ytsearch: " + url;
			}
			apm.loadItem(url, new AudioLoadResult(controller, url, false));
		}
	}

	public static class Stop implements PublicCommand {

		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {
			GuildVoiceState state;
			@SuppressWarnings("unused")
			VoiceChannel vc;
			if((state = author.getVoiceState()) == null || (vc = state.getChannel()) == null) {
				channel.sendMessage("You must be in a voice channel to use that!").queue();
				return;
			}

			MusicController controller = bot.getPlayerManager().getController(guild.getId());
			AudioPlayer player = controller.getPlayer();

			channel.sendMessage("Trying to stop playing.").queue(msg -> {
				if(player.getPlayingTrack() == null) {
					msg.editMessage("Nothing is playing.").queue();
					return;
				}

				player.stopTrack();
				guild.getAudioManager().closeAudioConnection();
				controller.getQueue().setQueueList(new ArrayList<>());
				msg.editMessage("Stopped playing and cleared the queue.").queue();
			});
		}
	}

	public static class Pause implements PublicCommand {

		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {
			GuildVoiceState state;
			@SuppressWarnings("unused")
			VoiceChannel vc;
			if((state = author.getVoiceState()) == null || (vc = state.getChannel()) == null) {
				channel.sendMessage("You must be in a voice channel to use that!").queue();
				return;
			}

			MusicController controller = bot.getPlayerManager().getController(guild.getId());
			AudioPlayer player = controller.getPlayer();


			channel.sendMessage("Trying to pause.").queue(msg -> {
				if(player.getPlayingTrack() == null) {
					msg.editMessage("Nothing found what could be paused.").queue();
					return;
				}

				player.setPaused(true);
				msg.editMessage("Paused successfully.").queue();
			});
		}
	}

	public static class Resume implements PublicCommand {

		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {
			GuildVoiceState state;
			@SuppressWarnings("unused")
			VoiceChannel vc;
			if((state = author.getVoiceState()) == null || (vc = state.getChannel()) == null) {
				channel.sendMessage("You must be in a voice channel to use that!").queue();
				return;
			}

			MusicController controller = bot.getPlayerManager().getController(guild.getId());
			AudioPlayer player = controller.getPlayer();


			channel.sendMessage("Trying to resume.").queue(msg -> {
				if(player.getPlayingTrack() == null) {
					msg.editMessage("Nothing found what could be resumed.").queue();
					return;
				}

				if(player.isPaused()) {
					player.setPaused(false);
					msg.editMessage("Started playing again.").queue();
				} else {
					msg.editMessage("Already Playing").queue();
				}
			});
		}
	}

	public static class Volume implements PublicCommand {

		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {
			if(args.length == 1) {
				channel.sendMessage("The current volume is: **" + bot.getPlayerManager().getController(guild.getId()).getVolume() + "**.").queue();
				return;
			}
			int volume;
			try {
				volume = Integer.valueOf(args[1]);
			} catch (NumberFormatException e) {
				channel.sendMessage("``" + args[1] + "`` is not a valid number.").queue();
				return;
			}

			bot.getPlayerManager().getController(guild.getId()).setVolume(volume);;
			channel.sendMessage("Volume set to **" + volume + "**.").queue();
		}
	}

	public static class SetTime implements PublicCommand {

		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {
			GuildVoiceState state;
			VoiceChannel vc;

			if((state = author.getVoiceState()) == null || (vc = state.getChannel()) == null) {
				channel.sendMessage("You must be in a voice channel to use that!").queue();
				return;
			}

			MusicController controller = bot.getPlayerManager().getController(vc.getGuild().getId());
			AudioPlayer player = controller.getPlayer();

			if(player.getPlayingTrack() == null) {
				channel.sendMessage("Nothing is playing");
				return;
			}
			if(args.length < 2) {
				channel.sendMessage("Usage: " + bot.getPrefix() + "settime <time(in Seconds)/XX:XX)").queue();
				return;
			}
			int seconds = secondsFromString(args, channel);
			if(seconds == -1) return;

			if(seconds > player.getPlayingTrack().getInfo().length/1000) {
				channel.sendMessage("The Song is not that long `(" + getTimeFromSeconds(seconds) + "/"
						+ getTimeFromSeconds((int) (player.getPlayingTrack().getInfo().length/1000)) + ")`").queue();
				return;
			}

			player.getPlayingTrack().setPosition(seconds*1000);//needs milliseconds
			channel.sendMessage("Set Time to `" + getTimeFromSeconds(seconds) + "`. ").queue();
		}

		private int secondsFromString(String[] args, TextChannel channel) {

			String[] time = args[1].split(":");
			for(String string : time) 
				try {
					Integer.parseInt(string);
				} catch (Exception e) {
					channel.sendMessage("``" + string + "`` is not a valid Number").queue();
					return -1;
				}
			int timeLenght = time.length;
			//Seconds
			int seconds = Integer.parseInt(time[timeLenght-1]);
			//Minutes
			if(timeLenght > 1)
				seconds += Integer.parseInt(time[timeLenght-2])*60;
			//Hours
			if(timeLenght > 2)
				seconds += Integer.parseInt(time[timeLenght-3])*60*60;
			//Days
			if(timeLenght > 3)
				seconds += Integer.parseInt(time[timeLenght-3])*60*60*24;

			return seconds;
		}

		public String getTimeFromSeconds(int seconds) {
			String time = "";

			int minutes = seconds / 60;
			int hours = minutes / 60;
			int days = hours / 24;
			seconds %= 60;
			minutes %= 60;
			hours %= 24;

			//Days
			if(days > 0)
				time += days + ":";

			//Hours
			if(hours < 10 && days > 0)
				time += "0" + hours + ":";
			else if(hours > 0 || days > 0)
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
	}
}
