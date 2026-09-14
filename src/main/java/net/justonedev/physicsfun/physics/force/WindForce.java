package net.justonedev.physicsfun.physics.force;

import net.justonedev.physicsfun.base.Entity;
import net.justonedev.physicsfun.base.Vector2D;
import net.justonedev.physicsfun.physics.Force;

public class WindForce extends Force {

    // TODO has strength, variance over ticks (how it evolves) and variance per get call, so not every get call is the same
    // TODO but, i should be studying right now, so I'm going to do this next time

    private static final double AIR_RESISTANCE_DEFAULT_STRENGTH = 0.1;

    private final double strength;

    public WindForce() {
        this(AIR_RESISTANCE_DEFAULT_STRENGTH);
    }

    public WindForce(double strength) {
        super(new Vector2D(0, 0));
        this.strength = strength;
    }

    @Override
    public Vector2D getForce(Entity entity) {
        if (entity == null) return getForce();
        return new Vector2D(entity.getVelocity()).invert().multiply(strength);
    }

    @Override
    public boolean isNone() {
        return false;
    }
}
