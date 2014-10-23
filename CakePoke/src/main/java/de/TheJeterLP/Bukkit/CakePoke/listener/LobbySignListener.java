package de.TheJeterLP.Bukkit.CakePoke.listener;

import de.TheJeterLP.Bukkit.VirusCraftTools.Party.Party;
import de.TheJeterLP.Bukkit.VirusCraftTools.Party.PartyManager;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import de.TheJeterLP.Bukkit.CakePoke.Arena.Arena;
import de.TheJeterLP.Bukkit.CakePoke.Arena.ArenaManager;
import de.TheJeterLP.Bukkit.CakePoke.Bukkit.CakePoke;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageManager.PrefixType;
import org.bukkit.Bukkit;

public class LobbySignListener extends VClistener {

    @EventHandler
    public void onLobbySign(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!(event.getClickedBlock().getState() instanceof Sign)) return;
        Sign s = (Sign) event.getClickedBlock().getState();
        if (!s.getLine(0).contains("[CP]")) return;
        int id = Integer.valueOf(s.getLine(0).split(" - ")[1]);
        Arena a = ArenaManager.getArena(id);
        if (a == null) {
            event.getPlayer().sendMessage("§cArena does not exist.");
            return;
        }

        if (PartyManager.getInstance().getParty(event.getPlayer()) == null) {
            a.addPlayer(event.getPlayer());
        } else {
            Party pa = PartyManager.getInstance().getParty(event.getPlayer());

            if (!pa.isCreator(event.getPlayer())) {
                CakePoke.getMessageManager().message(event.getPlayer(), PrefixType.BAD, "Only the creator of your party can choose a game.");
                return;
            }

            if ((a.getNumPlayers() - a.getDatas().size()) < pa.size()) {
                CakePoke.getMessageManager().message(event.getPlayer(), PrefixType.BAD, "The arena does not has enough free slots for your whole party.");
                return;
            }
            
            for (String party : pa.getPlayers()) {
                if (Bukkit.getPlayerExact(party) == null) continue;
                a.addPlayer(Bukkit.getPlayerExact(party));
            }

        }
    }

}
