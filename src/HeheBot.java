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
		//this.setName("hehe|bot");
		this.setName("dickbutt");
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
		//ignoreList.add("RX14");
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
				if (line[1].equalsIgnoreCase("Quit") //if we are being told to quit all
						&& admins.containsKey(sender)) {
					this.cleanQuit();
				} else if (line[1].equalsIgnoreCase("leave")//leavs a channel
						&& admins.containsKey(sender)) {
					try {
						quit(channel);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (line[1].equalsIgnoreCase("ignore")//adds someone to the ignore list
						&& admins.containsKey(sender)) {
					if (line.length == 3) {
						sendMessage(channel, "Will tell " + line[2]
								+ " to go f**k himself!");
						ignoreList.add(line[2]);
					} else {
						sendMessage(channel, "Bad Argument for command: "
								+ line[1]);
					}

				} else if (line[1].equalsIgnoreCase("unignore")//unignores someone
						&& admins.containsKey(sender)) {
					if (line.length == 3) {
						sendMessage(channel, "Will stop telling " + line[2]
								+ " to go f**k himself!");
						ignoreList.remove(line[2]);
					} else {
						sendMessage(channel, "Bad Argument for command: "
								+ line[1]);
					}

				} else { //if i dont know how to do a command
					sendMessage(channel, "Sorry " + sender
							+ " I dont know how to do that!");
				}
			}

		} else if (message.contains("hehe") || (message.contains("Hehe")) //if the message is a ping for hehe
				|| (message.contains("HEHE"))) {
			if (!ignoreList.contains(sender))
				sendMessage(channel, "" + hehe[randInt(0, hehe.length - 1)]);
		}
	}

	/**
	 * When someone changes their name
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
	 * this program joins the bot into the chan
	 * 
	 * @param chan
	 *            - the chan to join
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
		} catch (ConcurrentModificationException derp) {
			// herplederp
		}

	}

	/**
	 * Prints to a chanel
	 * 
	 * @param chan
	 * @param msg
	 */
	public void say(String chan, String msg) {
		sendMessage(chan, msg);
		log(chan, this.getName() + ": " + msg + "\n");

	}

	/**
	 * This command quits a chan and closes the log for that chan
	 * 
	 * @param chan
	 *            - the chan to quit
	 * @throws IOException
	 */
	public void quit(String chan) throws IOException {
		
		chanLogs.get(chan).close();
		println("Closed: " + chan);
		this.partChannel(chan, "Admin told me to quit");
	}

	/**
	 * 
	 */
	public void cleanQuit() {
		while (chanLogs.size() > 0) {
			try {
				// selects a chan
				String chan = (String) chanLogs.keySet().toArray()[0];
				say(chan, "Goodbye all! admin told me to quit");
				quit(chan); // removes us form the chan, closes the log
				chanLogs.remove(chan); // removes the chan form the lit of logs
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		chanLogs.clear();
		
		System.exit(0);
	}

	// Adds an entry to a log
	public void log(String chan, String toLog) {
		try { // -----Logs the message-----
			chanLogs.get(chan).write(toLog);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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