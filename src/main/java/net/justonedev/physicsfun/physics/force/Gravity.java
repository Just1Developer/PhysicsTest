package net.justonedev.physicsfun.physics.force;

import net.justonedev.physicsfun.base.Vector2D;
import net.justonedev.physicsfun.physics.Force;

public class Gravity extends Force {
    public Gravity(double downwardStrength) {
        this(new Vector2D(0, downwardStrength));
    }
    public Gravity(double downwardStrength, double sidewaysStrength) {
        this(new Vector2D(sidewaysStrength, downwardStrength));
    }
    public Gravity(Vector2D pull) {
        super(pull);
    }

    @Override
    protected void decayTick() {
        // regular gravity has no decay
    }

    @Override
    public boolean isNone() {
        return false;
    }
}
