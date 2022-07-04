package de.officeryoda.Commands.Structure;

import de.officeryoda.Commands.Private.cmdPingPr;
import de.officeryoda.Commands.Private.cmdPlay;
import de.officeryoda.Commands.Public.cmdAdministration;
import de.officeryoda.Commands.Public.cmdDev;
import de.officeryoda.Commands.Public.cmdFun;
import de.officeryoda.Commands.Public.cmdJoinLeave;
import de.officeryoda.Commands.Public.cmdMissionGetRights;
import de.officeryoda.Commands.Public.cmdMusic;
import de.officeryoda.Commands.Public.cmdPingPu;
import de.officeryoda.Commands.Public.cmdQueue;
import de.officeryoda.Commands.Public.cmdStats;

public class CommandAdder {
	
	private CommandHandler cmdHandler;
	
	public CommandAdder(CommandHandler cmdHandler) {
		this.cmdHandler = cmdHandler;
		
		addPublicCommands();
		addPrivateCommands();
	}
	
	private void addPublicCommands() {
		cmdHandler.addCommand("ping", new cmdPingPu.Ping(), false);
		//Join/Leave
		cmdHandler.addCommand("join", new cmdJoinLeave.Join(), false);
		cmdHandler.addCommand("leave", new cmdJoinLeave.Leave(), false);
		cmdHandler.addCommand("autoLeave", new cmdJoinLeave.AutoLeave(), false);
		//Music(direct impact)
		cmdHandler.addCommand("play", new cmdMusic.Play(), false);
		cmdHandler.addCommand("stop", new cmdMusic.Stop(), false);
		cmdHandler.addCommand("pause", new cmdMusic.Pause(), false);
		cmdHandler.addCommand("resume", new cmdMusic.Resume(), false);
		cmdHandler.addCommand("volume", new cmdMusic.Volume(), false);
		cmdHandler.addCommand("setTime", new cmdMusic.SetTime(), false);
		//Queue & trackInfo
		cmdHandler.addCommand("trackInfo", new cmdQueue.TrackInfo(), false);
		cmdHandler.addCommand("shuffle", new cmdQueue.Shuffle(), false);
		cmdHandler.addCommand("skip", new cmdQueue.Skip(), false);
		cmdHandler.addCommand("queue", new cmdQueue.Queue(), false);
		cmdHandler.addCommand("loop", new cmdQueue.Loop(), false, "repeat");
		cmdHandler.addCommand("trackInfo", new cmdQueue.TrackInfo(), false);
		//Administration
		cmdHandler.addCommand("ban", new cmdAdministration.Ban(), false);
		cmdHandler.addCommand("mute", new cmdAdministration.VoiceMute(), false);
		cmdHandler.addCommand("unmute", new cmdAdministration.VoiceUnmute(), false);
		cmdHandler.addCommand("poll", new cmdAdministration.Poll(), false);
		//Fun
		cmdHandler.addCommand("kopfOderZahl", new cmdFun.KopfOderZahl(), false, "koz");
		cmdHandler.addCommand("stats", new cmdStats.Stats(), false);
		//Development
		cmdHandler.addCommand("development", new cmdDev(), false, "d", "dev");
		
		//BotMaster
		cmdHandler.addCommand("getPermissions", new cmdMissionGetRights(), false, "gP", "getPrms");
	}
	
	private void addPrivateCommands() {
		boolean prCmd = true;
		cmdHandler.addCommand("ping", new cmdPingPr.Ping(), prCmd);
		cmdHandler.addCommand("play", new cmdPlay.Play(), prCmd, "p");
	}
}
