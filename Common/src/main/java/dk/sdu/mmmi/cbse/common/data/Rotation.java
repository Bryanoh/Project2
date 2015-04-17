package dk.sdu.mmmi.cbse.common.data;

import java.io.Serializable;

public final class Rotation implements Serializable {

    public float angle;

    public Rotation() {
    }

    public Rotation(float angle) {
        this.angle = angle;
    }

}
