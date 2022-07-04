package de.officeryoda.Commands.Public;

import de.officeryoda.Bot.Bot;
import de.officeryoda.Commands.Structure.PublicCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class cmdFun extends ListenerAdapter {

	public static class KopfOderZahl implements PublicCommand {

		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {
			int number = (int) (Math.random() * 2 + 1); // will return either 1 or 2
			switch (number) {
			case 1:
				message.reply(":man: Kopf").queue();
				break;
			case 2:
				message.reply(":1234: Zahl").queue();
				break;
			default:
				message.reply("Was zum Teufel hast du gemacht, dass du diese Nachricht siehst?!").queue();
				break;
			}
		}
	}
}
