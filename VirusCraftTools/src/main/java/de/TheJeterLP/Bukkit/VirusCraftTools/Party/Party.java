package de.TheJeterLP.Bukkit.VirusCraftTools.Party;

import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Party {

    private final Player creator;
    private final List<String> players = new ArrayList<>();
    private final List<String> invitions = new ArrayList<>();

    protected Party(Player creator) {
        this.creator = creator;
        players.add(creator.getName());
    }

    public boolean contains(Player p) {
        return players.contains(p.getName());
    }

    public void add(Player p) {
        for (String s : players) {
            Utils.sendMessage(MessageType.PARTY, Bukkit.getPlayerExact(s), p.getName() + " joined the party.");
        }
        players.add(p.getName());
    }

    public void remove(Player p) {
        if (!contains(p)) return;
        if (isCreator(p)) {
            PartyManager.getInstance().deleteParty(this);
        }
        players.remove(p.getName());
        if (players.isEmpty()) {
            PartyManager.getInstance().deleteParty(this);
        }
    }

    public Player getCreator() {
        return creator;
    }

    public boolean isCreator(Player p) {
        return creator.getName().equals(p.getName()) && contains(p);
    }

    public List<String> getPlayers() {
        return players;
    }

    public void invite(Player target, Player invitor) {
        this.invitions.add(target.getName());
        Utils.sendMessage(MessageType.PARTY, target, "You got an invition for a party. Do /party join to accept it.");
        Utils.sendMessage(MessageType.PARTY, invitor, "You invited " + target.getName() + " to your party.");
    }

    public boolean isInvited(Player p) {
        return invitions.contains(p.getName());
    }

    public void uninvite(Player p) {
        if (!isInvited(p)) return;
        invitions.remove(p.getName());
    }

    public int size() {
        return players.size();
    }

    public List<String> getInvitions() {
        return this.invitions;
    }

}
