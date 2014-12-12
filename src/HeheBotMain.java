
public class HeheBotMain {

	public static void main(String[] args) throws Exception {

		// Now start our bot up.
		HeheBot bot = new HeheBot();

		// Enable debugging output.
		bot.setVerbose(true);

		// Connect to the IRC server.
		bot.connect("irc.esper.net");
		// Join the #WAMM channel.
		bot.joinChan("#WAMM");
		bot.joinChan("#WAMM_HE");
		bot.joinChan("#nupa");

	}

}