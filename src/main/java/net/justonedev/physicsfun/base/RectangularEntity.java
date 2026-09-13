package net.justonedev.physicsfun.base;

import lombok.Getter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Optional;

@Getter
public class RectangularEntity extends Entity {

    private static final int HITBOX_BORDER_STRENGTH = 3;
    private static final Stroke HITBOX_BORDER_STROKE = new BasicStroke(HITBOX_BORDER_STRENGTH);

    private int width;
    private int height;
    private Color hitboxColor;

    public RectangularEntity(int x, int y, int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(Graphics2D graphics) {
        int x = getLocation().getIntX();
        int y = getLocation().getIntY();

        getHitboxColor().ifPresent(color -> {
            graphics.setColor(color);
            var stroke = graphics.getStroke();
            graphics.setStroke(HITBOX_BORDER_STROKE);
            graphics.drawRect(x, y, getWidth(), getHeight());
            graphics.setStroke(stroke);
        });
    }

    @Override
    public void tick() {
        super.tick();
    }

    public Vector2D getCenter() {
        return new Vector2D(getLocation().getX() + getWidth() / 2d, getLocation().getY() + height / 2d);
    }

    public void setHitboxVisible(boolean visible) {
        this.setHitboxVisible(visible, Color.black);
    }

    public void setHitboxVisible(boolean visible, Color hitboxColor) {
        if (!visible) this.hitboxColor = null;
        else this.hitboxColor = hitboxColor;
    }

    private Optional<Color> getHitboxColor() {
        if (this.hitboxColor == null) return Optional.empty();
        return Optional.of(this.hitboxColor);
    }
}
