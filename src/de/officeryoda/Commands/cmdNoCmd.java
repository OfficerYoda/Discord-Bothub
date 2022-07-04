package de.officeryoda.Commands;

import de.officeryoda.Bot.Bot;
import de.officeryoda.Commands.Structure.PrivateCommand;
import de.officeryoda.Commands.Structure.PublicCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class cmdNoCmd {
	
	public static class publicNoCmd implements PublicCommand{
		
		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member member, Guild guild, TextChannel channel) {
			channel.sendMessage("This command wasn't assigned to any code.").queue();
		}
	}
	
	public static class privateNoCmd implements PrivateCommand {
		
		@Override
		public void executeCommand(Bot bot, User user, PrivateChannel channel, Message message, String[] args) {
			channel.sendMessage("This command wasn't assigned to any code.").queue();
		}
	}
}
