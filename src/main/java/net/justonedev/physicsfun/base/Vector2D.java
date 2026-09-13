package net.justonedev.physicsfun.base;

import lombok.Getter;
import lombok.Setter;
import net.justonedev.physicsfun.exception.IllegalOperationException;

import java.util.Objects;

@Getter
public class Vector2D {
    public static final Vector2D ZERO = new Vector2D(0, 0, true);
    private static final double ZERO_EPSILON_VALUE = 1d / 1e6;

    private final boolean immutable;
    @Setter
    private double x;
    @Setter
    private double y;
    
    public Vector2D(double x, double y) {
        this(x, y, false);
    }

    public Vector2D(Vector2D clone) {
        this(clone.getX(), clone.getY(), clone.isImmutable());
    }

    private Vector2D(double x, double y, boolean immutable) {
        setX(x);
        setY(y);
        this.immutable = immutable;
    }

    public void add(Vector2D other) {
        if (isImmutable()) throw new IllegalOperationException("Vector is immutable.");
        setX(getX() + other.getX());
        setY(getY() + other.getY());
    }

    public void subtract(Vector2D other) {
        if (isImmutable()) throw new IllegalOperationException("Vector is immutable.");
        setX(getX() - other.getX());
        setY(getY() - other.getY());
    }

    public void multiply(double factor) {
        if (isImmutable()) throw new IllegalOperationException("Vector is immutable.");
        setX(getX() * factor);
        setY(getY() * factor);
    }

    public void cap(Vector2D maximum) {
        if (isImmutable()) throw new IllegalOperationException("Vector is immutable.");
        setX(Math.min(getX(), maximum.getX()));
        setY(Math.min(getY(), maximum.getY()));
    }

    public int getIntX() {
        return (int) Math.round(getX());
    }

    public int getIntY() {
        return (int) Math.round(getY());
    }

    public boolean isQuasiZero() {
        return isQuasiZero(ZERO_EPSILON_VALUE);
    }

    public boolean isQuasiZero(double epsilon) {
        return Math.abs(getX()) < epsilon && Math.abs(getY()) < epsilon;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        return x == vector2D.x && y == vector2D.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
