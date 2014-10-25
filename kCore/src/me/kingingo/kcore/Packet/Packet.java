package me.kingingo.kcore.Packet;

public abstract class Packet {
	public abstract Packet create(String[] packet);
	public abstract String getName();
	public abstract String toString();
	public abstract void Set(String[] split);
	public abstract void Set(String split);
}
