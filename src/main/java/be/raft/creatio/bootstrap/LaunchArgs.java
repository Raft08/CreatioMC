/*
  AtlasWorld's Proprietary License

  Copyright (c) 2022 - 2024 AtlasWorld Studio. All Rights Reserved.

  This software is proprietary to AtlasWorld Studio and may only be used internally
  within the organization obtaining the software. Any commercial use, copying, modification,
  distribution, or exploitation of the software requires express written permission from AtlasWorld Studio.
*/
package be.raft.creatio.bootstrap;

import fr.atlasworld.common.logging.Level;
import fr.atlasworld.common.logging.LogUtils;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LaunchArgs {
    private static final OptionParser OPTION_PARSER = new OptionParser() {
        {
            // Informative
            acceptsAll(asList("version", "v"), "Displays the server version.");
            acceptsAll(asList("help", "?"), "Lists every possible arguments and their usages.");

            // Dev
            acceptsAll(asList("debug"), "Launches the server with debug/trace logging enabled.");

            // Plugins
            // TODO: Make the add-plugin argument flag work.
            /* acceptsAll(asList("add-plugin"), "Add a plugin file to the server.")
                    .withRequiredArg()
                    .ofType(File.class)
                    .defaultsTo(new File[] {})
                    .describedAs("Plugin Jar File");
            */

            acceptsAll(asList("plugins"), "Sets the directory to look for plugins.")
                    .withRequiredArg()
                    .ofType(File.class)
                    .defaultsTo(new File("plugins"))
                    .describedAs("Directory");
        }
    };
    public static LaunchArgs instance;
    private final OptionSet launchOpt;

    private LaunchArgs(OptionSet launchOpt) {
        this.launchOpt = launchOpt;
    }

    public static LaunchArgs parse(String[] args) {
        OptionSet options = null;

        try {
            options = OPTION_PARSER.parse(args);
        } catch (OptionException e) {
            System.err.println("\nFailed to start AtlasNetwork: " + e.getMessage());
            System.err.println("Use '--help' or '--?' to show help menu.\n");
            System.exit(-1);
        }

        if (options.has("help"))
            printHelp();

        if (options.has("version"))
            printVersion();

        if (options.has("debug"))
            LogUtils.setGlobalLevel(Level.TRACE);

        instance = new LaunchArgs(options);
        return get();
    }

    private static void printVersion() {
        long compileTime = Version.getInstance().getBuildTime().getTime();
        long currentTime = System.currentTimeMillis();

        long differenceTime = currentTime - compileTime;

        System.out.println();
        System.out.println(Version.getInstance());
        System.out.println("Compiled " + getTimeAgo(differenceTime) + " ago");
        System.out.println();

        System.exit(0);
    }

    private static void printHelp() {
        try {
            System.out.println(); // Spacing
            LaunchArgs.OPTION_PARSER.printHelpOn(System.out);
            System.out.println(); // Spacing
        } catch (IOException e) {
            System.err.println("Failed to print help menu.");
            e.printStackTrace(System.err);
        }
        System.exit(0);
    }

    private static String getTimeAgo(long timeDifference) {
        long days = TimeUnit.DAYS.convert(timeDifference, TimeUnit.MILLISECONDS);
        long hours = TimeUnit.HOURS.convert(timeDifference, TimeUnit.MILLISECONDS) % 24;
        long minutes = TimeUnit.MINUTES.convert(timeDifference, TimeUnit.MILLISECONDS) % 60;

        if (days > 0)
            return days + " days";
        else if (hours > 0)
            return hours + " hours";
        else if (minutes > 0)
            return minutes + " minutes";
        else return "less than a minute";
    }

    public static LaunchArgs get() {
        if (instance == null)
            throw new IllegalStateException("Arguments not initialized!");

        return instance;
    }

    public boolean debugMode() {
        return this.launchOpt.has("debug");
    }

    public File pluginDirectory() {
        return (File) this.launchOpt.valueOf("plugins");
    }

    @SuppressWarnings("unchecked")
    public List<Path> addedPluginFiles() {
        return ((List<File>) this.launchOpt.valueOf("add-plugin")).stream().map(File::toPath).toList();
    }

    private static List<String> asList(String... args) {
        return List.of(args);
    }
}
