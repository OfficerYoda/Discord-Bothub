package de.officeryoda.Commands.Structure;

import de.officeryoda.Commands.cmdNoCmd;

public class CommandItem {
	
	private String cmdName;
	private String[] aliases;
	private BotCommand cmdClass;
	private boolean privateCommand;
	
	public CommandItem(String cmdName, String[] aliases, BotCommand cmdClass, boolean privateCommand) {
		this.cmdName = cmdName != null ? cmdName.toLowerCase() : "command";
		this.aliases = aliases != null ? getLowerCaseArgs(aliases) : new String[0];
		this.cmdClass = cmdClass != null ? cmdClass : (privateCommand ? new cmdNoCmd.privateNoCmd() : new cmdNoCmd.publicNoCmd());
		this.privateCommand = privateCommand;
	}

	public String getCmdName() {
		return cmdName;
	}

	public String[] getCmdAliases() {
		return aliases;
	}

	public BotCommand getCmdClass() {
		return cmdClass;
	}
	
	public boolean isPrivateCommand() {
		return privateCommand;
	}

	private String[] getLowerCaseArgs(String[] aliases) {
		String[] args = new String[aliases.length];
		
		for (int i = 0; i < aliases.length; i++)
			args[i] = aliases[i].toLowerCase();
		
		return args;
	}
}
