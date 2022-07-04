package de.officeryoda.WordleFinder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class WordleInput {

	WordleFinder wordleFinder;
	private boolean editing = false;

	protected Shell shlWorldeWordFinder;
	private Text txtUsed;
	private Text txtUnused;
	private Text txtLetter1;
	private Text txtLetter2;
	private Text txtLetter3;
	private Text txtLetter4;
	private Text txtLetter5;
	private Label lblKnownLetters;
	private Text txtWords;
	private Label lblWords;

	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open(WordleFinder wordleFinder) {
		this.wordleFinder = wordleFinder;
		Display display = Display.getDefault();
		createContents();
		shlWorldeWordFinder.open();
		shlWorldeWordFinder.layout();
		while (!shlWorldeWordFinder.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlWorldeWordFinder = new Shell();
		shlWorldeWordFinder.setBackground(SWTResourceManager.getColor(42, 42, 42));
		shlWorldeWordFinder.setImage(SWTResourceManager.getImage(WordleInput.class, "/de/officeryoda/GUI/Images/Wordle.png"));
		shlWorldeWordFinder.setSize(450, 300);
		shlWorldeWordFinder.setMinimumSize(450, 300);
		shlWorldeWordFinder.setMaximumSize(450, 300);
//				shlWorldeWordFinder.setSize(650, 300);
//				shlWorldeWordFinder.setMinimumSize(650, 300);
//				shlWorldeWordFinder.setMaximumSize(650, 300);
		shlWorldeWordFinder.setText("Worlde word finder");

		Button btnFind = new Button(shlWorldeWordFinder, SWT.NONE);
		btnFind.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnFind.setBackground(SWTResourceManager.getColor(50, 50, 50));
		btnFind.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		btnFind.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				find();
			}
		});
		btnFind.setFont(SWTResourceManager.getFont("Segoe UI", 21, SWT.BOLD));
		btnFind.setBounds(10, 206, 415, 45);
		btnFind.setText("Find");

		txtUsed = new Text(shlWorldeWordFinder, SWT.BORDER);
		txtUsed.setBackground(SWTResourceManager.getColor(50, 50, 50));
		txtUsed.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		txtUsed.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		txtUsed.setBounds(154, 27, 270, 33);
		
				Label lblUsedLetters = new Label(shlWorldeWordFinder, SWT.NONE);
				lblUsedLetters.setAlignment(SWT.RIGHT);
				lblUsedLetters.setBackground(SWTResourceManager.getColor(42, 42, 42));
				lblUsedLetters.setForeground(SWTResourceManager.getColor(255, 204, 51));
				lblUsedLetters.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
				lblUsedLetters.setBounds(0, 27, 148, 33);
				lblUsedLetters.setText("Used Letters:");

		txtUnused = new Text(shlWorldeWordFinder, SWT.BORDER);
		txtUnused.setBackground(SWTResourceManager.getColor(50, 50, 50));
		txtUnused.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		txtUnused.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		txtUnused.setBounds(154, 66, 270, 33);
		
				Label lblUnusedLetters = new Label(shlWorldeWordFinder, SWT.NONE);
				lblUnusedLetters.setAlignment(SWT.RIGHT);
				lblUnusedLetters.setBackground(SWTResourceManager.getColor(42, 42, 42));
				lblUnusedLetters.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
				lblUnusedLetters.setImage(SWTResourceManager.getImage("C:\\Users\\officeryoda\\Pictures\\KA\\Wordle.png"));
				lblUnusedLetters.setText("Unused Letters:");
				lblUnusedLetters.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
				lblUnusedLetters.setBounds(0, 66, 148, 33);

		txtLetter1 = new Text(shlWorldeWordFinder, SWT.BORDER | SWT.CENTER);
		txtLetter1.setBackground(SWTResourceManager.getColor(50, 50, 50));
		txtLetter1.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		txtLetter1.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Text widget = (Text) e.widget;

				if (editing) {
					editing = false;
					return;
				}

				char c;
				try {
					c = widget.getText().charAt(0);
				} catch (Exception ex) {
					return;
				}

				if (c == ' ') {
					editing = true;
					widget.setText(widget.getText().replace(" ", ""));
					return;
				}

				editing = true;
				widget.setText((c + "").toUpperCase());
			}
		});
		txtLetter1.setFont(SWTResourceManager.getFont("Segoe UI", 29, SWT.BOLD));
		txtLetter1.setBounds(28, 150, 50, 50);

		txtLetter2 = new Text(shlWorldeWordFinder, SWT.BORDER | SWT.CENTER);
		txtLetter2.setBackground(SWTResourceManager.getColor(50, 50, 50));
		txtLetter2.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		txtLetter2.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Text widget = (Text) e.widget;

				if (editing) {
					editing = false;
					return;
				}

				char c;
				try {
					c = widget.getText().charAt(0);
				} catch (Exception ex) {
					return;
				}

				if (c == ' ') {
					editing = true;
					widget.setText(widget.getText().replace(" ", ""));
					return;
				}

				editing = true;
				widget.setText((c + "").toUpperCase());
			}
		});
		txtLetter2.setFont(SWTResourceManager.getFont("Segoe UI", 29, SWT.BOLD));
		txtLetter2.setBounds(107, 150, 50, 50);

		txtLetter3 = new Text(shlWorldeWordFinder, SWT.BORDER | SWT.CENTER);
		txtLetter3.setBackground(SWTResourceManager.getColor(50, 50, 50));
		txtLetter3.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		txtLetter3.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Text widget = (Text) e.widget;

				if (editing) {
					editing = false;
					return;
				}

				char c;
				try {
					c = widget.getText().charAt(0);
				} catch (Exception ex) {
					return;
				}

				if (c == ' ') {
					editing = true;
					widget.setText(widget.getText().replace(" ", ""));
					return;
				}

				editing = true;
				widget.setText((c + "").toUpperCase());
			}
		});
		txtLetter3.setFont(SWTResourceManager.getFont("Segoe UI", 29, SWT.BOLD));
		txtLetter3.setBounds(184, 150, 50, 50);

		txtLetter4 = new Text(shlWorldeWordFinder, SWT.BORDER | SWT.CENTER);
		txtLetter4.setBackground(SWTResourceManager.getColor(50, 50, 50));
		txtLetter4.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		txtLetter4.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Text widget = (Text) e.widget;

				if (editing) {
					editing = false;
					return;
				}

				char c;
				try {
					c = widget.getText().charAt(0);
				} catch (Exception ex) {
					return;
				}

				if (c == ' ') {
					editing = true;
					widget.setText(widget.getText().replace(" ", ""));
					return;
				}

				editing = true;
				widget.setText((c + "").toUpperCase());
			}
		});
		txtLetter4.setFont(SWTResourceManager.getFont("Segoe UI", 29, SWT.BOLD));
		txtLetter4.setBounds(262, 150, 50, 50);

		txtLetter5 = new Text(shlWorldeWordFinder, SWT.BORDER | SWT.CENTER);
		txtLetter5.setBackground(SWTResourceManager.getColor(50, 50, 50));
		txtLetter5.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		txtLetter5.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Text widget = (Text) e.widget;

				if (editing) {
					editing = false;
					return;
				}

				char c;
				try {
					c = widget.getText().charAt(0);
				} catch (Exception ex) {
					return;
				}

				if (c == ' ') {
					editing = true;
					widget.setText(widget.getText().replace(" ", ""));
					return;
				}

				editing = true;
				widget.setText((c + "").toUpperCase());
			}
		});
		txtLetter5.setFont(SWTResourceManager.getFont("Segoe UI", 29, SWT.BOLD));
		txtLetter5.setBounds(340, 150, 50, 50);

		lblKnownLetters = new Label(shlWorldeWordFinder, SWT.NONE);
		lblKnownLetters.setBackground(SWTResourceManager.getColor(42, 42, 42));
		lblKnownLetters.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		lblKnownLetters.setAlignment(SWT.CENTER);
		lblKnownLetters.setText("Known Letters");
		lblKnownLetters.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		lblKnownLetters.setBounds(10, 111, 414, 33);

		txtWords = new Text(shlWorldeWordFinder, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		txtWords.setBackground(SWTResourceManager.getColor(50, 50, 50));
		txtWords.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		txtWords.setBounds(440, 27, 194, 224);
		
		lblWords = new Label(shlWorldeWordFinder, SWT.NONE);
		lblWords.setBackground(SWTResourceManager.getColor(42, 42, 42));
		lblWords.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		lblWords.setFont(SWTResourceManager.getFont("Segoe UI", 17, SWT.BOLD));
		lblWords.setBounds(440, 0, 171, 25);
		lblWords.setText("Words: 0");

	}

	private void find() {
		shlWorldeWordFinder.setSize(650, 300);
		shlWorldeWordFinder.setMinimumSize(650, 300);
		shlWorldeWordFinder.setMaximumSize(650, 300);

		String[] unused = txtUnused.getText().split("");
		String[] used = txtUsed.getText().split("");
		List<String> known = new ArrayList<>();
		known.add(txtLetter1.getText());
		known.add(txtLetter2.getText());
		known.add(txtLetter3.getText());
		known.add(txtLetter4.getText());
		known.add(txtLetter5.getText());
		
		List<String> wordList = wordleFinder.find(unused, used, known);
		
		String words = "";

		if(!wordList.isEmpty()) {
			for (String word : wordList)
				words += word + ", ";
			words = words.substring(0, words.length() - 1);
		} else {
			words = "No words found";
		}
		
		lblWords.setText("Words: " + wordList.size());
		txtWords.setText(words.equals("No words found") ? words : words.substring(0, words.length() - 1));
	}
}
