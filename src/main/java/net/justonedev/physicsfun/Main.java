package net.justonedev.physicsfun;

import net.justonedev.physicsfun.base.Entity;
import net.justonedev.physicsfun.base.RectangularEntity;
import net.justonedev.physicsfun.base.Vector2D;
import net.justonedev.physicsfun.physics.force.AirResistance;
import net.justonedev.physicsfun.physics.force.Gravity;
import net.justonedev.physicsfun.physics.force.SpringForce;
import net.justonedev.physicsfun.physics.force.SlingshotForce;
import net.justonedev.physicsfun.physics.force.StringForce;
import net.justonedev.physicsfun.render.Window;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        World world = new World();
        world.startTicking(20);

        Window window = new Window(world, 1000, 700);
        window.startRendering(60);

        var test = new RectangularEntity(100, 100, 70, 40);
        test.setHitboxVisible(true, Color.red);
        world.addEntity(test);

        Gravity gravity = new Gravity(35);
        world.addGlobalForce(gravity);

        SpringForce springForce = new SpringForce(new Vector2D(window.getWidth() / 2d, window.getHeight() / 2d), 0.2, 0.15);
        world.addOtherRenderable(springForce);
        test.applyNewForce(springForce);

        world.addGlobalForce(new AirResistance());

        StringForce pendulumForce = new StringForce(new Vector2D(window.getWidth() / 1.6d, window.getHeight() / 2d), 100);
        world.addOtherRenderable(pendulumForce);

        makeGrabbable(test, window);

        List<Entity> entityList = new ArrayList<>();
        entityList.add(test);

        window.addKeyListener(new KeyAdapter() {
            final int strength = 332;
            @Override
            public void keyTyped(KeyEvent event) {
                if (event.getKeyChar() == ' ') {
                    entityList.forEach(e -> e.applyNewForce(new SlingshotForce(test, new Vector2D(Math.random() * strength - strength / 2d, Math.random() * strength - strength / 2d), 5)));
                } else if (event.getKeyChar() == 'n' || event.getKeyChar() == 'e' || event.getKeyChar() == 's') {
                    // "new", "spawn", or "entity"
                    var test = new RectangularEntity(100, 100, 70, 40);
                    test.setHitboxVisible(true, Color.getHSBColor((float) Math.random(), 0.7f, 0.6f));
                    world.addEntity(test);
                    test.applyNewForce(springForce);
                    entityList.add(test);
                } else if (event.getKeyChar() == 'p') {
                    // "new", "spawn", or "entity"
                    var test = new RectangularEntity(100, 100, 70, 40);
                    test.setHitboxVisible(true, Color.getHSBColor((float) Math.random(), 0.7f, 0.6f));
                    world.addEntity(test);
                    test.applyNewForce(pendulumForce);
                    entityList.add(test);
                    makeGrabbable(test, window);
                }
            }
        });
    }

    private static void makeGrabbable(RectangularEntity entity, Window window) {
        int headerSize = window.isUndecorated() ? 0 : 30;
        MouseAdapter mouseEvents = new MouseAdapter() {
            int offsetX = 0;
            int offsetY = 0;
            boolean dragging = false;

            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY() - headerSize;
                Vector2D loc = entity.getLocation();
                if (x < loc.getX()) return;
                if (y < loc.getY()) return;
                if (x > loc.getX() + entity.getWidth()) return;
                if (y > loc.getY() + entity.getHeight()) return;
                offsetX = loc.getIntX() - x;
                offsetY = loc.getIntY() - y;
                dragging = true;
                entity.suspendTicking();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
                entity.unsuspendTicking();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseReleased(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!dragging) return;
                entity.getLocation().setX(offsetX + e.getX());
                entity.getLocation().setY(offsetY + e.getY() - headerSize);
            }
        };

        window.addMouseListener(mouseEvents);
        window.addMouseMotionListener(mouseEvents);
    }

}
