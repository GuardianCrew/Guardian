package com.github.guardiancrew.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FriendlyByteBuf {

    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    @NotNull
    private final ByteBuf buf;

    public FriendlyByteBuf() {
        this(new byte[0]);
    }

    public FriendlyByteBuf(byte @NotNull [] bytes) {
        buf = Unpooled.buffer(0);
        buf.writeBytes(bytes);
    }

    public FriendlyByteBuf(File file) throws IOException {
        this(Files.readAllBytes(file.toPath()));
    }

    public FriendlyByteBuf(@NotNull ByteBuf buf) {
        this.buf = buf;
    }

    public byte @NotNull [] bytes() {
        int length = buf.writerIndex();
        int reader = buf.readerIndex();
        buf.readerIndex(0);
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++)
            bytes[i] = readByte();
        buf.readerIndex(reader);
        return bytes;
    }

    public byte @NotNull [] finish() {
        int length = buf.writerIndex() - buf.readerIndex();
        buf.readerIndex(0);
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++)
            bytes[i] = readByte();
        return bytes;
    }

    public @NotNull DataOutputStream stream() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(buffer);
        return writeToStream(stream);
    }

    public @NotNull DataOutputStream writeToStream(@NotNull DataOutputStream stream) throws IOException {
        stream.write(bytes());
        return stream;
    }

    public void writeToFile(File file) throws IOException {
        try (FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(finish());
        }
    }

    public @NotNull FriendlyByteBuf write(@NotNull Writable writable) {
        writable.write(this);
        return this;
    }

    public boolean readBoolean() {
        return buf.readBoolean();
    }

    public @NotNull FriendlyByteBuf writeBoolean(boolean value) {
        buf.writeBoolean(value);
        return this;
    }

    public byte readByte() {
        return buf.readByte();
    }

    public @NotNull FriendlyByteBuf writeByte(byte value) {
        buf.writeByte(value);
        return this;
    }

    public byte @NotNull [] readBytes(int length) {
        final byte[] result = new byte[length];
        for (int i = 0; i < length; i++)
            result[i] = readByte();
        return result;
    }

    public @NotNull FriendlyByteBuf writeBytes(byte @NotNull ... bytes) {
        buf.writeBytes(bytes);
        return this;
    }

    public byte @NotNull [] readByteArray() {
        int length = readVarInt();
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++)
            bytes[i] = readByte();
        return bytes;
    }

    public @NotNull FriendlyByteBuf writeByteArray(byte @NotNull [] bytes) {
        writeVarInt(bytes.length);
        for (byte b : bytes)
            writeByte(b);
        return this;
    }

    public short readShort() {
        return buf.readShort();
    }

    public @NotNull FriendlyByteBuf writeShort(short value) {
        buf.writeShort(value);
        return this;
    }

    public int readInt() {
        return buf.readInt();
    }

    public @NotNull FriendlyByteBuf writeInt(int value) {
        buf.writeInt(value);
        return this;
    }

    public long readLong() {
        return buf.readLong();
    }

    public @NotNull FriendlyByteBuf writeLong(long value) {
        buf.writeLong(value);
        return this;
    }

    public long[] readLongArray() {
        int length = readVarInt();
        long[] longs = new long[length];
        for (int i = 0; i < length; i++)
            longs[i] = readLong();
        return longs;
    }

    public @NotNull FriendlyByteBuf writeLongArray(long @NotNull [] longs) {
        writeVarInt(longs.length);
        for (long l : longs)
            writeLong(l);
        return this;
    }

    public float readFloat() {
        return buf.readFloat();
    }

    public @NotNull FriendlyByteBuf writeFloat(float value) {
        buf.writeFloat(value);
        return this;
    }

    public double readDouble() {
        return buf.readDouble();
    }

    public @NotNull FriendlyByteBuf writeDouble(double value) {
        buf.writeDouble(value);
        return this;
    }

    public int readVarInt() {
        int value = 0;
        int position = 0;
        byte currentByte;
        while (true) {
            currentByte = readByte();
            value |= (currentByte & SEGMENT_BITS) << position;
            if ((currentByte & CONTINUE_BIT) == 0) break;
            position += 7;
            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }
        return value;
    }

    public @NotNull FriendlyByteBuf writeVarInt(int value) {
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                writeByte((byte) value);
                return this;
            }
            writeByte((byte) ((value & SEGMENT_BITS) | CONTINUE_BIT));
            value >>>= 7;
        }
    }

    public int @NotNull [] readVarIntArray() {
        int length = readVarInt();
        int[] ints = new int[length];
        for (int i = 0; i < length; i++)
            ints[i] = readVarInt();
        return ints;
    }

    public @NotNull FriendlyByteBuf writeVarIntArray(int @NotNull [] ints) {
        writeVarInt(ints.length);
        for (int i : ints)
            writeVarInt(i);
        return this;
    }

    public long readVarLong() {
        long value = 0;
        int position = 0;
        byte currentByte;
        while (true) {
            currentByte = readByte();
            value |= (long) (currentByte & SEGMENT_BITS) << position;
            if ((currentByte & CONTINUE_BIT) == 0) break;
            position += 7;
            if (position >= 64) throw new RuntimeException("VarLong is too big");
        }
        return value;
    }

    public @NotNull FriendlyByteBuf writeVarLong(long value) {
        while (true) {
            if ((value & ~((long) SEGMENT_BITS)) == 0) {
                writeByte((byte) value);
                return this;
            }
            writeByte((byte) ((value & SEGMENT_BITS) | CONTINUE_BIT));
            value >>>= 7;
        }
    }

    public @NotNull String readString() {
        final int length = readVarInt();
        if (length < 0) throw new IllegalStateException();
        final byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++)
            bytes[i] = buf.readByte();
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public @NotNull FriendlyByteBuf writeString(@NotNull String value) {
        final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        writeVarInt(bytes.length);
        buf.writeBytes(bytes);
        return this;
    }

    public @NotNull List<String> readStringList() {
        final List<String> strings = new ArrayList<>();
        int length = readVarInt();
        for (int i = 0; i < length; i++)
            strings.add(readString());
        return strings;
    }

    public @NotNull FriendlyByteBuf writeStringList(@NotNull List<String> strings) {
        writeVarInt(strings.size());
        for (String string : strings)
            writeString(string);
        return this;
    }

    public @NotNull UUID readUUID() {
        return new UUID(readLong(), readLong());
    }

    public @NotNull FriendlyByteBuf writeUUID(@NotNull UUID uuid) {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
        return this;
    }

    public @NotNull Instant readInstant() {
        return Instant.ofEpochSecond(readLong(), readInt());
    }

    public @NotNull FriendlyByteBuf writeInstant(Instant instant) {
        writeLong(instant.getEpochSecond());
        writeInt(instant.getNano());
        return this;
    }

}
