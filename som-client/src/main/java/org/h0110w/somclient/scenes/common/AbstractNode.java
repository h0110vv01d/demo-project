package org.h0110w.somclient.scenes.common;

public abstract class AbstractNode<T, SELF> {
    protected T node;
    public abstract SELF initialize();
    public T getNode() {
        return node;
    }
}
