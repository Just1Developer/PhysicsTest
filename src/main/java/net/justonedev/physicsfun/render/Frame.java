package net.justonedev.physicsfun.render;

import lombok.Getter;
import net.justonedev.physicsfun.World;
import net.justonedev.physicsfun.base.Entity;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

public class Frame extends JPanel {

    private boolean render;
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
            entity.tick();
            entity.render(graphics2D);
        }
    }

    public void startRendering(double fps) {
        render = true;
        double frameTimeNanos = 1e9 / fps;
        long time, frameNanos, remaining;
        long frameTime = Math.round(1e3 / fps);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0L, frameTime);

        /*
        while (render) {
            time = System.nanoTime();
            this.repaint();
            frameNanos = System.nanoTime() - time;
            remaining = Math.round((frameTimeNanos - frameNanos) / 1e6);
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
         */
    }

    public void stopRendering() {
        this.render = false;
    }
}
