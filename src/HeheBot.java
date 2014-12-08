import org.jibble.pircbot.*;

public class HeheBot extends PircBot {

	public HeheBot() {
		this.setName("Hehe_Bot");
	}

	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		if (message.equalsIgnoreCase("time")) {
			String time = new java.util.Date().toString();
			sendMessage(channel, sender + ": The time is now my busness and not yours, hehe");
		}
		if (message.contains("hehe")) {
			sendMessage(channel, "yes?");
		}
	}

}