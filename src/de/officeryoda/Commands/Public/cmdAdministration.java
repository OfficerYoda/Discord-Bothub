package de.officeryoda.Commands.Public;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.HashedMap;

import com.vdurmont.emoji.EmojiParser;

import de.officeryoda.Bot.Bot;
import de.officeryoda.Commands.Structure.PublicCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildMuteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class cmdAdministration extends ListenerAdapter {

	private List<Member> muted;
	private List<String> permissionedIds;

	public static cmdAdministration INSTANCE;

	public cmdAdministration() {
		INSTANCE = this;

		muted = new ArrayList<>();
		permissionedIds = new ArrayList<>();

		permissionedIds.add("564162388946059274");
		permissionedIds.add("433223801195462657");
	}

	@Override
	public void onGuildVoiceGuildMute(GuildVoiceGuildMuteEvent event) {
		if(muted.isEmpty())
			return;
		Member member = event.getMember();
		if(!muted.contains(member))
			return;
		member.mute(true).queue();
	}

	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		if(muted.isEmpty())
			return;
		Member member = event.getMember();
		if(!muted.contains(member))
			return;
		member.mute(true).queue();
	}

	public static class VoiceMute implements PublicCommand {

		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {
			if(!cmdAdministration.INSTANCE.permissionedIds.contains(message.getAuthor().getId())) {
				message.reply("You don't have the permission to do this").queue();
				return;
			}

			List<Member> addList = new ArrayList<>();

			try {
				addList.addAll(message.getMentionedMembers());
			} catch (Exception e) {
				channel.sendMessage("Please @ the persons that you want to get muted.").queue();
				return;
			}
			cmdAdministration.INSTANCE.muted.addAll(addList);

			EmbedBuilder embed = new EmbedBuilder();
			StringBuilder builder = new StringBuilder();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd  HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();

			addList.forEach(m -> {
				if(m.getVoiceState().inVoiceChannel())
					m.mute(true).queue();
				builder.append("-" + m.getEffectiveName() + "\r\n");
			});
			embed.setTitle("Muted: ");
			embed.setDescription(builder.toString());
			embed.setFooter(dtf.format(now), guild.getSelfMember().getUser().getAvatarUrl());

			channel.sendMessage(embed.build()).queue();
		}

	}

	public static class VoiceUnmute implements PublicCommand {

		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {
			if(!cmdAdministration.INSTANCE.permissionedIds.contains(message.getAuthor().getId())) {
				message.reply("You don't have the permission to do this").queue();
				return;
			}

			message.getMentionedMembers().forEach(m -> m.mute(false));

			List<Member> removeList = new ArrayList<>();

			if(cmdAdministration.INSTANCE.muted.isEmpty()) {
				channel.sendMessage("There is currently nobody muted.").queue();
				return;
			}
			try {
				removeList.addAll(message.getMentionedMembers());
			} catch (Exception e) {
				channel.sendMessage("Please @ the persons that you want to get unmuted.").queue();
				return;
			}
			EmbedBuilder embed = new EmbedBuilder();
			StringBuilder builder = new StringBuilder();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd  HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();

			removeList.forEach(m -> builder.append("-" + m.getEffectiveName() + "\r\n"));
			embed.setTitle("Unmuted: ");
			embed.setDescription(builder.toString());
			embed.setFooter(dtf.format(now), guild.getSelfMember().getUser().getAvatarUrl());

			channel.sendMessage(embed.build()).queue();

			cmdAdministration.INSTANCE.muted.removeAll(removeList);
		}

	}

	public static class Ban implements PublicCommand {

		@Override
		public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild guild, TextChannel channel) {
			Member target = null;
			try {
				target = message.getMentionedMembers().get(0);
			} catch (Exception e) {
				channel.sendMessage("Who do you want to ban?").queue();
				return;
			}
			//			guild.ban(member, 0).queue();
			//			channel.sendMessage("Banned " + member.getEffectiveName() + ".").queue();

			try {
				if (!author.canInteract(target) || !author.hasPermission(Permission.BAN_MEMBERS)) {
					channel.sendMessage("You don't have the required permission to ban this member!").queue();
					return;
				}

				final Member selfMember = guild.getSelfMember();

				if (!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.BAN_MEMBERS)) {
					channel.sendMessage("I don't have the required permission to ban this member!").queue();
					return;
				}

				guild.ban(target, 0, "Banned by Bot (" + author.getEffectiveName() + " used" + bot.getPrefix() + "ban)").queue();
				channel.sendMessage("User has been banned successfully.").queue();
			} catch(Exception ex) {
				channel.sendMessage("The user is not in the server, Or Invalid ID").queue();
			}
		}
	}

	public static class Poll implements PublicCommand {

		private Map<Integer, String> reactEmotes;
		private Map<Integer, String> emoteIds;

		String zero = ":zero:";
		String one = ":one:";
		String two = ":two:";
		String three = ":three:";
		String four = ":four:";
		String five = ":five:";
		String six = ":six:";
		String seven = ":seven:";
		String eight = ":eight:";
		String nine = ":nine:";

		public Poll() {
			reactEmotes = new HashedMap<>();
			reactEmotes.put(0, zero);
			reactEmotes.put(1, one);
			reactEmotes.put(2, two);
			reactEmotes.put(3, three);
			reactEmotes.put(4, four);
			reactEmotes.put(5, five);
			reactEmotes.put(6, six);
			reactEmotes.put(7, seven);
			reactEmotes.put(8, eight);
			reactEmotes.put(9, nine);

			emoteIds = new HashedMap<>();
			emoteIds.put(0, "U+0030 U+20E3");
			emoteIds.put(1, "U+0031 U+20E3");
			emoteIds.put(2, "U+0032 U+20E3");
			emoteIds.put(3, "U+0033 U+20E3");
			emoteIds.put(4, "U+0034 U+20E3");
			emoteIds.put(5, "U+0035 U+20E3");
			emoteIds.put(6, "U+0036 U+20E3");
			emoteIds.put(7, "U+0037 U+20E3");
			emoteIds.put(8, "U+0038 U+20E3");
			emoteIds.put(9, "U+0039 U+20E3");
		}

		@Override
		public void executeCommand(Bot bot, Message message, String[] baseArgs, Member author, Guild guild, TextChannel channel) {
			Map<String, StringBuilder> groupBuilder = new HashedMap<>();

			List<String> reactions = new ArrayList<>();
			List<Integer> availableReactions = new ArrayList<>();
			for (int i = 0; i < 10; i++)
				availableReactions.add(i);

			String[] args = message.getContentRaw().split(",");
			args[0] = args[0].replace(".poll ", "");
			for (int i = 0; i < args.length; i++) {
				if(args[i].endsWith("/"))
					args = fuse(args, i);
			}

			if(!message.getContentRaw().contains(",")) {
				channel.sendMessage("``Example:`` _.poll What's 1+2?, 1, 2, 3_").queue();
				return;
			}

			EmbedBuilder embed = new EmbedBuilder();
			StringBuilder builder = new StringBuilder();

			embed.setTitle(args[0]);

			//goes through all possible answers
			for(int i = 1; i < args.length; i++) {
				String reactEmote = "";
				String group = "";
				String rawArg = args[i];
				String[] specArgs = null;
				String specArgsStr = "";

				//continues if thers no text in argument
				if (rawArg.replace(" ", "").equals("")) continue;

				//gets specArgs if containing and checks if bracket is closed
				if (rawArg.contains("{")) {
					try {
						specArgsStr = rawArg.substring(rawArg.indexOf("{") + 1, rawArg.indexOf("}"));
						specArgs = specArgsStr.split(";");
					} catch (Exception e) {
						channel.sendMessage(bracketNotClosed(rawArg.substring(rawArg.indexOf("{"), rawArg.length()))).queue();
						return;
					}
				}

				//executes specArgs
				if(specArgs != null) {
					for (String arg : specArgs) {
						if(arg.replace(" ", "").toLowerCase().startsWith("icon="))
							reactEmote = getEmote(message, arg);
						if(arg.replace(" ", "").toLowerCase().startsWith("group="))
							group = arg.replace("group=", "");
					}
				}

				//reacts
				if(reactions.contains(reactEmote)) {
					int j = availableReactions.remove(0);
					reactEmote = "!" + reactEmotes.get(j);
					reactions.add(emoteIds.get(j));
				}

				if(reactEmote.equals("")) {
					if(availableReactions.isEmpty()) continue;
					int j = availableReactions.remove(0);
					reactEmote = reactEmotes.get(j);
					reactions.add(emoteIds.get(j));
				} else if(!reactEmote.startsWith("!")) {
					reactions.add(reactEmote);
				}

				//manage group
				if(group.equals("")) {
					builder.append(reactEmote + ": " + args[i].replace("{" + specArgsStr + "}", "") + "\r\n");
				} else {
					if(groupBuilder.get(group) == null)
						groupBuilder.put(group, new StringBuilder());
					if(reactEmote.startsWith("!"))
						reactEmote = reactEmote.replace("!", "");
					groupBuilder.get(group).append(reactEmote + ": " + args[i].replace("{" + specArgsStr + "}", "") + "\r\n");
				}
			}

			embed.setDescription(builder.toString());

			for(String builderId : groupBuilder.keySet())
				embed.addField(builderId, groupBuilder.get(builderId).toString(), false);
			embed.setFooter(bot.getEmbedFooterTime(), bot.getProfilePictureUrl());

			channel.sendMessage(embed.build()).queue(msg -> reactions.forEach(r -> msg.addReaction(r).queue()));
		}

		/**
		 * @param input
		 * @param index the index of the string which will fuse with the next one
		 * @return
		 */
		public String[] fuse(String[] input, int index) {
			String[] args = new String[input.length - 1];

			for (int i = 0; i < args.length; i++) {
				if(i < index) {
					args[i] = input[i];
				} if(i == index) {
					args[i] = (input[i] + "," + input[i + 1]).replace("/", "");
				} else {
					args[i] = input[i + 1];
				}
			}

			return args;
		}

		private MessageEmbed bracketNotClosed(String errorLine) {
			EmbedBuilder embed = new EmbedBuilder()
					.setColor(Color.red)
					.setDescription("Argument Error: bracket isn't closed" + " --> ...__**" + errorLine + "**__ <--");
			return embed.build();
		}

		private String getEmote(Message message, String input) {
			// Collect emojis
			List<String> emojis = EmojiParser.extractEmojis(input);
			List<String> customEmoji = message.getEmotes().stream()
					.map((emote) -> emote.getName() + ":" + emote.getId())
					.collect(Collectors.toList());

			// Create merged list
			List<String> merged = new ArrayList<>();
			merged.addAll(emojis);
			merged.addAll(customEmoji);

			// Sort based on index in message to preserve order
			merged.sort(Comparator.comparingInt(input::indexOf));

			return merged.isEmpty() ? "" : merged.get(0);
		}
	}
}
