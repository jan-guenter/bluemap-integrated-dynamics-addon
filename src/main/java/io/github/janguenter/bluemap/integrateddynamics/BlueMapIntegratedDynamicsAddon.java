/*
 * SPDX-License-Identifier: MIT
 */
package io.github.janguenter.bluemap.integrateddynamics;

import io.github.janguenter.bluemap.addon.adapter.api.bluemap523.BlueMapRuntimeCompatibility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** BlueMap add-on entrypoint installed before resource-pack construction. */
public final class BlueMapIntegratedDynamicsAddon implements Runnable {

    @Override
    public void run() {
        try {
            if (!BlueMapRuntimeCompatibility.matchesCurrent()) {
                inactive("unsupported BlueMap internal ABI", null);
                return;
            }
            Class<?> adapter = Class.forName(
                    "io.github.janguenter.bluemap.integrateddynamics.adapter.bluemap523.BlueMap523Adapter",
                    true,
                    BlueMapIntegratedDynamicsAddon.class.getClassLoader()
            );
            Method install = adapter.getMethod("install");
            install.invoke(null);
        } catch (InvocationTargetException exception) {
            inactive("adapter initialization failed", exception.getCause());
        } catch (ReflectiveOperationException | LinkageError | RuntimeException exception) {
            inactive("adapter unavailable", exception);
        }
    }

    private static void inactive(String reason, Throwable cause) {
        String detail = cause == null ? "" : " (" + cause.getClass().getSimpleName() + ")";
        System.err.println("BlueMap Integrated Dynamics add-on is inactive: " + reason + detail + ".");
    }
}
