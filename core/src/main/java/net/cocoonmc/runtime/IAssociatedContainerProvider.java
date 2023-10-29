package net.cocoonmc.runtime;

public interface IAssociatedContainerProvider extends IAssociatedContainer {

    IAssociatedContainer getAssociatedContainer();

    default <T> T getAssociatedObject(IAssociatedContainerKey<T> key) {
        return getAssociatedContainer().getAssociatedObject(key);
    }

    default <T> void setAssociatedObject(IAssociatedContainerKey<T> key, T value) {
        getAssociatedContainer().setAssociatedObject(key, value);
    }
}
