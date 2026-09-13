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

public class DirectedAttractionForce extends Force implements Renderable {

    private static final double DISTANCE_SCALAR = 1;
    private static final double DISTANCE_INVERSE_WEIGHT = 2;
    private static final double DISTANCE_SQUARED_WEIGHT = 5;

    private final Vector2D location;
    private final double baseAttraction;
    private final double strength;
    private boolean ticked;

    // This force scales with distance and pulls towards a point
    public DirectedAttractionForce(Vector2D targetLocation, double strength) {
        this(targetLocation, strength, 0);
    }
    public DirectedAttractionForce(Vector2D targetLocation, double strength, double baseAttraction) {
        super(new Vector2D(0, 0));
        this.ticked = false;
        this.location = targetLocation;
        this.strength = strength;
        this.baseAttraction = baseAttraction;
    }

    @Override
    protected void decayTick() {
        ticked = true;
        // we can assume target to be != null
        double distanceSquared = getDistanceSquared2();
        double distanceSquaredFactor = (1 / (Math.max(1, distanceSquared))) * DISTANCE_SQUARED_WEIGHT;

        // do like this because one case of target loc creates a copy anyway, so minimize copies
        Vector2D targetDirection = getTargetLocationCopy().subtract(location).invert();
        double vectorLength = targetDirection.length();
        double distanceFactor = (1 / (DISTANCE_SCALAR * (Math.max(1, distanceSquared)))) * DISTANCE_INVERSE_WEIGHT;

        double factorSum = distanceFactor + distanceSquaredFactor + baseAttraction;
        double finalFactor = (factorSum * strength) / (Math.max(1, vectorLength));
        System.out.printf("Target Direction: %s, final factor: %f, distanceFactor %f = (1 / (%f * %f)) * %f %n", targetDirection, finalFactor, distanceFactor, DISTANCE_SCALAR, (vectorLength + 1), DISTANCE_INVERSE_WEIGHT);
        targetDirection.multiply(finalFactor);
        super.setForce(targetDirection);
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
