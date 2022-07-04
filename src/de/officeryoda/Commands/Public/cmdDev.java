package de.officeryoda.Commands.Public;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.vdurmont.emoji.EmojiParser;

import de.officeryoda.Bot.Bot;
import de.officeryoda.Commands.Structure.PublicCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.TextChannel;

public class cmdDev implements PublicCommand {

	@Override
	public void executeCommand(Bot bot, Message message, String[] args, Member member, Guild guild, TextChannel channel) {
		message.addReaction("U+0030 U+20E3").queue();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("end1");
		try {
			System.out.println(message.getReactions());
			message.getReactions();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(MessageReaction reaction : message.getReactions()) {
			ReactionEmote emote = reaction.getReactionEmote();
			System.out.println(emote.getName());
			System.out.println(emote.getId());
			System.out.println(emote.getAsCodepoints());
			System.out.println(emote.getAsReactionCode());
			System.out.println(emote.getEmoji());
			System.out.println(emote.getEmote().getName());
		}
		System.out.println("end2");
	}

	/**
	 * @param message
	 */
	public void extractEmojis(Message message) {
		// Collect emojis
		String content = message.getContentRaw();
		List<String> emojis = EmojiParser.extractEmojis(content);
		List<String> customEmoji = message.getEmotes().stream()
		        .map((emote) -> emote.getName() + ":" + emote.getId())
		        .collect(Collectors.toList());

		// Create merged list
		List<String> merged = new ArrayList<>();
		merged.addAll(emojis);
		merged.addAll(customEmoji);

		// Sort based on index in message to preserve order
		merged.sort(Comparator.comparingInt(content::indexOf));

		for (String emoji : merged) {
		    message.addReaction(emoji).queue();
		}
	}

}
