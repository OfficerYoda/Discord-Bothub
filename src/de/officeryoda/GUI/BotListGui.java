package de.officeryoda.GUI;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import de.officeryoda.Main;
import de.officeryoda.Bot.Bot;
import de.officeryoda.Utils.Config;
import de.officeryoda.WordleFinder.WordleFinder;

public class BotListGui {
	public static BotListGui INSTANCE;

	final Main main;
	Map<Group, Bot> bots;

	protected Shell shell;

	public BotListGui(Main main) {
		INSTANCE = this;

		this.main = main;
		this.bots = new HashMap<>();
	}

	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				System.exit(69);
			}
		});
		shell.setImage(SWTResourceManager.getImage(BotListGui.class, "/de/officeryoda/GUI/Images/Discord Bots.png"));
		shell.setBackground(new Color(42, 42, 42));
		shell.setSize(450, 300);
		shell.setMinimumSize(450, 300);
		shell.setText("Discord-Bothub");
		shell.setLayout(new GridLayout(2, false));

		Button btnAddBot = new Button(shell, SWT.NONE);
		btnAddBot.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		btnAddBot.setBackground(new Color(42, 42, 42));
		btnAddBot.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				BotAddGui botAddGui = new BotAddGui(shell, 0, null);
				botAddGui.open();
			}
		});
		GridData gd_btnAddBot = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_btnAddBot.widthHint = 222;
		btnAddBot.setLayoutData(gd_btnAddBot);
		btnAddBot.setText("Add Bot");

		Button btnWordleFinder = new Button(shell, SWT.NONE);
		btnWordleFinder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				new WordleFinder().start();
			}
		});
		btnWordleFinder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnWordleFinder.setText("Wordle Finder");
		btnWordleFinder.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		btnWordleFinder.setBackground(SWTResourceManager.getColor(42, 42, 42));

		for(Bot bot : main.getBots()) {
			String botname = bot.getVisualName();
			createGroup(botname, bot.getTrueName().toLowerCase().equals("bane") ? true : false);
		}
		//		createGroup("Boba", false);
		//		createGroup("Bane", true);
	}

	public void createGroup(String groupName, boolean baseBot) {
		Group group = new Group(shell, SWT.NONE);
		group.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		group.setLayout(new FormLayout());
		GridData gd_group = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd_group.heightHint = 28;
		group.setLayoutData(gd_group);
		group.setBackground(new Color(50, 50, 50));
		group.setText(groupName);

		Bot bot = main.getBotByVisualName(group.getText());
		bots.put(group, bot);
		Config config = bot.getFileManager().getConfig();

		Button btnStartStopBot = new Button(group, SWT.BORDER);
		btnStartStopBot.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (bot.isRunning()) {
					btnStartStopBot.setBackground(new Color(0, 0, 0));
					btnStartStopBot.setEnabled(false);
					btnStartStopBot.setText("stopping bot");
					bot.stop();
					btnStartStopBot.setEnabled(true);

					btnStartStopBot.setText("Start Bot");
					btnStartStopBot.setBackground(new Color(7, 209, 0)); //green
				} else {
					btnStartStopBot.setBackground(new Color(0, 0, 0));
					btnStartStopBot.setEnabled(false);
					btnStartStopBot.setText("starting bot");
					bot.start();
					btnStartStopBot.setEnabled(true);

					btnStartStopBot.setText("Stop Bot");
					btnStartStopBot.setBackground(new Color(209, 27, 27)); //red
				}
			}
		});
		FormData fd_btnStartStopBot = new FormData();
		fd_btnStartStopBot.right = new FormAttachment(100);
		fd_btnStartStopBot.left = new FormAttachment(100, -78);
		fd_btnStartStopBot.bottom = new FormAttachment(0, 24);
		fd_btnStartStopBot.top = new FormAttachment(0, -5);
		btnStartStopBot.setLayoutData(fd_btnStartStopBot);
		btnStartStopBot.setBackground(new Color(7, 209, 0)); //209, 27, 27
		btnStartStopBot.setText("Start Bot");

		Combo comboActivity = new Combo(group, SWT.READ_ONLY);
		comboActivity.setItems(new String[] {"competing", "playing", "listening", "watching"});
		comboActivity.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		comboActivity.setBackground(new Color(58, 58, 58));
		FormData fd_comboActivity = new FormData();
		fd_comboActivity.bottom = new FormAttachment(100, -23);
		fd_comboActivity.top = new FormAttachment(0);
		comboActivity.setLayoutData(fd_comboActivity);
		comboActivity.setText((String) config.get("Activity.type", "listening"));

		Text txtActivity = new Text(group, SWT.BORDER);
		fd_comboActivity.right = new FormAttachment(txtActivity, -6);
		txtActivity.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (bot.isRunning() && !txtActivity.getText().isBlank())
					bot.getBotHandler().setActivity(comboActivity.getText(), txtActivity.getText());
				config.put("Activity.type", comboActivity.getText());
				config.put("Activity.value", txtActivity.getText());
			}
		});
		txtActivity.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 13) {
					shell.setFocus();
					if (bot.isRunning() && !txtActivity.getText().isBlank())
						bot.getBotHandler().setActivity(comboActivity.getText(), txtActivity.getText());
					config.put("Activity.type", comboActivity.getText());
					config.put("Activity.value", txtActivity.getText());
				}
			}
		});
		txtActivity.setText((String) config.get("Activity.value", "commands"));
		txtActivity.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		txtActivity.setBackground(SWTResourceManager.getColor(58, 58, 58));
		FormData fd_txtActivity = new FormData();
		fd_txtActivity.left = new FormAttachment(0, 233);
		fd_txtActivity.right = new FormAttachment(btnStartStopBot, -6);
		fd_txtActivity.top = new FormAttachment(0);
		fd_txtActivity.bottom = new FormAttachment(100, -7);
		txtActivity.setLayoutData(fd_txtActivity);

		Text txtPrefix = new Text(group, SWT.BORDER);
		fd_comboActivity.left = new FormAttachment(0, 137);
		txtPrefix.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				bot.setPrefix(txtPrefix.getText());
			}
		});
		FormData fd_txtPrefix = new FormData();
		fd_txtPrefix.right = new FormAttachment(0, 131);
		fd_txtPrefix.left = new FormAttachment(0, 57);
		fd_txtPrefix.bottom = new FormAttachment(100, -7);
		fd_txtPrefix.top = new FormAttachment(0);
		txtPrefix.setLayoutData(fd_txtPrefix);
		txtPrefix.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		txtPrefix.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				//keyCode 13 == Key.Enter;
				if(e.keyCode == 13)
					group.setFocus();
				else
					bot.setPrefix(txtPrefix.getText());
			}
		});
		txtPrefix.setText(bot.getPrefix());
		txtPrefix.setBackground(SWTResourceManager.getColor(58, 58, 58));

		if(!baseBot) {
			Button btnEdit = new Button(group, SWT.NONE);
			btnEdit.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					BotAddGui botAddGui = new BotAddGui(shell, 0, bot);
					try {
						Bot editBot;
						if((editBot = (Bot) botAddGui.open()) != null) {
							//EDIT
							//botname
							if(!bot.getVisualName().equals(editBot.getVisualName())) {
								bot.setVisualName(editBot.getVisualName());
								group.setText(editBot.getVisualName());
							}
							//token
							if(!bot.getToken().equals(editBot.getToken())) {
								bot.stop();
								btnStartStopBot.setBackground(new Color(7, 209, 0));
								btnStartStopBot.setText("Start Bot");
								bot.setToken(editBot.getToken());
							}
							//prefix
							if(!bot.getPrefix().equals(editBot.getPrefix())) {
								bot.setPrefix(editBot.getPrefix());
								txtPrefix.setText(editBot.getPrefix());
							}
						} else {
							//DELETE
							bot.stop();
							bot.getFileManager().deleteDirectory();
							Main main = Main.INSTANCE;
							main.removeBot(bot);
							main.getBotSaves().put(bot.getTrueName(), null);

							
							group.dispose();
							shell.layout();
						}
					} catch (Exception ex) {
						System.err.println("Bot edit error:\n" + ex);
					}
				}
			});
			btnEdit.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
			fd_txtPrefix.left = new FormAttachment(btnEdit, 3);
			btnEdit.setText("edit");
			btnEdit.setBackground(SWTResourceManager.getColor(58, 58, 58));
			FormData fd_btnEdit = new FormData();
			fd_btnEdit.left = new FormAttachment(0);
			fd_btnEdit.right = new FormAttachment(0, 42);
			btnEdit.setLayoutData(fd_btnEdit);
		}

		shell.layout(); // to "update" the shell
	}
}
