package de.officeryoda.Commands.Public;

import java.awt.Color;

import de.officeryoda.Bot.Bot;
import de.officeryoda.Commands.Structure.PublicCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class cmdPingPu {

	public static class Ping implements PublicCommand {

		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {

			EmbedBuilder embed = new EmbedBuilder();
			long processingTime;
			long cmdTime = bot.getCmdHandler().getCommandTime();

			embed.setTitle("**pong** :ping_pong:");
			embed.setColor(Color.GREEN);
			embed.setDescription(":hourglass: **Time:** pinging...\r\n"
					+ ":stopwatch: **Code:** processing...");
			embed.setFooter(bot.getEmbedFooterTime(), bot.getProfilePictureUrl());
			processingTime = System.currentTimeMillis() - cmdTime;
			channel.sendMessage(embed.build()).queue(msg -> {
				EmbedBuilder embed2 = new EmbedBuilder();

				embed2.setTitle("**pong** :ping_pong:");
				embed2.setColor(Color.GREEN);
				embed2.setDescription(":hourglass: **Time:** " + (System.currentTimeMillis() - cmdTime) + " ms\r\n"
						+ ":stopwatch: **Code:** " + processingTime + " ms");
				embed2.setFooter(bot.getEmbedFooterTime(), bot.getProfilePictureUrl());
				msg.editMessage(embed2.build()).queue();
				embed2.clear();
			});
		}
	}
}
