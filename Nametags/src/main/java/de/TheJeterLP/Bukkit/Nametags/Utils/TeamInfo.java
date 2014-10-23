package de.TheJeterLP.Bukkit.Nametags.Utils;

public class TeamInfo {

    private final String name;
    private String prefix;
    private String suffix;

    public TeamInfo(String name) {
        this.name = name;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getName() {
        return name;
    }

}
