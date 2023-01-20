package com.github.guardiancrew.util;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Version implements Comparable<Version> {

    private final int[] version = new int[3];

    @Nullable
    private String postfix;

    public Version(int... version) {
        Preconditions.checkArgument(version.length >= 2 && version.length <= 3);
        System.arraycopy(version, 0, this.version, 0, version.length);
    }

    public Version(int major, int minor, int patch, @Nullable String postfix) {
        this(major, minor, postfix);
        this.version[2] = patch;
    }

    public Version(int major, int minor, @Nullable String postfix) {
        this.version[0] = major;
        this.version[1] = minor;
        this.postfix = postfix;
    }

    public final static Pattern versionPattern = Pattern.compile("(\\d)\\.(\\d)(?:\\.(\\d))?\\s*(.*)");

    public Version(String version) {
        Matcher matcher = versionPattern.matcher(version);
        if (!matcher.matches())
            throw new IllegalArgumentException(String.format("'%s' is not a valid version string", version));
        for (int i = 1; i <= 3; i++) {
            String group = matcher.group(i);
            if (group != null)
                this.version[i - 1] = Integer.parseInt(group);
        }
        if (matcher.group(4).isEmpty())
            postfix = matcher.group(4);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Version))
            return false;
        return compareTo((Version) obj) == 0;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(version);
        result = 31 * result + (postfix != null ? postfix.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(@NotNull Version other) {
        for (int i = 0; i < version.length; i++) {
            if (version[i] > other.version[i])
                return 1;
            if (version[i] < other.version[i])
                return -1;
        }
        if (postfix == null)
            return other.postfix == null ? 0 : -1;
        return other.postfix == null ? 1 : postfix.compareTo(other.postfix);
    }

    public int compareTo(int... version) {
        return compareTo(new Version(version));
    }

    public boolean isSmallerThan(Version other) {
        return compareTo(other) < 0;
    }

    public boolean isLargerThan(Version other) {
        return compareTo(other) > 0;
    }

    public int getMajor() {
        return version[0];
    }

    public int getMinor() {
        return version[1];
    }

    public int getPatch() {
        return version.length == 2 ? 0 : version[2];
    }

    public boolean isStable() {
        return postfix == null;
    }

    @Override
    public String toString() {
        return version[0] + "." + version[1] + (version.length == 2 ? "" : version[2]) + (postfix == null ? "" : (postfix.startsWith("-") ? postfix : " " + postfix));
    }

    public static int compare(String first, String second) {
        return new Version(first).compareTo(new Version(second));
    }

}
