package net.justonedev.physicsfun.physics.force;

import net.justonedev.physicsfun.base.Entity;
import net.justonedev.physicsfun.base.RectangularEntity;
import net.justonedev.physicsfun.base.Renderable;
import net.justonedev.physicsfun.base.Vector2D;
import net.justonedev.physicsfun.physics.Force;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class SpringForce extends Force implements Renderable {

    private static final double DEFAULT_SPRING_VALUE = 0.5;
    private static final double DEFAULT_DAMPING_VALUE = 0.2;

    private final Vector2D location;
    private final double strength;
    private final double damping;
    private boolean ticked;

    // This force scales with distance and pulls towards a point
    public SpringForce(Vector2D targetLocation) {
        this(targetLocation, DEFAULT_SPRING_VALUE, DEFAULT_DAMPING_VALUE);
    }

    public SpringForce(Vector2D targetLocation, double strength) {
        this(targetLocation, strength, DEFAULT_DAMPING_VALUE);
    }

    public SpringForce(Vector2D targetLocation, double strength, double damping) {
        super(new Vector2D(0, 0));
        this.ticked = false;
        this.location = targetLocation;
        this.strength = strength;
        this.damping = damping;
    }

    @Override
    protected void decayTick() {
        ticked = true;
        Entity entity = getTarget();
        Vector2D position = getTargetLocationCopy();
        Vector2D displacement = new Vector2D(position).subtract(location).invert();
        Vector2D force = displacement.multiply(strength);
        Vector2D dampingForce = new Vector2D(entity.getVelocity())
                .multiply(-damping);
        force.add(dampingForce);
        setForce(force);
    }

    @Override
    public boolean isNone() {
        return ticked && super.isNone();
    }

    private Vector2D getTargetLocationCopy() {
        if (getTarget() == null) return new Vector2D(0, 0);
        Entity target = getTarget();
        if (target instanceof RectangularEntity)
            // order does not matter because distance squares, and getCenter creates a new vector anyway
            return ((RectangularEntity) target).getCenter();
        return new Vector2D(target.getLocation());
    }

    private double getDistanceSquared() {
        if (getTarget() == null) return 0;
        Entity target = getTarget();
        if (target instanceof RectangularEntity)
            // order does not matter because distance squares, and getCenter creates a new vector anyway
            return ((RectangularEntity) target).getCenter().subtract(location).lengthSquared();
        return new Vector2D(location).subtract(target.getLocation()).lengthSquared();
    }

    private double getDistanceSquared2() {
        // more efficient approach that does not create a new vector object per call
        if (getTarget() == null) return 0;
        Entity target = getTarget();
        int centerX, centerY;
        if (target instanceof RectangularEntity) {
            // order does not matter because distance squares, and getCenter creates a new vector anyway
            centerX = ((RectangularEntity) target).getCenter().getIntX();
            centerY = ((RectangularEntity) target).getCenter().getIntY();
        } else {
            centerX = target.getLocation().getIntX();
            centerY = target.getLocation().getIntY();
        }
        // order of subtraction does not matter
        int distanceX = centerX - location.getIntX();
        int distanceY = centerY - location.getIntY();
        return distanceX * distanceX + distanceY * distanceY;
    }

    @Override
    public boolean addDirectly() {
        return true;
    }

    @Override
    public void render(Graphics2D graphics) {
        Stroke previousStroke = graphics.getStroke();
        graphics.setColor(Color.darkGray);
        graphics.setStroke(new BasicStroke(10));
        graphics.fillOval(location.getIntX(), location.getIntY(), 15, 15);
        graphics.setStroke(previousStroke);
    }
}
