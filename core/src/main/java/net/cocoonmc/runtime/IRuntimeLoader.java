package net.cocoonmc.runtime;


import org.bukkit.Bukkit;

/**
 * Matches the server's NMS version to its {@link IRuntime}
 */
public interface IRuntimeLoader {

    /**
     * Matches the server version to it's {@link IRuntime}
     *
     * @return The {@link IRuntime} for this server
     * @throws IllegalStateException If the version wrapper failed to be instantiated or is unable to be found
     */
    static IRuntime load() {
        final String serverVersion = Bukkit.getServer()
                .getClass()
                .getPackage()
                .getName()
                .split("\\.")[3];
        try {
            return (IRuntime) Class.forName(IRuntime.class.getPackage().getName() + "." + serverVersion + ".RuntimeFactory")
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException(
                    "Cocoon does not support server version \"" + serverVersion + "\"", exception);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(
                    "Failed to instantiate version wrapper for version " + serverVersion, exception);
        }
    }
}
