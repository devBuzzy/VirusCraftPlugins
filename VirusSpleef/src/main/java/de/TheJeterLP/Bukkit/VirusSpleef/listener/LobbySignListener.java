package de.TheJeterLP.Bukkit.VirusSpleef.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Party.Party;
import de.TheJeterLP.Bukkit.VirusCraftTools.Party.PartyManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager.PrefixType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import de.TheJeterLP.Bukkit.VirusSpleef.Arena.Arena;
import de.TheJeterLP.Bukkit.VirusSpleef.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.VirusSpleef.Bukkit.VirusSpleef;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LobbySignListener extends VClistener {

    @EventHandler
    public void onLobbySign(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!(event.getClickedBlock().getState() instanceof Sign)) return;
        Sign s = (Sign) event.getClickedBlock().getState();
        if (!s.getLine(0).contains("[Spleef]")) return;
        int id = Integer.valueOf(s.getLine(0).split(" - ")[1]);
        Arena a = ArenaManager.getArena(id);

        if (a == null) {
            event.getPlayer().sendMessage("§cArena does not exist.");
            return;
        }

        Player p = event.getPlayer();
        if (PartyManager.getInstance().getParty(p) != null) {
            Party pa = PartyManager.getInstance().getParty(p);

            if (!pa.isCreator(p)) {
                VirusSpleef.getMessageManager().message(p, PrefixType.BAD, "Only the creator of your party can choose a game.");
                return;
            }

            if ((a.getNumPlayers() - a.getDatas().size()) < pa.size()) {
                VirusSpleef.getMessageManager().message(p, PrefixType.BAD, "The arena does not has enough free slots for your whole party.");
            } else {
                for (String party : pa.getPlayers()) {
                    if (Bukkit.getPlayerExact(party) == null) continue;
                    a.addPlayer(Bukkit.getPlayerExact(party));
                }
            }
        } else {
            a.addPlayer(event.getPlayer());
        }
    }
}
