package eu.sqrt5.nitro.core;

public abstract class Dumpable {
    abstract void dump(int indent);

    public void dump() {
        dump(0);
    }
}
