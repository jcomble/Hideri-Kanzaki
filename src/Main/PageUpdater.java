package Main;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class PageUpdater {
	private User user;
	private Message message;
	private int page;
	private String emoji_name;
	private SQLRequest req;
	private FamiliersEmojis emojis;
	private MessageReaction reaction;
	private Guild guild;
	private int couleur;
	
	public PageUpdater(User user, Message message, int page, String emoji_name, SQLRequest req, FamiliersEmojis emojis, MessageReaction reaction, Guild guild, int couleur) {
		this.user = user;
		this.message = message;
		this.page = page;
		this.emoji_name = emoji_name;
		this.req = req;
		this.emojis = emojis;
		this.reaction = reaction;
		this.guild = guild;
		this.couleur = couleur;
	}
	
	public void update() throws SQLException, ClassNotFoundException {
		page = 0;
		for (String tmp_emoji : emojis.get_emojis()) {
			page +=1;
			if (tmp_emoji.equals(emoji_name)) {
				break;
			}
		}
		ResultSet res = req.request("SELECT * FROM Familiers WHERE id_member = " + user.getId().toString() + " AND id_server = " + guild.getId().toString() + ";");
		int experience = res.getInt("expf" + String.valueOf(page));
		int version = res.getInt("versionf" + String.valueOf(page));
		int pv = res.getInt("pvf" + String.valueOf(page));
		int pm = res.getInt("pmf" + String.valueOf(page));
		StatsReader stats = new StatsReader(page, experience, version, pv, pm);
		String description = stats.getstats();
		res.close();
		req.update("UPDATE FamiliersEmbeds SET page = " + String.valueOf(page) + " WHERE id_message = " + message.getId() + ";");
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(couleur);
		embed.setDescription(description);
		MessageEmbed msgemb = embed.build();
		MessageAction msgact = message.editMessage(emojis.get_emojis()[page - 1]);
		msgact.setEmbeds(msgemb);
		msgact.queue();
		reaction.removeReaction(user).queue();
	}
}
