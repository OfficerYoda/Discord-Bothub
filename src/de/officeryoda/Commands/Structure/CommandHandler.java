package de.officeryoda.Commands.Structure;

import java.util.ArrayList;
import java.util.List;

import de.officeryoda.Bot.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class CommandHandler {

	private Bot bot;
	private String prefix;
	private long commandTime;

	private List<CommandItem> commands;

	public CommandHandler(Bot bot) {
		this.bot = bot;
		this.commands = new ArrayList<>();
		this.prefix = bot.getPrefix();
	}

	public void executePublicCommand(Message message, String[] args, TextChannel channel, Member member, Guild guild) {
		if(!args[0].startsWith(prefix)) return;

		String executedCmd = args[0].toLowerCase().replaceFirst(prefix, "");

		for(CommandItem item : commands) {
			if(item.isPrivateCommand()) continue;

			boolean validCmd = false;
			if(item.getCmdName().equals(executedCmd)) validCmd = true;
			for(String alias : item.getCmdAliases())
				if(alias.equals(executedCmd))
					validCmd = true;
			if(!validCmd) continue;

			((PublicCommand) item.getCmdClass()).executeCommand(bot, message, args, member, guild, channel);
		}
	}

	public void executePrivateCommand(User user, PrivateChannel channel, Message message, String[] args) {
		if(!args[0].startsWith(prefix)) return;

		String executedCmd = args[0].toLowerCase().replace(prefix, "");
		for(CommandItem item : commands) {
			if(!item.isPrivateCommand()) continue;

			boolean validCmd = false;
			if(item.getCmdName().equals(executedCmd)) validCmd = true;
			for(String alias : item.getCmdAliases())
				if(alias.equals(executedCmd))
					validCmd = true;
			if(!validCmd) continue;

			((PrivateCommand) item.getCmdClass()).executeCommand(bot, user, channel, message, args);
		}
	}

	public Bot getBot() {
		return bot;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public long getCommandTime() {
		return commandTime;
	}

	public void setCommandTime(long commandTime) {
		this.commandTime = commandTime;
	}

	public CommandItem getCommand(String cmdName) {
		for(CommandItem item : commands) {
			if(item.getCmdName().equals(cmdName.toLowerCase())) return item;
			for(String alias : item.getCmdAliases())
				if(alias.equals(cmdName.toLowerCase())) return item;
		}
		return null;
	}

	public void addCommand(String cmdName, BotCommand cmdClass, boolean privateCommand, String... aliases) {
		commands.add(new CommandItem(cmdName, aliases, cmdClass, privateCommand));
	}
}
