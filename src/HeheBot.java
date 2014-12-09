import org.jibble.pircbot.*;
import java.util.*;

public class HeheBot extends PircBot {
	private HashMap<String, String> people = new HashMap<String, String>();
	private HashMap<String, String> admins = new HashMap<String, String>();
	private HashMap<String, String> help = new HashMap<String, String>();
	private String[] hehe = { "yes?", "what?", "yyyyesss?", "what is it?",
			"what do you want?" };
	private ArrayList<String> ignoreList;

	public HeheBot() {
		this.setName("hehe|bot");
		// TODO load help, people, ignoreList
		// TODO load admins
		ignoreList=new ArrayList<String>();
		ignoreList.add("Mewtwo");
		ignoreList.add("Inumuta");
	}

	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		String[] line = message.split(" ");

		if (!people.containsKey(sender)) {//if i don't know who this person is
			people.put(sender, hostname);//add them to the map
		}
		if (!ignoreList.contains(sender) && line.length > 1) {
			if (line[0].equalsIgnoreCase(this.getName() + ":")) {
				println(people);
			}

		} else if (message.contains("hehe") || (message.contains("he he"))||(message.contains("Hehe")) || (message.contains("HEHE") )) {
			if(!ignoreList.contains(sender))
				sendMessage(channel, ""+hehe[randInt(hehe.length-1)]);
		}
	}

	public void onNickChange(String oldNick, String login, String hostname,
			String newNick) {
		people.remove(oldNick);
		people.put(newNick, hostname);
		if (ignoreList.contains(oldNick)) {
			ignoreList.add(newNick);
		}
	}

	private int randInt(int min, int max){
		Random rn = new Random();
		int range = max - min + 1;
		int randomNum =  rn.nextInt(range) + min;
		return randomNum;
	}
	private int randInt(int max){
		Random rn = new Random();
		int range = max + 1;
		int randomNum =  rn.nextInt(range);
		return randomNum;
	}
	private void println(Object o) {
		System.out.println(o);
	}
}