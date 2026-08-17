/*
 * SPDX-License-Identifier: MIT
 */
package io.github.janguenter.bluemap.integrateddynamics.activation;

/** Shared activation state for the accepted Integrated Dynamics family route. */
public final class IntegratedDynamicsRuntime {

    public static final IntegratedDynamicsRuntime INSTANCE = new IntegratedDynamicsRuntime();

    private final RouteActivation route = new RouteActivation("integrated-dynamics-family");

    private IntegratedDynamicsRuntime() {
    }

    public RouteActivation route() {
        return route;
    }

    public void disable(String detail) {
        route.fail(detail);
    }
}
