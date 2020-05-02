package br.com.eterniaserver.commands;

import br.com.eterniaserver.config.Configs;
import br.com.eterniaserver.config.Strings;
import br.com.eterniaserver.methods.RTPPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RTP implements CommandExecutor {

    private final RTPPlayer rtpPlayer;
    private final Strings strings;
    private final Configs configs;

    public RTP(RTPPlayer rtpPlayer, Strings strings, Configs configs) {
        this.rtpPlayer = rtpPlayer;
        this.strings = strings;
        this.configs = configs;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1 && player.hasPermission("eternia.rtp.reload")) {
                configs.reload();
                player.sendMessage(strings.putPrefix("server.reload"));
            } else if (args.length == 0 && player.hasPermission("eternia.rtp")) {
                if (!configs.ptp.containsKey(player)) {
                    if (configs.econ) {
                        if (configs.economy.has(player, configs.configs.getInt("server.amount"))) {
                            configs.economy.withdrawPlayer(player, configs.configs.getInt("server.amount"));
                            rtpPlayer.teleportPlayer(player);
                        } else {
                            player.sendMessage(strings.putPrefix("econ.no-money"));
                        }
                    } else {
                        rtpPlayer.teleportPlayer(player);
                    }
                } else {
                    player.sendMessage(strings.putPrefix("rtp.wait"));
                }
            } else {
                player.sendMessage(strings.putPrefix("server.no-perm"));
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(strings.putPrefix("server.only-player"));
        }
        return true;
    }

}
