package de.TheJeterLP.Bukkit.VirusCraftTools.Party;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class PartyManager {

    private final List<Party> parties = new ArrayList<>();
    private static final PartyManager INSTANCE = new PartyManager();

    public static PartyManager getInstance() {
        return INSTANCE;
    }

    public void createParty(Player creator) {
        Party p = new Party(creator);
        parties.add(p);
    }

    public List<Party> getParties() {
        return this.parties;
    }

    public Party getParty(Player p) {
        for (Party party : parties) {
            if (party.contains(p)) return party;
        }
        return null;
    }

    public void deleteParty(Party p) {
        parties.remove(p);
    }

    public boolean isInvited(Player p) {
        for (Party pa : parties) {
            if (pa.isInvited(p)) return true;
        }
        return false;
    }

    public Party getPartyForInvition(Player p) {
        for (Party pa : parties) {
            if (pa.isInvited(p)) return pa;
        }
        return null;
    }

}
