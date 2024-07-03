package be.raft.creatio.server.world;

import be.raft.creatio.CreatioServer;

import java.io.File;

public class WorldManager {
    private final CreatioServer server;
    private final File saveFile;

    public WorldManager(CreatioServer server) {
        this.server = server;

        this.saveFile = new File(server.settings().worldName());
        if (!this.saveFile.exists())
            this.saveFile.mkdirs();

        
    }
}
