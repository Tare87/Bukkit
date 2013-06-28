package de.zeltclan.tare.zeltcmds.cmds;

import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import de.zeltclan.tare.bukkitutils.MessageUtils;
import de.zeltclan.tare.zeltcmds.CmdParent;
import de.zeltclan.tare.zeltcmds.ZeltCmds;
import de.zeltclan.tare.zeltcmds.enums.RequireListener;
import de.zeltclan.tare.zeltcmds.enums.Type;

public final class CmdMode extends CmdParent {

	public static enum Types implements Type{
		ADVENTURE,
		ADVENTURE_CREATIVE, ADVENTURE_SURVIVAL,
		ADVENTURE_CREATIVE_SURVIVAL, ADVENTURE_SURVIVAL_CREATIVE,
		CREATIVE,
		CREATIVE_ADVENTURE, CREATIVE_SURVIVAL,
		CREATIVE_ADVENTURE_SURVIVAL, CREATIVE_SURVIVAL_ADVENTURE,
		SURVIVAL, SURVIVAL_ADVENTURE, SURVIVAL_CREATIVE,
		SURVIVAL_ADVENTURE_CREATIVE, SURVIVAL_CREATIVE_ADVENTURE;
	}
	
	private final Types type;
	private final String msg;
	
	public CmdMode(Types p_type, Permission p_perm, Permission p_permExt, RequireListener p_listener, String p_msg) {
		super(ZeltCmds.getLanguage().getString("description_mode_" + p_type.name().toLowerCase()), p_perm, p_permExt, p_listener);
		type = p_type;
		msg = (p_msg.isEmpty() ? null : p_msg);
	}
	
	@Override
	protected void executeConsole(CommandSender p_sender, String p_cmd, String[] p_args) {
		switch (p_args.length) {
		case 0:
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_not_enough"));
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player", new Object[] {p_cmd}));
			break;
		case 1:
			final OfflinePlayer off_player = p_sender.getServer().getOfflinePlayer(p_args[0]);
			if (off_player.isOnline()) {
				final Player player = off_player.getPlayer();
				this.rotateMode(player);
			} else {
				MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
			}
			break;
		default:
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
			MessageUtils.msg(p_sender, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_Player", new Object[] {p_cmd}));
			break;
		}
	}
	
	@Override
	protected String executePlayer(Player p_player, String p_cmd, String[] p_args) {
		switch (p_args.length) {
		case 0:
			if (this.checkPerm(p_player, false)) {
				GameMode newMode = this.rotateMode(p_player);
				return (ZeltCmds.getLanguage().getString("log_mode_self", new Object[] {newMode.name(), p_player.getDisplayName()}));
			}
			break;
		case 1:
			if (this.checkPerm(p_player, true)) {
				final OfflinePlayer off_player = p_player.getServer().getOfflinePlayer(p_args[0]);
				if (off_player.isOnline()) {
					final Player player = off_player.getPlayer();
					GameMode newMode = this.rotateMode(player);
					return (ZeltCmds.getLanguage().getString("log_mode_player", new Object[] {newMode.name(), p_player.getDisplayName(), player.getDisplayName()}));
				} else {
					MessageUtils.msg(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString((off_player.getFirstPlayed() != 0 ? "player_offline" : "player_not_found"), new Object[] {p_args[0]}));
				}
			}
			break;
		default:
			MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("arguments_too_many"));
			MessageUtils.warning(p_player, "[" + this.getPlugin().getName() + "] " + ZeltCmds.getLanguage().getString("usage_player", new Object[] {"/" + p_cmd}));
			break;
		}
		return null;
	}
	
	private GameMode rotateMode(Player p_player) {
		String[] types = type.name().split("_");
		GameMode[] modes = new GameMode[types.length];
		for (int i = 0; i < types.length; i++) {
			if (types[i].equalsIgnoreCase("adventure")) {
				modes[i] = GameMode.ADVENTURE;
			} else if (types[i].equalsIgnoreCase("creative")) {
				modes[i] = GameMode.CREATIVE;
			} else {
				modes[i] = GameMode.SURVIVAL;
			}
		}
		int index = 0;
		for (int i = 0; i < (modes.length-1); i++) {
			if (modes[i] == p_player.getGameMode()) {
				index = i+1;
				break;
			}
		}
		p_player.setGameMode(modes[index]);
		if (msg != null) {
			MessageUtils.info(p_player, msg);
		}
		return p_player.getGameMode();
	}
}
