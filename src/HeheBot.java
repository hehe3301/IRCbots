import org.jibble.pircbot.*;

import java.util.*;
import java.io.*;

public class HeheBot extends PircBot {
	private HashMap<String, String> people = new HashMap<String, String>();
	private HashMap<String, String> admins = new HashMap<String, String>();
	private HashMap<String, String> help = new HashMap<String, String>();
	private HashMap<String, BufferedWriter> chanLogs = new HashMap<String, BufferedWriter>();
	private String[] hehe = { "yes?", "what?", "yyyyesss?", "what is it?",
			"what do you want?" };
	private ArrayList<String> ignoreList;

	public HeheBot() {
		this.setName("hehe|bot");
		admins.put("hehe", "pass");
		try {
			File ignoreFile = new File("ignore.txt"); // opens the ignore list
			if (!ignoreFile.exists()) {
				ignoreFile.createNewFile();
			}
			File commandFile = new File("commands.txt"); // opens the command
															// list
			if (!ignoreFile.exists()) {
				throw new FileNotFoundException();
			}

			File adminsFile = new File("admins.txt"); // opens the admin list
			if (!ignoreFile.exists()) {
				throw new FileNotFoundException();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		// TODO load help, people, ignoreList
		// TODO load admins
		ignoreList = new ArrayList<String>();
		ignoreList.add("Mewtwo");
		ignoreList.add("Inumuta");
		ignoreList.add("RX14");
	}

	/**
	 * 
	 */
	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		String[] line = message.split(" ");
		// -----LOG-----//
		log(channel, sender + ": " + message + "\n");
		// -----updates the used-----
		if (!people.containsKey(sender)) {// if i don't know who this person is
			people.put(sender, hostname);// add them to the map
		}

		// ------parses the line ------
		if (!ignoreList.contains(sender) && line.length >= 2) {
			if (line[0].equalsIgnoreCase(this.getName() + ":")) {
				if (line[1].equalsIgnoreCase("Quit")
						&& admins.containsKey(sender)) {
					this.cleanQuit();
				} else if (line[1].equalsIgnoreCase("leave")
						&& admins.containsKey(sender)) {
					sendMessage(channel, "Goodbye all! " + sender
							+ " told me to quit");
					this.partChannel(channel, "Admin told me to quit");
				} else if (line[1].equalsIgnoreCase("ignore")
						&& admins.containsKey(sender)) {
					if (line.length == 3) {
						sendMessage(channel, "Will tell " + line[2]
								+ " to go f**k himself!");
						ignoreList.add(line[2]);
					} else {
						sendMessage(channel, "Bad Argument for command: "
								+ line[1]);
					}

				}else if (line[1].equalsIgnoreCase("unignore")
						&& admins.containsKey(sender)) {
					if (line.length == 3) {
						sendMessage(channel, "Will stop telling " + line[2]
								+ " to go f**k himself!");
						ignoreList.remove(line[2]);
					} else {
						sendMessage(channel, "Bad Argument for command: "
								+ line[1]);
					}

				} else {
					sendMessage(channel, "Sorry " + sender
							+ " i dont know how to do that!");
				}
			}

		} else if (message.contains("hehe") || (message.contains("he he"))
				|| (message.contains("Hehe")) || (message.contains("HEHE"))) {
			if (!ignoreList.contains(sender))
				sendMessage(channel, "" + hehe[randInt(0, hehe.length - 1)]);
		}
	}

	/**
	 * 
	 */
	public void onNickChange(String oldNick, String login, String hostname,
			String newNick) {
		people.remove(oldNick);
		people.put(newNick, hostname);

		if (ignoreList.contains(oldNick)) {
			ignoreList.add(newNick);
		}

		for (String chan : chanLogs.keySet()) {
			try {
				chanLogs.get(chan).write(oldNick + " -> " + newNick);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 
	 * @param chan
	 */
	public void joinChan(String chan) {
		joinChannel(chan);
		try {
			File chanLog = new File(chan + ".txt"); // opens the chan log
			if (!chanLog.exists()) {
				chanLog.createNewFile();
			}
			FileWriter fileWritter = new FileWriter(chanLog.getName(), true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			chanLogs.put(chan, bufferWritter);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ConcurrentModificationException derp){
			//herplederp
		}

	}

	public void quit(String chan) throws IOException{
		say(chan, "Goodbye all! admin told me to quit");
		chanLogs.get(chan).close();
		
		println("Closed: " + chan);
		this.partChannel(chan, "Admin told me to quit");
	}
	/**
	 * 
	 */
	public void cleanQuit() {
		while(chanLogs.size()>0){
			try {
				String chan = (String) chanLogs.keySet().toArray()[0]; //selects a chan
				quit(chan); //removes us form the chan, closes the log
				chanLogs.remove(chan); //removes the chan form the lit of logs
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				chanLogs.clear();
			}
		}
			}

	public void log(String chan, String toLog){
		try { // -----Logs the message-----
			chanLogs.get(chan).write(toLog);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void say(String chan, String msg){
		sendMessage(chan, msg);
		log(chan, this.getName() + ": " + msg + "\n");
		
	}
	/**
	 * Generates a random int between min and max
	 * 
	 * @param min
	 *            - the minimum value the rand int can be
	 * @param max
	 *            - the maximum value the random int can be
	 * @return - the random int between min and max
	 */
	private int randInt(int min, int max) {
		Random rn = new Random();
		int range = max - min + 1;
		int randomNum = rn.nextInt(range) + min;
		return randomNum;
	}

	/**
	 * Prints an object then a new line
	 * 
	 * @param o
	 *            - the object ot be printed
	 */
	private void println(Object o) {
		System.out.println(o);
	}
}