package de.officeryoda.Bot;

import java.nio.file.FileSystems;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.security.auth.login.LoginException;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import de.officeryoda.Main;
import de.officeryoda.Commands.Public.cmdAdministration;
import de.officeryoda.Commands.Public.cmdFun;
import de.officeryoda.Commands.Structure.CommandAdder;
import de.officeryoda.Commands.Structure.CommandHandler;
import de.officeryoda.Listener.MessageListener;
import de.officeryoda.Listener.ReadyListener;
import de.officeryoda.Music.PlayerManager;
import de.officeryoda.Utils.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Bot {
	private BotBase botBase;
	private String trueName;
	private String visualName;
	private String TOKEN;
	private String prefix;

	private BotHandler botHandler;
	private FileManager fileHandler;
	private long startTime;

	private JDA jda;
	private boolean running;

	private CommandHandler commandHandler;

	private AudioPlayerManager audioPlayerManager;
	private PlayerManager playerManager;
	
	public Bot(BotBase botBase) {
		this.botBase = botBase;
		this.trueName = botBase.getTrueName();
		this.visualName = botBase.getVisualName();
		this.TOKEN = botBase.getToken();
		this.prefix = botBase.getPrefix();
		
		botHandler = new BotHandler(this);
		String disc = FileSystems.getDefault().getRootDirectories().iterator().next().toString().replace(":\\", ""); //gets the first of the Discs
		fileHandler = new FileManager(this, disc);

		running = false;
	}
	
	public void start() {
		if(running) return;
		startTime = System.currentTimeMillis();
		running = true;
		
		//initialize classes
		this.commandHandler = new CommandHandler(this);
		new CommandAdder(commandHandler);
		audioPlayerManager = new DefaultAudioPlayerManager();
		playerManager = new PlayerManager(this);
		
		//Start Bot
		JDABuilder builder = JDABuilder.createDefault(TOKEN);
		builder.setAutoReconnect(true);
		//	builder.setActivity(Activity.listening((String) config.get("Activity.value", "Discord")));
		builder.setActivity(Activity.playing("new Code"));
		builder.setStatus(OnlineStatus.ONLINE);

		builder.addEventListeners(new ReadyListener(this));
		builder.addEventListeners(new MessageListener(this));
		builder.addEventListeners(new cmdAdministration());
		builder.addEventListeners(new cmdFun());

		builder.setChunkingFilter(ChunkingFilter.ALL);
		builder.setMemberCachePolicy(MemberCachePolicy.ALL);
		builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
		try {
			jda = builder.build();
		} catch (LoginException e) {
			running = false;
			throw new NullPointerException("JDA from Bot " + trueName + " is null [" + TOKEN + "]\n" + e);
		}
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		audioPlayerManager.getConfiguration().setFilterHotSwapEnabled(true);
		
		Config config = fileHandler.getConfig();
		botHandler.setActivity((String) config.get("Activity.type", "playing"), (String) config.get("Activity.value", "Discord"));
		
		Main.INSTANCE.addOnlineBot(this);
		
		System.out.println(trueName + " booted");
	}

	public void stop() {
		if(!running) return;
		jda.shutdown();
		
		Main.INSTANCE.removeOnlineBot(this);
		
		System.out.println(trueName + " stopped");

		running = false;
	}
	
	public BotBase getBotBase() {
		return botBase;
	}
	
	public String getToken() {
		return TOKEN;
	}
	
	public void setToken(String TOKEN) {
		this.TOKEN = TOKEN;
		this.botBase.setToken(TOKEN);
	}
	
	public String getVisualName() {
		return visualName;
	}
	
	public void setVisualName(String visualName) {
		this.fileHandler.changeVisualName(visualName); //muss drüber bleiben
		this.visualName = visualName;
		this.botBase.setVisualName(visualName);
	}
	
	public String getTrueName() {
		return trueName;
	}
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
		this.botBase.setPrefix(prefix);
		if(commandHandler != null)
			this.commandHandler.setPrefix(prefix);
	}

	public BotHandler getBotHandler() {
		return botHandler;
	}
	
	public FileManager getFileManager() {
		return fileHandler;
	}

	public CommandHandler getCmdHandler() {
		return commandHandler;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public JDA getJda() {
		return jda;
	}
	
	public long getStartTime() {
		return startTime;
	}

	public String getEmbedFooterTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd  HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  

		return dtf.format(now);
	}

	public String getProfilePictureUrl() {
		return jda.getSelfUser().getAvatarUrl();
	}
	
	public AudioPlayerManager getAudioPlayerManager() {
		return audioPlayerManager;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}
}
