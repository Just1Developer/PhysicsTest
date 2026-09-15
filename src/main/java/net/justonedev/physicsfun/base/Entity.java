package net.justonedev.physicsfun.base;

import lombok.Getter;
import lombok.Setter;
import net.justonedev.physicsfun.physics.Force;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class Entity implements Renderable, Tickable {
    private static final int SPEED_LIMIT = 150;
    public static Vector2D MAXIMUM_VELOCITY = new Vector2D(SPEED_LIMIT, SPEED_LIMIT);

    @Getter
    @Setter
    protected Vector2D location;
    @Getter
    @Setter
    protected Vector2D velocity;

    protected Set<Force> forces;
    protected List<Force> removedForces;

    private boolean tickingSuspended = false;

    public Entity() {
        this(0, 0);
    }

    public Entity(int x, int y) {
        this.setLocation(new Vector2D(x, y));
        this.setVelocity(new Vector2D(0, 0));
        forces = new HashSet<>();
        removedForces = new LinkedList<>();
    }

    @Override
    public void tick() {
        if (tickingSuspended) return;
        // How do we handle decay? Simple, not at all.
        // Decaying forces should simply not be added to multiple targets.
        this.forces.forEach(force -> force.tickForSingleTarget(this));
        removedForces.forEach(this.forces::remove);
        this.removedForces.clear();
        location.add(velocity);
        this.resolveSelfCollisions();
    }

    public void applyNewForce(Force force) {
        this.forces.add(force);
        force.addTarget(this);
    }

    public void applyForceTick(Force force) {
        if (force.addIndirectly()) this.velocity.add(force.getForce(this)).cap(MAXIMUM_VELOCITY);
        if (force.addDirectly()) this.location.add(force.getForce(this));
        if (force.isNone()) this.removedForces.add(force);
    }

    public void suspendTicking() {
        this.tickingSuspended = true;
    }

    public void unsuspendTicking() {
        this.tickingSuspended = false;
    }

    public void resolveSelfCollisions() {
        // later: resolves / fixes self position
    }

    public Vector2D getCenter() {
        return getLocation();
    }
}
