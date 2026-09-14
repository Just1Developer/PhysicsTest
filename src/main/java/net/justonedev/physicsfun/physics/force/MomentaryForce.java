package net.justonedev.physicsfun.physics.force;

import net.justonedev.physicsfun.base.Entity;
import net.justonedev.physicsfun.base.Vector2D;
import net.justonedev.physicsfun.physics.Force;

public class MomentaryForce extends Force {
    public MomentaryForce(Entity target, Vector2D direction) {
        super(target, direction, 0);
    }
}
