package de.officeryoda.Bot;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class ErrorHandler {

	public void discNotSet(TextChannel channel) {
		channel.sendMessage("Can't do that right now. ``" + "Errorcode: 1" + "``").queue();
//		MainGui.Console.println("[ERROR] in " + channel.getGuild().getName() + " | " + channel.getName()
//		+ "Disc isn't chosen (Errorcode: 1)");
		System.err.println("[ERROR] In " + channel.getGuild().getName() + " | " + channel.getName()
		+ "Disc isn't chosen (Errorcode: 1)");
	}
	
	public void musicFailedToLoad(FriendlyException exception, Bot bot, Guild guild) {
		bot.getPlayerManager().getController(guild.getId()).getQueue().getCmdChannel().sendMessage("**ERROR:** ``" + exception.getLocalizedMessage() + "``").queue();
//		MainGui.Console.println("[ERROR] in " + guild.getName() + " | failed to load (Errorcode: 2)");
		System.err.println("[ERROR] In " + guild.getName() + " | failed to load (Errorcode: 2)"
				+ "\n" + exception.getLocalizedMessage());
	}
	
	public void logsFolderDidNotCreate() {
//		MainGui.Console.println("[ERROR] Logs Folder couldn't be created (Errorcode: 3)");
		System.err.println("[ERROR] Logs Folder couldn't be created (Errorcode: 3)");
	}
	
	public void couldNotChangeActivity(String activityType, String activityValue) {
//		MainGui.Console.println("[ERROR] Changing activity failed (Errorcode: 4)");
		System.err.println("[ERROR] Changing activity failed [" + activityType + ", " + activityValue + "] (Errorcode: 4)");
	}
}