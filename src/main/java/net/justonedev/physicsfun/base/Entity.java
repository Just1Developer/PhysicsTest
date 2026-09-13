package net.justonedev.physicsfun.base;

import lombok.Getter;
import lombok.Setter;
import net.justonedev.physicsfun.physics.Force;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class Entity implements Renderable, Tickable {
    @Getter
    @Setter
    protected Vector2D location;

    protected Set<Force> forces;
    protected List<Force> removedForces;

    public Entity() {
        this(0, 0);
    }

    public Entity(int x, int y) {
        this.setLocation(new Vector2D(x, y));
        forces = new HashSet<>();
        removedForces = new LinkedList<>();
    }

    @Override
    public void tick() {
        this.forces.forEach(Force::tick);
        removedForces.forEach(this.forces::remove);
        this.removedForces.clear();
    }

    public void applyNewForce(Force force) {
        this.forces.add(force);
        force.setTarget(this);
    }

    public void applyForceTick(Force force) {
        this.location.add(force.getForce());
        if (force.isNone()) this.removedForces.add(force);
    }
}
