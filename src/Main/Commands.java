package Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import commands.Changecasw;
import commands.Changefamilier;
import commands.Changefetiche;
import commands.Changepseudosw;
import commands.Changeurlmaster;
import commands.Definecolor;
import commands.Defineprefix;
import commands.Familiers;
import commands.Help;
import commands.Inventory;
import commands.Me;
import commands.Renamemaster;
import commands.Test;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.Emoji.Type;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {
	private SQLRequest req;
	private FamiliersEmojis emojis;
	
	private ArrayList<String> getargs(String content) {
		ArrayList<String> args = new ArrayList<String>();
		String tmp_string = "";
		int length = content.length();
		char last_char= ' ', actual_char = ' ';
		boolean guillemet_ouvert = false;
		for (int iterator = 0; iterator < length; iterator++) {
			last_char = actual_char;
			actual_char = content.charAt(iterator);
			if (!guillemet_ouvert && (actual_char == ' ' || actual_char == '\n')) {
				if (!tmp_string.equals("")) {
					args.add(tmp_string);
					tmp_string = "";
				}
			} else if (last_char != '\\' && actual_char == '\"' && !guillemet_ouvert) {
				guillemet_ouvert = true;
			} else if (last_char != '\\' && actual_char == '\"' && guillemet_ouvert) {
				guillemet_ouvert = false;
				if (!tmp_string.equals("")) {
					args.add(tmp_string);
					tmp_string = "";
				}
			} else {
				tmp_string = tmp_string + actual_char;
			}
		}
		if (!tmp_string.equals("")) {
			args.add(tmp_string);
			tmp_string = "";
		}
		return args;
	}
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
		MessageChannel channel = event.getChannel();
		Guild guild = event.getGuild();
        Message message = event.getMessage();
        User user = message.getAuthor();
        String content = message.getContentRaw();
        ArrayList<String> args = getargs(content);
        try {
        	ResultSet res = req.request("SELECT * FROM Carte WHERE id_member = " + user.getId().toString() + " AND id_server = " + guild.getId().toString() + ";");
			if (!res.next()) {
				req.update("INSERT INTO Carte VALUES (" + guild.getId().toString() + ", " + user.getId().toString() + ", 'Mario', NULL, NULL, NULL, 1, 0, NULL);");
				String request = ", 0, 20, 50, 1";
				for (int iterator = 0; iterator < 19; iterator++) {
					request += ", 0, 0, 0, 0";
				}
				request += ", 1);";
				req.update("INSERT INTO Familiers VALUES (" + guild.getId().toString() + ", " + user.getId().toString() + request);
				req.update("INSERT INTO Inventaire VALUES (" + guild.getId().toString() + ", " + user.getId().toString() + ", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);");
			}
			res.close();
			res = req.request("SELECT * FROM Prefixes WHERE id_server = " + guild.getId().toString() + ";");
			char prefix = '?';
			if (!res.next()) {
				req.update("INSERT INTO Prefixes VALUES (" + guild.getId().toString() + ", " + String.valueOf((int) '?') + ")");
			} else {
				prefix = (char) res.getInt("prefixe");
			}
			res.close();
			res = req.request("SELECT * FROM Couleurs WHERE id_server = " + guild.getId().toString() + ";");
			int couleur = 16711680;
			if (!res.next()) {
				req.update("INSERT INTO Couleurs VALUES (" + guild.getId().toString() + ", 16711680)");
			} else {
				couleur = res.getInt("couleur");
			}
			res.close();
	        if (args.size() == 0 || args.get(0).charAt(0) != prefix) {
	        	return;
	        }
	        String suffixe = args.get(0).substring(1);
	        switch (suffixe) {
		        case "ping":
		            channel.sendMessage("chocho!").queue();
		            return;
		        case "me":
					Me me_command = new Me(prefix, couleur, channel, args, req, user, guild, message, emojis);
					me_command.build();
		        	return;
		        case "renamemaster":
					Renamemaster renamemaster_command = new Renamemaster(prefix, channel, args, req, user, guild, message);
					renamemaster_command.build();
		        	return;
		        case "changefetiche":
					Changefetiche changefetiche_command = new Changefetiche(prefix, channel, args, req, user, guild, message);
					changefetiche_command.build();
		        	return;
		        case "changepseudosw":
					Changepseudosw changepseudosw_command = new Changepseudosw(prefix, channel, args, req, user, guild, message);
					changepseudosw_command.build();
		        	return;
		        case "changecasw":
					Changecasw changecasw_command = new Changecasw(prefix, channel, args, req, user, guild, message);
					changecasw_command.build();
		        	return;
		        case "changeurlmaster":
					Changeurlmaster changeurlmaster_command = new Changeurlmaster(prefix, channel, args, req, user, guild, message);
					changeurlmaster_command.build();
		        	return;
		        case "inventory":
					Inventory inventory_command = new Inventory(prefix, couleur, channel, args, req, user, guild, message);
					inventory_command.build();
		        	return;
		        case "familiers":
					Familiers familiers_command = new Familiers(prefix, couleur, channel, args, req, user, guild, message, emojis);
					familiers_command.build();
		        	return;
		        case "help":
		        	Help help = new Help(prefix, couleur, channel, args, message);
		        	help.build();
		        	return;
		        case "defineprefix":
					Defineprefix defineprefix_command = new Defineprefix(prefix, channel, args, req, user, guild, message);
					defineprefix_command.build();
		        	return;
		        case "definecolor":
		        	Definecolor definecolor_command = new Definecolor(prefix, channel, args, req, user, guild, message);
		        	definecolor_command.build();
		        	return;
		        case "changefamilier":
		        	Changefamilier changefamilier_command = new Changefamilier(prefix, channel, args, req, user, guild, message);
		        	changefamilier_command.build();
		        	return;
		        case "test":
		        	Test test_command = new Test(prefix, couleur, channel, args, message);
		        	test_command.build();
		        	return;
	        }
        } catch (ClassNotFoundException | SQLException e) {
        	e.printStackTrace();
        }
    }
	
	public Commands(SQLRequest req) {
		this.req = req;
		this.emojis = new FamiliersEmojis();
	}
	public boolean is_in(String word, String[] list_words) {
		for (String tmp_word : list_words) {
			if (tmp_word.equals(word)) {
				return true;
			}
		}
		return false;
	}
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		User user = event.getUser();
		Guild guild = event.getGuild();
		if (user.isBot()) {
			return;
		}
		Message message = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
		MessageReaction reaction = event.getReaction();
		Emoji emoji = reaction.getEmoji();
		try {
			if (emoji.getType().equals(Type.CUSTOM) && (is_in(emoji.getFormatted(), emojis.get_emojis()))) {
				ResultSet res = req.request("SELECT * FROM Couleurs WHERE id_server = " + guild.getId().toString() + ";");
				int couleur = res.getInt("couleur");
				res.close();
				res = req.request("SELECT * FROM FamiliersEmbeds WHERE id_message = " + message.getId() + " AND id_member = " + user.getId() + ";");
				int page = res.getInt("page");
				res.close();
				PageUpdater pageupdater = new PageUpdater(user, message, page, emoji.getFormatted(), req, emojis, reaction, guild, couleur);
				pageupdater.update();
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
    public void onReady(ReadyEvent event) {
    }
	
}
