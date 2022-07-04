package de.officeryoda.Commands.Private;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import de.officeryoda.Bot.Bot;
import de.officeryoda.Commands.Structure.PrivateCommand;
import de.officeryoda.Music.AudioLoadResult;
import de.officeryoda.Music.MusicController;
import de.officeryoda.Music.Queue;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class cmdPlay {
	
	public static class Play implements PrivateCommand {
		
		@Override
		public void executeCommand(Bot bot, User user, PrivateChannel channel, Message message, String[] args) {
			
			Guild guild = bot.getBotHandler().getGuildById(args[1]);
			VoiceChannel vc = guild.getVoiceChannelById(args[2]);
			
			if(args.length <= 1) {
				channel.sendMessage("Usage: " + bot.getPrefix() + "play <url/song name>").queue();
				return;
			}
			
			MusicController controller = bot.getPlayerManager().getController(guild.getId());
			Queue queue = controller.getQueue();
			AudioPlayerManager apm = bot.getAudioPlayerManager();
			AudioManager manager = guild.getAudioManager();
			
			queue.setPrCmdChannel(channel);
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
	
}
