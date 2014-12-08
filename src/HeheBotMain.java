import org.jibble.pircbot.*;

public class HeheBotMain {
    
    public static void main(String[] args) throws Exception {
        
        // Now start our bot up.
    	HeheBot bot = new HeheBot();
        
        // Enable debugging output.
        bot.setVerbose(true);
        
        // Connect to the IRC server.
        bot.connect("irc.esper.net");

        // Join the #pircbot channel.
        bot.joinChannel("#WAMM");
        
    }
    
}