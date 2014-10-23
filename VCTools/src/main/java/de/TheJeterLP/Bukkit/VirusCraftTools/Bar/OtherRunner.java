package de.TheJeterLP.Bukkit.VirusCraftTools.Bar;

/**
 * @author TheJeterLP
 */
public class OtherRunner implements Runnable {

    private int health = 100;
    private boolean countingDown = false;

    @Override
    public void run() {

        if (health == 100 && countingDown == false) {
            countingDown = true;
        } else if (health == 0 && countingDown == true) {
            countingDown = false;
        }

        if (countingDown) {
            health--;
        } else {
            health++;
        }

        BarTool.setHealth(health);
    }

    public int getHealth() {
        return health;
    }

}
