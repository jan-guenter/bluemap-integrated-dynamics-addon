/*
 * SPDX-License-Identifier: MIT
 */
package io.github.janguenter.bluemap.integrateddynamics.adapter.bluemap523;

import de.bluecolored.bluemap.core.world.mca.blockentity.MCABlockEntity;
import de.bluecolored.bluenbt.NBTName;

/** BlueNBT projection of only the stable multipart visual fields. */
public final class MultipartBlockEntityData extends MCABlockEntity {

    private Object connected;

    @NBTName("forceDisconnected")
    private Object forceDisconnected;

    @NBTName("partContainer")
    private Object partContainer;

    @NBTName("facadeBlockTag")
    private Object facadeBlockTag;

    @NBTName("realCable")
    private Object realCable;

    public MultipartBlockEntityData() {
    }

    Object connected() {
        return connected;
    }

    Object forceDisconnected() {
        return forceDisconnected;
    }

    Object partContainer() {
        return partContainer;
    }

    Object facadeBlockTag() {
        return facadeBlockTag;
    }

    Object realCable() {
        return realCable;
    }
}
