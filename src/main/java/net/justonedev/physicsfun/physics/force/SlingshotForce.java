package net.justonedev.physicsfun.physics.force;

import net.justonedev.physicsfun.base.Entity;
import net.justonedev.physicsfun.base.Vector2D;
import net.justonedev.physicsfun.physics.Force;

public class SlingshotForce extends Force {
    public SlingshotForce(Entity target, Vector2D direction, double decayFactor) {
        super(target, direction, decayFactor);
    }
}
