package commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Main.FamiliersEmojis;
import Main.SQLRequest;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class Familiers {
	private MessageChannel channel;
	private ArrayList<String> args;
	private SQLRequest req;
	private User user;
	private Guild guild;
	private Message message;
	private FamiliersEmojis emojis;
	
	public Familiers(MessageChannel channel, ArrayList<String> args, SQLRequest req, User user, Guild guild, Message message, FamiliersEmojis emojis) {
		this.channel = channel;
		this.args = args;
		this.req = req;
		this.user = user;
		this.guild = guild;
		this.message = message;
		this.emojis = emojis;
	}
	
	public void build() throws ClassNotFoundException, SQLException {
		if (args.size() != 1) {
			channel.sendMessageFormat("`?familiers` seulement").queue();
			return;
		}
		message.delete().queue();
		ResultSet res = req.request("SELECT * FROM Familiers WHERE id_member = " + user.getId().toString() + " AND id_server = " + guild.getId().toString() + ";");
		String[] list_emojis = emojis.get_emojis();
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Familiers");
		for (int iterator = 0; iterator < list_emojis.length; iterator++) {
			int experience = res.getInt("expf" + String.valueOf(iterator + 1));
			String text = experience == -1 ? "-" : "niv. 1\n 20/20 PV\n 50/50 PM";
			embed.addField(list_emojis[iterator], text, true);
		}
		res.close();
		channel.sendMessageEmbeds(embed.build()).queue();
	}
}
