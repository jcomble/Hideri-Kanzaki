package Main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class BotStart {
	
	private static String readString(String path_map) {
		try {
			byte[] bytes = Files.readAllBytes(Paths.get(path_map));
			String content = "";
			for (byte tmp_byte : bytes) {
				content = content + (char) tmp_byte;
			}
			return content;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
    public static void main(String[] args) throws LoginException, ClassNotFoundException, SQLException {
    	SQLRequest req = new SQLRequest();
    	String token = readString("src/Main/token.txt");
        JDABuilder.createLight(token, GatewayIntent.DIRECT_MESSAGE_REACTIONS,
        		GatewayIntent.DIRECT_MESSAGE_TYPING,
        		GatewayIntent.DIRECT_MESSAGES,
        		GatewayIntent.GUILD_BANS,
        		GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
        		GatewayIntent.GUILD_INVITES,
        		GatewayIntent.GUILD_MEMBERS,
        		GatewayIntent.GUILD_MESSAGE_REACTIONS,
        		GatewayIntent.GUILD_MESSAGE_TYPING,
        		GatewayIntent.GUILD_MESSAGES,
        		GatewayIntent.GUILD_PRESENCES,
        		GatewayIntent.GUILD_VOICE_STATES,
        		GatewayIntent.GUILD_WEBHOOKS
        	)
            .addEventListeners(new Commands(req))
            .setActivity(Activity.playing("Yamete Kudasai!"))
            .build();
    }
}