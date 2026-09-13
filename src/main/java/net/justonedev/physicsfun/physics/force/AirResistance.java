package net.justonedev.physicsfun.physics.force;

import net.justonedev.physicsfun.base.Vector2D;
import net.justonedev.physicsfun.physics.Force;

public class AirResistance extends Force {
    private static final double AIR_RESISTANCE_DEFAULT_STRENGTH = 0.1;

    private final double strength;

    public AirResistance() {
        this(AIR_RESISTANCE_DEFAULT_STRENGTH);
    }

    public AirResistance(double strength) {
        super(new Vector2D(0, 0));
        this.strength = strength;
    }

    @Override
    protected void decayTick() {
        Vector2D opposingForce = new Vector2D(getTarget().getVelocity()).invert().multiply(strength);
        super.setForce(opposingForce);
    }

    @Override
    public boolean isNone() {
        return false;
    }
}
