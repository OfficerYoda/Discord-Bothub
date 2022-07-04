package de.officeryoda.Commands.Structure;

import de.officeryoda.Bot.Bot;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;

public interface PrivateCommand extends BotCommand {

	public abstract void executeCommand(Bot bot, User user, PrivateChannel channel, Message message, String[] args);
	
}
