package commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import Main.SQLRequest;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class Defineprefix implements DiscordCommands {
	private MessageChannel channel;
	private ArrayList<String> args;
	private SQLRequest req;
	private User user;
	private Guild guild;
	private Message message;
	private char prefix;
	
	public Defineprefix(char prefix, MessageChannel channel, ArrayList<String> args, SQLRequest req, User user, Guild guild, Message message) {
		this.prefix = prefix;
		this.channel = channel;
		this.args = args;
		this.req = req;
		this.user = user;
		this.guild = guild;
		this.message = message;
	}
	
	public void build() {
		if (args.size() != 2) {
			channel.sendMessageFormat("`" + prefix + "defineprefix prefix` seulement").queue();
			return;
		}
		String s_prefix = args.get(1);
		if (s_prefix.length() != 1) {
			channel.sendMessageFormat("Un pr?fixe de taille 1 s'il-te-pla?t!").queue();
			return;
		}
		guild.retrieveMemberById(user.getIdLong()).queue(
			(member)-> {
				EnumSet<Permission> permissions = member.getPermissions();
				if (!permissions.contains(Permission.ADMINISTRATOR)) {
					channel.sendMessageFormat("Tu n'es pas administrateur").queue();
					return;
				}
				message.delete().queue();
				char prefix = s_prefix.charAt(0);
				try {
					req.update("UPDATE prefixes SET prefixe = " + String.valueOf((int) prefix) + " WHERE id_server = " + guild.getId() + ";");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				channel.sendMessage("Le pr?fixe de ce serveur est `" + prefix + "` d?sormais").queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
			}
		);
	}
}
