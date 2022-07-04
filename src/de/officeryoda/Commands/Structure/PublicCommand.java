package de.officeryoda.Commands.Structure;

import de.officeryoda.Bot.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public interface PublicCommand extends BotCommand {
	
	public abstract void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel);
	
}
