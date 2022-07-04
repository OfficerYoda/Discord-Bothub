package de.officeryoda.GUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import de.officeryoda.Main;
import de.officeryoda.Bot.Bot;
import de.officeryoda.Bot.BotBase;
import de.officeryoda.Bot.BotCreater;

public class BotAddGui extends Dialog {

	private Bot bot;
	private boolean remove;

	protected Object result;
	protected Shell shell;
	private Label lblBotname;
	private Text txtBotname;
	private Label lblToken;
	private Text txtToken;
	private Text txtPrefix;
	private Button btnDeleteBot;
	private Label lblDoubleClick;
	private Label lblOutput;
	private Button btnAddBot;
	private Button btnCancel;
	private Label lblPrefix;
	private Button btnTutorialLink;
	private Label lblTutorial;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public BotAddGui(Shell parent, int style, Bot bot) {
		super(parent, style);
		this.bot = bot;
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		try {
			createContents();
		} catch (IOException e) {}
		setLocation();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	private void setLocation() {
		/** get the size of the parent window/shell */
		Rectangle bounds = getParent().getBounds();

		/** get the size of the window/shell */
		Rectangle rect = shell.getBounds();

		/** calculate the centre */
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;

		/** set the new location */
		shell.setLocation(x, y);
	}

	/**
	 * Create contents of the dialog.
	 * @throws IOException 
	 */
	private void createContents() throws IOException {
		shell = new Shell();
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if(bot != null)
					result = new Bot(new BotBase(bot.getTrueName(), txtBotname.getText(), txtToken.getText(), txtPrefix.getText()));
				if(remove)
					result = null;
			}
		});
		shell.setText("Add Discord Bot");
		shell.setImage(SWTResourceManager.getImage(BotAddGui.class, "/de/officeryoda/GUI/Images/Discord Bots.png"));
		shell.setMinimumSize(new Point(443, 260));
		shell.setMaximumSize(new Point(443, 260));
		shell.setModified(true);
		shell.setBackground(SWTResourceManager.getColor(42, 42, 42));
		shell.setSize(443, 260);

		if(bot != null) {
			btnCancel = new Button(shell, SWT.NONE);
			btnCancel.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
			btnCancel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
			btnCancel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					shell.dispose();
				}
			});
			btnCancel.setBounds(355, 193, 69, 23);
			btnCancel.setBackground(SWTResourceManager.getColor(50, 50, 50));
			btnCancel.setText("Cancel");
		}

		lblBotname = new Label(shell, SWT.NONE);
		lblBotname.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		lblBotname.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
		lblBotname.setBackground(SWTResourceManager.getColor(42, 42, 42));
		lblBotname.setToolTipText("The name which will\r\nbe shown in the Botlist\r\nand under which files\r\nwill be saved.");
		lblBotname.setBounds(10, 10, 95, 31);
		lblBotname.setText("Botname");

		txtBotname = new Text(shell, SWT.BORDER);
		if(bot != null)
			txtBotname.setText(bot.getVisualName());
		txtBotname.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		txtBotname.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.NORMAL));
		txtBotname.setBackground(SWTResourceManager.getColor(50, 50, 50));
		txtBotname.setToolTipText("The name which will\r\nbe shown in the Botlist\r\nand under which files\r\nwill be saved.");
		txtBotname.setBounds(104, 10, 320, 31);

		lblToken = new Label(shell, SWT.NONE);
		lblToken.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		lblToken.setBackground(SWTResourceManager.getColor(42, 42, 42));
		lblToken.setToolTipText("The Token of the Bot.");
		lblToken.setText("Token");
		lblToken.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
		lblToken.setBounds(10, 51, 95, 31);

		txtToken = new Text(shell, SWT.BORDER);
		if(bot != null)
			txtToken.setText(bot.getToken());
		txtToken.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.NORMAL));
		txtToken.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		txtToken.setBackground(SWTResourceManager.getColor(50, 50, 50));
		txtToken.setToolTipText("The Token of the Bot.");
		txtToken.setBounds(104, 47, 320, 31);

		btnAddBot = new Button(shell, SWT.NONE);
		btnAddBot.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent event) {
				addBot();
			}
		});
		btnAddBot.setFont(SWTResourceManager.getFont("Segoe UI", 20, SWT.BOLD));
		btnAddBot.setText(bot != null ? "finish edit" : "Add Bot");
		btnAddBot.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		btnAddBot.setBackground(SWTResourceManager.getColor(50, 50, 50));
		btnAddBot.setBounds(280, 140, 144, 49);

		lblPrefix = new Label(shell, SWT.NONE);
		lblPrefix.setToolTipText("The letter(-s)/symbol(-s) that are infront of every command");
		lblPrefix.setText("Prefix");
		lblPrefix.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		lblPrefix.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
		lblPrefix.setBackground(SWTResourceManager.getColor(42, 42, 42));
		lblPrefix.setBounds(10, 84, 95, 31);

		txtPrefix = new Text(shell, SWT.BORDER);
		if(bot != null)
			txtPrefix.setText(bot.getPrefix());
		txtPrefix.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 13)
					addBot();
			}
		});
		txtPrefix.setToolTipText("The letter(-s)/symbol(-s) that are infront of every command");
		txtPrefix.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		txtPrefix.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.NORMAL));
		txtPrefix.setBackground(SWTResourceManager.getColor(50, 50, 50));
		txtPrefix.setBounds(104, 84, 320, 31);

		btnTutorialLink = new Button(shell, SWT.NONE);
		btnTutorialLink.setBounds(151, 140, 123, 49);
		btnTutorialLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				Program.launch("https://youtu.be/22Q_O2WQvkI");
			}
		});
		btnTutorialLink.setText("link");
		btnTutorialLink.setForeground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
		btnTutorialLink.setFont(SWTResourceManager.getFont("Segoe UI", 24, SWT.NORMAL));
		btnTutorialLink.setBackground(SWTResourceManager.getColor(50, 50, 50));

		if(bot != null) {
			btnDeleteBot = new Button(shell, SWT.NONE);
			btnDeleteBot.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					remove = true;
					shell.dispose();
				}
			});
			btnDeleteBot.setText("Delete Bot");
			btnDeleteBot.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
			btnDeleteBot.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
			btnDeleteBot.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
			btnDeleteBot.setBounds(10, 185, 95, 31);

			lblDoubleClick = new Label(shell, SWT.NONE);
			lblDoubleClick.setBackground(SWTResourceManager.getColor(42, 42, 42));
			lblDoubleClick.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
			lblDoubleClick.setBounds(106, 201, 95, 15);
			lblDoubleClick.setText("<- double click");
		}

		lblTutorial = new Label(shell, SWT.NONE);
		lblTutorial.setToolTipText("The letter(-s)/symbol(-s) that are infront of every command");
		lblTutorial.setText("Tutorial:");
		lblTutorial.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		lblTutorial.setFont(SWTResourceManager.getFont("Segoe UI", 24, SWT.BOLD));
		lblTutorial.setBackground(SWTResourceManager.getColor(42, 42, 42));
		lblTutorial.setBounds(10, 142, 135, 56);

		lblOutput = new Label(shell, SWT.RIGHT);
		lblOutput.setBackground(SWTResourceManager.getColor(42, 42, 42));
		lblOutput.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		lblOutput.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		lblOutput.setBounds(104, 193, 245, 23);
		shell.setTabList(new Control[]{txtBotname, txtToken, txtPrefix, btnTutorialLink, btnAddBot});

	}

	public void addBot() {
		//Botname check
		String botname = txtBotname.getText();

		if(botname.equals("")) {
			setOutput("Botname missing", 1500);
			return;
		}

		//invalid characters check
		List<String> invalidChars = new ArrayList<>();
		invalidChars.add("\\");
		invalidChars.add("/");
		invalidChars.add(":");
		invalidChars.add("*");
		invalidChars.add("?");
		invalidChars.add("\"");
		invalidChars.add("<");
		invalidChars.add(">");
		invalidChars.add("|");
		for (String string : invalidChars) {
			if(botname.contains(string)) {
				setOutput("Invalid charachter: " + string, 1500);
				return;
			}
		}

		//name already existing check
		for(Bot bot : Main.INSTANCE.getBots()) {
			if(bot.getVisualName().toLowerCase().equals(botname)) {
				setOutput("A Bot with this name already exists", 1500);
				return;
			}
		}

		//Token check
		String token = txtToken.getText();
		if(!(token.substring(24, 25).equals(".") && token.substring(31, 32).equals("."))) {
			setOutput("Invalid Token", 1500);
			return;
		}

		//Prefix chekc
		if(txtPrefix.getText().equals("")) {
			setOutput("Prefix missing", 1500);
			return;
		}

		if(bot == null) {
			BotCreater.INSTANCE.createBot(botname, botname, token, txtPrefix.getText());
			BotListGui.INSTANCE.createGroup(botname, false);
		}

		shell.dispose();
	}

	//ouput delay
	private void setOutput(String output, int time) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				lblOutput.setText(output);
				delayOutput(time, output);
				return;
			}
		});
	}

	private void delayOutput(int milliseconds, String output) {
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(milliseconds);
					resetOutput(output);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
		}.start();
	}

	private void resetOutput(String output) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if(lblOutput.getText().equals(output))
					lblOutput.setText("");
			}
		});
	}
}
