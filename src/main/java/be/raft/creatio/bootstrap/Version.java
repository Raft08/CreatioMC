/*
  AtlasWorld's Proprietary License

  Copyright (c) 2022 - 2024 AtlasWorld Studio. All Rights Reserved.

  This software is proprietary to AtlasWorld Studio and may only be used internally
  within the organization obtaining the software. Any commercial use, copying, modification,
  distribution, or exploitation of the software requires express written permission from AtlasWorld Studio.
*/
package be.raft.creatio.bootstrap;

import fr.atlasworld.common.logging.LogUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;

public class Version {
    private static final StackWalker CLASS_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String FILENAME = "build.properties";
    // Singleton
    private static Version instance;
    private final Properties file;

    private Version() {
        this.file = new Properties();

        this.defaults();
        this.load();
    }

    public static Version getInstance() {
        if (instance == null)
            instance = new Version();

        return instance;
    }

    private void defaults() {
        this.file.put("version", "Undefined");
        this.file.put("commit", "Undefined");
        this.file.put("branch", "Undefined");
        this.file.put("build_time", new Date(0));
    }

    public void load() {
        ClassLoader loader = CLASS_WALKER.getCallerClass().getClassLoader();

        try {
            InputStream stream = loader.getResourceAsStream(FILENAME);

            if (stream == null) {
                LOGGER.warn("Could not load '{}'", FILENAME);
                return;
            }

            InputStreamReader reader = new InputStreamReader(stream);
            this.file.load(reader);

            reader.close();
            stream.close();
        } catch (IOException e) {
            LOGGER.warn("Failed to load '{}'", FILENAME, e);
        }
    }

    public String getVersion() {
        return (String) this.file.get("version");
    }

    public String getCommit() {
        return (String) this.file.get("commit");
    }

    public String getBranch() {
        return (String) this.file.get("branch");
    }

    public Date getBuildTime() {
        try {
            return (Date) this.file.get("build_time");
        } catch (ClassCastException e) {
            return new Date(Long.parseLong((String) this.file.get("build_time")));
        }
    }

    @Override
    public String toString() {
        return this.getVersion() + " (" + this.getBranch() + ": " + this.getCommit().substring(0, 7) + ")";
    }
}
