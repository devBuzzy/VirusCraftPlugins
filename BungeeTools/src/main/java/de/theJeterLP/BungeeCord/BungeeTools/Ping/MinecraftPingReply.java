package de.theJeterLP.BungeeCord.BungeeTools.Ping;

public class MinecraftPingReply {
    /** The IP of the server */
    private final String ip;
    /** The port of the server */
    private final int port;
    /** The MOTD of the server */
    private final String motd;
    /** The protocol version of the server */
    private final String protocolVersion;
    /** The game version of the server */
    private final String version;
    /** The max player count of the server */
    private final int maxPlayers;
    /** The current online player count of the server */
    private final int onlinePlayers;

    protected MinecraftPingReply(final String ip, final int port, final String motd, final int onlinePlayers, final int maxPlayers) {
        this(ip, port, motd, "Pre-47", "Pre-12w42b", onlinePlayers, maxPlayers);
    }

    protected MinecraftPingReply(final String ip, final int port, final String motd, final String protocolVersion, final String version, final int onlinePlayers, final int maxPlayers) {
        this.ip = ip;
        this.port = port;
        this.motd = motd;
        this.protocolVersion = protocolVersion;
        this.version = version;
        this.maxPlayers = maxPlayers;
        this.onlinePlayers = onlinePlayers;
    }

    /**
     * Gets the server's IP
     * 
     * @return the server's IP
     */
    public String getIp() {
        return this.ip;
    }

    /** 
     * Gets the server's maximum player count
     * 
     * @return the server's maximum player count
     */
    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    /**
     * Gets the server's MOTD
     * 
     * @return the server's MOTD
     */
    public String getMotd() {
        return this.motd;
    }

    /**
     * Gets the server's current online player count
     * 
     * @return the server's current online player count
     */
    public int getOnlinePlayers() {
        return this.onlinePlayers;
    }

    /**
     * Gets the server's port
     * 
     * @return the server's port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Gets the server's protocol version
     * 
     * @return the server's protocol version
     */
    public String getProtocolVersion() {
        return this.protocolVersion;
    }

    /**
     * Gets the server's game version
     * 
     * @return the server's game version
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Returns a JSON representation of the data contained within this ping reply.
     */
    @Override
    public String toString() {
        return String.format("{\"ip\":\"%s\",\"port\":%s,\"maxPlayers\":%s,\"onlinePlayers\":%s,\"motd\":\"%s\",\"protocolVersion\":\"%s\",\"serverVersion\":\"%s\"}", this.getIp(), this.getPort(), this.getMaxPlayers(), this.getOnlinePlayers(), this.getMotd(), this.getProtocolVersion(), this.getVersion());
    }
}
