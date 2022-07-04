package de.officeryoda.Utils.stats;

import net.dv8tion.jda.api.entities.Member;

public enum StatsType {
	
	//MUSIC
	MUSIC_TIME("Music.time"),
	MUSIC_SONGS("Music.songs"),
	
	//COMMANDS
	COMMANDS_USES("Commands.id.uses"),
	COMMANDS_SPECIFIC_USES("Commands.id.cmdUses"),
	COMMANDS_NAME("Commands.id.name"),
	
	//GAME
	//every User
	GAME_TOTAL_STARTED("Game.total.started"),
	GAME_TOTAL_LEVEL("Game.total.level"),
	GAME_TOTAL_MOVES("Game.total.moves"),
	//per User
	GAME_STARTED("Game.id.started"),
	GAME_LEVEL("Game.id.level"),
	GAME_MOVES("Game.id.moves");
	
	String path;
	
	private StatsType(String path) {
		this.path = path;	
	}
	
	public String getPath() {
		return path;
	}
	
	public String getPath(Member member) {
		return path.replace("id", member.getId());
	}
}
