package net.justonedev.physicsfun.render;

import lombok.Getter;
import net.justonedev.physicsfun.World;
import net.justonedev.physicsfun.base.Entity;
import net.justonedev.physicsfun.base.Renderable;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

public class Frame extends JPanel {

    @Getter
    private final World world;

    public Frame(World world, int width, int height) {
        this.world = world;
        this.setSize(width, height);
        this.setVisible(true);
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        this.invalidate();
        for (Entity entity : world.getEntities()) {
            entity.render(graphics2D);
        }
        for (Renderable renderable : world.getOtherRenderables()) {
            renderable.render(graphics2D);
        }
    }

    public void startRendering(double fps) {
        long frameTime = Math.round(1e3 / fps);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0L, frameTime);
    }
}
