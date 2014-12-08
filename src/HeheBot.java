import org.jibble.pircbot.*;

public class HeheBot extends PircBot {

	public HeheBot() {
		this.setName("hehe|bot");
	}

	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		if (message.contains("hehe")) {
			sendMessage(channel, "yes?");
		}
	}

}