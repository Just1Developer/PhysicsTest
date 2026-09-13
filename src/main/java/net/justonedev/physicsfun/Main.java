package net.justonedev.physicsfun;

import net.justonedev.physicsfun.base.RectangularEntity;
import net.justonedev.physicsfun.base.Vector2D;
import net.justonedev.physicsfun.physics.force.AirResistance;
import net.justonedev.physicsfun.physics.force.Gravity;
import net.justonedev.physicsfun.physics.force.SpringForce;
import net.justonedev.physicsfun.physics.force.SlingshotForce;
import net.justonedev.physicsfun.render.Window;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class Main {

    public static void main(String[] args) throws Exception {
        World world = new World();
        world.startTicking(20);

        Window window = new Window(world, 800, 600);
        window.startRendering(60);

        var test = new RectangularEntity(100, 100, 70, 40);
        test.setHitboxVisible(true, Color.red);
        world.addEntity(test);

        Gravity gravity = new Gravity(35);
        test.applyNewForce(gravity);

        test.applyNewForce(new SlingshotForce(test, new Vector2D(50, 5), 5));

        SpringForce force = new SpringForce(new Vector2D(400, 200), 0.2, 0.15);
        world.addOtherRenderable(force);
        test.applyNewForce(force);
        test.applyNewForce(new AirResistance());

        makeGrabbable(test, window);

        /*
        window.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             * /
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                test.getLocation().setX(e.getX());
                test.getLocation().setY(e.getY());
            }
        });
        */
    }

    static void makeGrabbable(RectangularEntity entity, Window window) {
        int headerSize = window.isUndecorated() ? 0 : 30;
        MouseAdapter adapter = new MouseAdapter() {
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

        window.addMouseListener(adapter);
        window.addMouseMotionListener(adapter);
    }

}
