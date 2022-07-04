package de.officeryoda.Commands.Public;

import de.officeryoda.Bot.Bot;
import de.officeryoda.Commands.Structure.PublicCommand;
import de.officeryoda.Music.MusicController;
import de.officeryoda.Utils.Config;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class cmdJoinLeave {

	public static class Join implements PublicCommand {

		@SuppressWarnings("deprecation")
		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {
			if(!guild.getSelfMember().hasPermission(channel, Permission.VOICE_CONNECT)) {
				channel.sendMessage("I don't have permissions to join a voice channel!").queue();
				return;
			}
			// Creates a variable equal to the channel that the user is in.
			VoiceChannel connectedChannel = author.getVoiceState().getChannel();
			// Checks if they are in a channel - not being in a channel means that the variable = null.
			if(connectedChannel == null) {
				channel.sendMessage("You are not connected to a voice channel!").queue();
				return;
			}

			AudioManager audioManager = guild.getAudioManager();
			if(audioManager.isAttemptingToConnect()) {
				channel.sendMessage("The bot is trying to connect! Enter the chill zone!").queue();
				return;
			}

			if(guild.getSelfMember().getVoiceState().getChannel() != null)
				if(guild.getSelfMember().getVoiceState().getChannel() == connectedChannel){
					channel.sendMessage("The bot is already connected to your channel.").queue();
					return;
				}

			channel.sendMessage("Connecting to *" + connectedChannel.getName() + "*!").queue(msg -> {
				audioManager.openAudioConnection(connectedChannel);
				msg.editMessage("Connected to *" + connectedChannel.getName() + "*!").queue();
			});
		}
	}

	public static class Leave implements PublicCommand {

		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {
			VoiceChannel connectedChannel = guild.getSelfMember().getVoiceState().getChannel();
			if(connectedChannel == null) {
				channel.sendMessage("Bot is currently not connected to a voice channel!").queue();
				return;
			}

			channel.sendMessage("Disconnecting from *" + connectedChannel.getName() + "*!").queue(msg -> {
				guild.getAudioManager().closeAudioConnection();
				MusicController controller = null;
				controller = bot.getPlayerManager().getController(guild.getId());

				controller.getPlayer().stopTrack();
				msg.editMessage("Disconnected from *" + connectedChannel.getName() + "*!").queue();
			});
		}

	}

	public static class AutoLeave implements PublicCommand {

		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {
			Config config = bot.getFileManager().getConfig();
			String path = guild.getId() + ".music.leaveOnTrackEnd";
			boolean crntState = (boolean) config.get(path, false);
			if(args.length >= 2) {
				switch (args[1]) {
				case "true":
					config.put(path, true);
					break;
				case "false":
					config.put(path, false);
					break;
				case "toggle":
					config.put(path, !crntState);
					break;
				default:
					config.put(path, !crntState);
					break;
				}
				channel.sendMessage("Leaving if nothing is left to play is currently set to: " + (boolean) config.get(path, false)).queue();
				return;
			}
			channel.sendMessage("Leaving if nothing is left to play is currently set to: ``" + (boolean) config.get(path, false) + "``").queue();
		}

	}
}
