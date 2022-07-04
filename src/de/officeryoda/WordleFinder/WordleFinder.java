package de.officeryoda.WordleFinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

public class WordleFinder {
	List<String> linkedList;

	private String[] unused;
	private String[] used;
	private List<String> known;
	
	public void start() {
		linkedList = new LinkedList<>();
		
		WordleInput wordleInput = new WordleInput();
		wordleInput.open(this);
	}
	
	private List<String> sort(List<String> list) {
//		long time = System.currentTimeMillis();

		list = getWords();

		List<String> removeList = new ArrayList<>();
		removeList.add("");
		for (String word : list) {
			boolean remove = false;

			//specific letter check
			int charNum = 0;
			for (String knownChar : known) {
				if (knownChar.isBlank()) {
					charNum++;
					continue;
				}
				if(!word.substring(charNum, charNum+1).toUpperCase().equals(knownChar)) {
					remove = true;
					break;
				}
				
				charNum++;
			}
			
			if (remove) {
				removeList.add(word);
				continue;
			}

			//used letters check
			for (String usedChar : used) {
				if(!word.contains(usedChar)) {
					remove = true;
					break;
				}
			}

			if (remove) {
				removeList.add(word);
				continue;
			}

			//unused letters check
			for (String unusedChar : unused) {
				if (unusedChar.equals("")) continue;

				if(word.contains(unusedChar)) {
					remove = true;
					break;
				}
			}

			if (remove) {
				removeList.add(word);
				continue;
			}

		}

		list.removeAll(removeList);
		
		return list;
	}

	private List<String> getWords() {
		URL url = null;
		String username = "YodasBots";
		String password = "Y0da's_Git-Acc0unt!";
		List<String> content = new LinkedList<>();
		try {
			url = new URL("https://raw.githubusercontent.com/YodasBots/WordleList/main/sortedWorldeWords.txt");
			URLConnection uc;
			uc = url.openConnection();

			uc.setRequestProperty("X-Requested-With", "Curl");
			String userpass = username + ":" + password;
			String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes())); //needs Base64 encoder, apache.commons.codec
			uc.setRequestProperty("Authorization", basicAuth);

			BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null)
				content.add(line);

		} catch (IOException e) {
			return content;
		}

		return content;
	}

	public List<String> find(String[] unused, String[] used, List<String> known) {
		this.unused = unused;
		this.used = used;
		this.known = known;

		return sort(linkedList);
	}
}