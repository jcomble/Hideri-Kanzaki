package Main;

import java.util.ArrayList;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class CommandParameters {
	private MessageChannel channel;
	private ArrayList<String> args;
	private SQLRequest req;
	private User user;
	private Guild guild;
	private Message message;
	private FamiliersEmojis emojis;
	private char prefix;
	private int couleur;
	
	public CommandParameters(MessageChannel channel, ArrayList<String> args, SQLRequest req, User user, Guild guild,
			Message message, FamiliersEmojis emojis, char prefix, int couleur) {
		this.channel = channel;
		this.args = args;
		this.req = req;
		this.user = user;
		this.guild = guild;
		this.message = message;
		this.emojis = emojis;
		this.prefix = prefix;
		this.couleur = couleur;
	}
	
	public MessageChannel getChannel() {
		return channel;
	}
	public void setChannel(MessageChannel channel) {
		this.channel = channel;
	}
	public ArrayList<String> getArgs() {
		return args;
	}
	public void setArgs(ArrayList<String> args) {
		this.args = args;
	}
	public SQLRequest getReq() {
		return req;
	}
	public void setReq(SQLRequest req) {
		this.req = req;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Guild getGuild() {
		return guild;
	}
	public void setGuild(Guild guild) {
		this.guild = guild;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public FamiliersEmojis getEmojis() {
		return emojis;
	}
	public void setEmojis(FamiliersEmojis emojis) {
		this.emojis = emojis;
	}
	public char getPrefix() {
		return prefix;
	}
	public void setPrefix(char prefix) {
		this.prefix = prefix;
	}
	public int getCouleur() {
		return couleur;
	}
	public void setCouleur(int couleur) {
		this.couleur = couleur;
	}
	
}
