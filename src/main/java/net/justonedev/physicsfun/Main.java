package net.justonedev.physicsfun;

import net.justonedev.physicsfun.base.RectangularEntity;
import net.justonedev.physicsfun.base.Vector2D;
import net.justonedev.physicsfun.physics.force.SlingshotForce;
import net.justonedev.physicsfun.render.Window;

import java.awt.Color;

public class Main {

    public static void main(String[] args) throws Exception {
        World world = new World();

        Window window = new Window(world, 800, 600);
        window.startRendering(30);

        Thread.sleep(2000);

        var test = new RectangularEntity(100, 100, 70, 40);
        test.setHitboxVisible(true, Color.red);
        world.addEntity(test);

        Thread.sleep(5000);

        test.applyNewForce(new SlingshotForce(test, new Vector2D(50, 5), 5));
    }

}
