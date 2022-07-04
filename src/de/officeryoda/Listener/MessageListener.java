package de.officeryoda.Listener;

import de.officeryoda.Bot.Bot;
import de.officeryoda.Commands.Structure.CommandHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
	String prefix;
	CommandHandler cmdHandler;
	
	public MessageListener(Bot bot) {
		cmdHandler = bot.getCmdHandler();
		prefix = bot.getPrefix();
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		cmdHandler.setCommandTime(System.currentTimeMillis());
		if(event.getAuthor().isBot()) return;

		Guild guild = event.getGuild();
		Member member = event.getMember();
		TextChannel channel = event.getChannel();
		Message message = event.getMessage();
		String[] args = message.getContentDisplay().split(" ");
		
		cmdHandler.executePublicCommand(message, args, channel, member, guild);
	}
	
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		cmdHandler.setCommandTime(System.currentTimeMillis());
		if(event.getAuthor().isBot()) return;
		
		User user = event.getAuthor();
		PrivateChannel channel = event.getChannel();
		Message message = event.getMessage();
		String[] args = message.getContentDisplay().split(" ");
//		if(!args[0].startsWith(prefix)) return;
		
		cmdHandler.executePrivateCommand(user, channel, message, args);
	}
}
