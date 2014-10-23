/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.thejeterlp.shoplogin;

import de.thejeterlp.shoplogin.command.BaseCommand;
import de.thejeterlp.shoplogin.command.CommandArgs;
import de.thejeterlp.shoplogin.command.CommandHandler;
import de.thejeterlp.shoplogin.command.CommandResult;
import de.thejeterlp.shoplogin.command.HelpPage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
@CommandHandler
public class Executor {

    private final HelpPage helpPage = new HelpPage("register");

    public Executor() {
        helpPage.addPage("email password", "Creates a new account for you on http://shop.vc-network.com");
        helpPage.prepare();
    }

    @BaseCommand(command = "register", sender = BaseCommand.Sender.CONSOLE)
    public CommandResult executeConsole(CommandSender sender, CommandArgs args) {
        if (helpPage.sendHelp(sender, args)) return CommandResult.SUCCESS;
        return CommandResult.ONLY_PLAYER;
    }

    @BaseCommand(command = "register", sender = BaseCommand.Sender.PLAYER)
    public CommandResult executePlayer(final Player sender, final CommandArgs args) throws SQLException {
        if (helpPage.sendHelp(sender, args)) return CommandResult.SUCCESS;
        if (args.getLength() != 2) return CommandResult.ERROR;
        sender.sendMessage("§aYour account will be created now, this may take up to a few seconds...");
        PreparedStatement st = Main.getDB().getPreparedStatement("SELECT ID FROM user WHERE uuid = ?;");
        final String uuid = sender.getUniqueId().toString();
        st.setString(1, uuid);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            Main.getDB().closeResultSet(rs);
            Main.getDB().closeStatement(st);
            sender.sendMessage("§cYour account is already registered!");
            return CommandResult.SUCCESS;
        }

        PreparedStatement st2 = Main.getDB().getPreparedStatement("SELECT ID FROM user WHERE email = ?;");
        st2.setString(1, args.getString(0));
        ResultSet rs2 = st2.executeQuery();

        if (rs2.next()) {
            Main.getDB().closeResultSet(rs2);
            Main.getDB().closeStatement(st2);
            sender.sendMessage("§cYour account is already registered!");
            return CommandResult.SUCCESS;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {

            @Override
            public void run() {
                try {
                    PreparedStatement st3 = Main.getDB().getPreparedStatement("INSERT INTO user (uuid, email, password, rank) VALUES (?, ?, ?, ?);");
                    st3.setString(1, sender.getUniqueId().toString());
                    st3.setString(2, args.getString(0));
                    st3.setString(3, "follows");
                    st3.setString(4, Main.getInstance().getChat().getPlayerGroups(sender)[0]);
                    st3.executeUpdate();
                    Main.getDB().closeStatement(st3);

                    PreparedStatement st4 = Main.getDB().getPreparedStatement("SELECT ID FROM user WHERE uuid = ?;");
                    st4.setString(1, uuid);
                    ResultSet rs4 = st4.executeQuery();

                    int id = -1;
                    if (rs4.next()) {
                        id = rs4.getInt("ID");
                    }
                    Main.getDB().closeResultSet(rs4);
                    Main.getDB().closeStatement(st4);

                    PreparedStatement st5 = Main.getDB().getPreparedStatement("UPDATE user SET password = ? WHERE uuid = ? AND email = ?;");
                    String hash = Util.getMd5Hash(Util.getMd5Hash(id + "") + args.getString(1));
                    st5.setString(1, hash);
                    st5.setString(2, uuid);
                    st5.setString(3, args.getString(0));
                    st5.executeUpdate();
                    Main.getDB().closeStatement(st5);
                    sender.sendMessage("§aYour account was registered! You can now login at http://shop.vc-network.com");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        return CommandResult.SUCCESS;
    }

}
