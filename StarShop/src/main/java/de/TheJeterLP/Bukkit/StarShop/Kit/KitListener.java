package de.TheJeterLP.Bukkit.StarShop.Kit;

import de.TheJeterLP.Bukkit.StarShop.Database.MySQL;
import de.TheJeterLP.Bukkit.StarShop.ScoreboardFactory;
import de.TheJeterLP.Bukkit.StarShop.Starshop;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.MessageType;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.Utils;
import de.TheJeterLP.Bukkit.VirusCraftTools.Utils.VClistener; 
import java.sql.SQLException;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author TheJeterLP
 */
public class KitListener extends VClistener {

    @EventHandler(ignoreCancelled = true)
    public void onClick(PlayerInteractEvent e) throws SQLException {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!(e.getClickedBlock().getState() instanceof Sign)) return;
        Sign sign = (Sign) e.getClickedBlock().getState();
        if (!Utils.removeColors(sign.getLine(0)).equalsIgnoreCase("[kit]")) return;
        String kit = Utils.removeColors(sign.getLine(2));
        int price = Utils.getInteger(Utils.removeColors(sign.getLine(1)));

        if (!MySQL.hasStars(e.getPlayer(), price)) {
            Utils.sendMessage(MessageType.ERROR, e.getPlayer(), "You don't have enough stars to do that!");
            return;
        }

        NormalKit k = Starshop.getInstance().getKitManager().getKit(kit);

        if (!Starshop.getInstance().getKitManager().getKits(e.getPlayer()).contains(k)) {
            MySQL.removeStars(e.getPlayer(), price);
            Utils.sendMessage(MessageType.INFO, e.getPlayer(), "You bought the kit " + kit + " for " + price + " stars.");
            Starshop.getInstance().getKitManager().addKit(k, e.getPlayer());
            ScoreboardFactory.updateBoard(e.getPlayer());
        }

        if (k instanceof RandomKit) {
            Utils.sendMessage(MessageType.INFO, e.getPlayer(), "You selected a Kit with Random generated items.");
        }

        Starshop.getInstance().getKitManager().deselectKit(e.getPlayer());
        Starshop.getInstance().getKitManager().selectKit(e.getPlayer(), k);
        Utils.sendMessage(MessageType.INFO, e.getPlayer(), "You selected " + kit);
    }
}
