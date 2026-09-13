package net.justonedev.physicsfun.physics;

import lombok.Getter;
import lombok.Setter;
import net.justonedev.physicsfun.base.Entity;
import net.justonedev.physicsfun.base.Tickable;
import net.justonedev.physicsfun.base.Vector2D;

import java.util.Objects;

@Getter
public class Force implements Tickable {
    private final Vector2D decayRate; // constant
    private final double decayFactor; // linear (scales with value)

    private Vector2D force;
    @Setter
    private Entity target;

    private final long id;

    //region Constructors

    public Force(Vector2D force) {
        this(null, force, new Vector2D(Vector2D.ZERO), 1);
    }

    public Force(Vector2D initialForce, Vector2D constantDecay) {
        this(null, initialForce, constantDecay, 1);
    }

    public Force(Vector2D initialForce, double decayFactor) {
        this(null, initialForce, new Vector2D(Vector2D.ZERO), decayFactor);
    }

    public Force(Vector2D initialForce, Vector2D constantDecay, double decayFactor) {
        this(null, initialForce, constantDecay, decayFactor);
    }

    public Force(Entity target, Vector2D force) {
        this(target, force, new Vector2D(Vector2D.ZERO), 1);
    }

    public Force(Entity target, Vector2D initialForce, Vector2D constantDecay) {
        this(target, initialForce, constantDecay, 1);
    }

    public Force(Entity target, Vector2D initialForce, double decayFactor) {
        this(target, initialForce, new Vector2D(Vector2D.ZERO), decayFactor);
    }

    public Force(Entity target, Vector2D initialForce, Vector2D constantDecay, double decayFactor) {
        this.target = target;
        this.force = initialForce;
        this.decayRate = constantDecay;
        this.decayFactor = 1d / decayFactor;
        this.id = (long) (Math.random() * System.nanoTime());
    }

    //endregion

    @Override
    public void tick() {
        // only tick if there is a target
        if (target == null) return;
        target.applyForceTick(this);
        decayTick();
    }

    protected void decayTick() {
        this.getForce().subtract(getDecayRate());
        this.getForce().multiply(getDecayFactor());
    }

    public boolean isNone() {
        return this.getForce().isQuasiZero();
    }

    protected void setForce(Vector2D force) {
        this.force = force;
    }

    protected void setForceValues(double x, double y) {
        this.getForce().setX(x);
        this.getForce().setY(y);
    }


    public boolean isEqualForce(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Force force1 = (Force) o;
        return Double.compare(decayFactor, force1.decayFactor) == 0 && id == force1.id && Objects.equals(decayRate, force1.decayRate) && Objects.equals(force, force1.force) && Objects.equals(target, force1.target);
    }

    // Multiple forces with the same values can apply to one entity, but not the same force object multiple times.
    // This may be changed later

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Force force = (Force) o;
        return id == force.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Force[%s, %s, %f]".formatted(getForce(), getDecayRate(), getDecayFactor());
    }
}
