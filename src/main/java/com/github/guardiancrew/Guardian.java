package com.github.guardiancrew;

import com.github.guardiancrew.command.Command;
import com.github.guardiancrew.deserialization.DefaultDeserializers;
import com.github.guardiancrew.deserialization.Deserializers;
import com.github.guardiancrew.punishment.Punishment;
import com.github.guardiancrew.util.FriendlyByteBuf;
import com.github.guardiancrew.util.Reflect;
import com.github.guardiancrew.util.Version;
import com.github.guardiancrew.wrapper.GuardianAdapter;
import com.github.guardiancrew.wrapper.GuardianPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Guardian extends JavaPlugin {

    private static Guardian instance;
    private static JarFile jarFile;
    private static Version minecraftVersion;

    private final File SAVE_FILE = new File(getDataFolder(), "players.dat");

    public static Guardian getInstance() {
        return instance;
    }

    @Override
    public File getFile() {
        return super.getFile();
    }

    public JarFile getJarFile() {
        return jarFile;
    }

    @Override
    public void onLoad() {
        try {
            jarFile = new JarFile(getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        updateMinecraftVersion();
        initDataFolder();
    }

    @Override
    public void onEnable() {
        instance = this;

        new DefaultDeserializers();
        try {
            deserializeData();
        } catch (IOException e) {
            getLogger().severe("Could not deserialize data");
            throw new RuntimeException(e);
        }

        registerListeners();
        registerCommands();

        Bukkit.getOnlinePlayers().forEach(GuardianAdapter::wrapPlayer);
    }

    @Override
    public void onDisable() {
        try {
            serializeData();
        } catch (IOException e) {
            getLogger().severe("Could not serialize data");
            throw new RuntimeException(e);
        }
    }

    private static final Pattern bukkitVersionPattern = Pattern.compile("\\d+\\.\\d+(?:\\.\\d+)?");

    private static void updateMinecraftVersion() {
        String bukkitVersion = Bukkit.getBukkitVersion();
        Matcher matcher = bukkitVersionPattern.matcher(bukkitVersion);
        if (matcher.find())
            minecraftVersion = new Version(matcher.group());
    }

    public static Version getMinecraftVersion() {
        return minecraftVersion;
    }

    public static boolean isRunningMinecraft(int major, int minor) {
        return minecraftVersion.compareTo(major, minor) >= 0;
    }

    public static boolean isRunningMinecraft(int major, int minor, int patch) {
        return minecraftVersion.compareTo(major, minor, patch) >= 0;
    }

    public static boolean isRunningMinecraft(Version version) {
        return minecraftVersion.compareTo(version) >= 0;
    }

    private void registerListeners() {
        try {
            for (Class<? extends Listener> c : Reflect.getClasses("com.github.guardiancrew.listeners", Listener.class)) {
                Listener listener = c
                        .getDeclaredConstructor()
                        .newInstance();
               getServer().getPluginManager().registerEvents(listener, this);
            }
        } catch (ClassNotFoundException | URISyntaxException | IOException | NoSuchMethodException |
                 InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerCommands() {
        try {
            Reflect.getClasses("com.github.guardiancrew.command.commands", Command.class);
        } catch (ClassNotFoundException | URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void initDataFolder() {
        if (getDataFolder().exists())
            return;
        getDataFolder().mkdir();
    }

    private void serializeData() throws IOException {
        FriendlyByteBuf buf = new FriendlyByteBuf();
        // -=-=-=-=-=- Players -=-=-=-=-=-
        Collection<GuardianPlayer> players = GuardianAdapter.getAllPlayers();
        buf.writeVarInt(players.size());
        for (GuardianPlayer player : players)
            buf.write(player);

        // -=-=-=-=- Punishments -=-=-=-=-
        for (GuardianPlayer player : players) {
            List<Punishment> punishments = player.getPunishments();
            buf.writeVarInt(punishments.size());
            for (Punishment punishment : punishments)
                buf.write(punishment);
        }

        buf.writeToFile(SAVE_FILE);
    }

    private void deserializeData() throws IOException {
        if (!SAVE_FILE.exists())
            return;
        FriendlyByteBuf buf = new FriendlyByteBuf(SAVE_FILE);
        int size = buf.readVarInt();
        for (int i = 0; i < size; i++)
            Deserializers.deserialize(buf, GuardianPlayer.class);

        for (int i = 0; i < size; i++) {
            int punishmentsSize = buf.readVarInt();
            for (int j = 0; j < punishmentsSize; j++) {
                Punishment punishment = Deserializers.deserialize(buf, Punishment.class);
                punishment.getTarget().addPunishment(punishment);
            }
        }
    }

}
