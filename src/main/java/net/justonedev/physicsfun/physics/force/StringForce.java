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
import java.util.Collection;

public class StringForce extends Force implements Renderable {

    // IDEA: This is like attaching it to a string that is attached at a given location.
    // If the object is closer to this location, everything is fine. If further away than
    // the length of the string, it bounces back with tremendous force (the string cannot tear).

    // Idea is like a pendulum

    private static final double DEFAULT_STRENGTH_VALUE = 1;
    private static final double DISTANCE_DAMPING_FACTOR = 0.5;

    private final Vector2D location;
    private final double strength;
    private final double stringLength;

    // This force scales with distance and pulls towards a point
    public StringForce(Vector2D targetLocation, double stringLength) {
        this(targetLocation, stringLength, DEFAULT_STRENGTH_VALUE);
    }

    public StringForce(Vector2D targetLocation, double stringLength, double strength) {
        super(new Vector2D(0, 0));
        this.location = targetLocation;
        this.strength = strength;
        this.stringLength = stringLength;
    }

    @Override
    public Vector2D getForce(Entity entity) {
        Vector2D position = getTargetLocationCopy(entity);
        Vector2D displacement = new Vector2D(position).subtract(location).invert();

        double distance = displacement.length();
        if (distance <= stringLength) return Vector2D.ZERO;

        // TODO wrong, direction should be in direction of circle, not center or something
        // or actually a hard cap on the distance
        displacement.multiply(1d / distance);
        displacement.multiply(distance - stringLength);

        Vector2D force = displacement.multiply(strength * DISTANCE_DAMPING_FACTOR);
        System.out.println(force + "@" + position);
        // already scaled with distance
        return force;
    }

    @Override
    public boolean isNone() {
        return false;
    }

    private Vector2D getTargetLocationCopy(Entity target) {
        if (target == null) return new Vector2D(0, 0);
        if (target instanceof RectangularEntity)
            // order does not matter because distance squares, and getCenter creates a new vector anyway
            return ((RectangularEntity) target).getCenter();
        return new Vector2D(target.getLocation());
    }

    private double getDistanceSquared(Entity target) {
        if (target == null) return 0;
        if (target instanceof RectangularEntity)
            // order does not matter because distance squares, and getCenter creates a new vector anyway
            return ((RectangularEntity) target).getCenter().subtract(location).lengthSquared();
        return new Vector2D(location).subtract(target.getLocation()).lengthSquared();
    }

    private double getDistanceSquared2(Entity target) {
        // more efficient approach that does not create a new vector object per call
        if (target == null) return 0;
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
        if (lastApplied != null && !lastApplied.isEmpty()) {
            graphics.setStroke(new BasicStroke(3));
            Vector2D center;
            for (Entity entity : lastApplied) {
                center = entity.getCenter();
                graphics.drawLine(center.getIntX(), center.getIntY(), location.getIntX(), location.getIntY());
            }
        }
        graphics.setStroke(new BasicStroke(10));
        graphics.fillOval(location.getIntX() - 8, location.getIntY() - 8, 16, 16);
        graphics.setStroke(previousStroke);
    }

    private Collection<Entity> lastApplied;

    @Override
    public void tickForTargets(Collection<Entity> targets) {
        super.tickForTargets(targets);
        lastApplied = targets;
    }
}
