package net.justonedev.physicsfun;

import net.justonedev.physicsfun.base.RectangularEntity;
import net.justonedev.physicsfun.base.Vector2D;
import net.justonedev.physicsfun.physics.force.AirResistance;
import net.justonedev.physicsfun.physics.force.SpringForce;
import net.justonedev.physicsfun.physics.force.SlingshotForce;
import net.justonedev.physicsfun.render.Window;

import java.awt.Color;

public class Main {

    public static void main(String[] args) throws Exception {
        World world = new World();
        world.startTicking(20);

        Window window = new Window(world, 800, 600);
        window.startRendering(60);

        var test = new RectangularEntity(100, 100, 70, 40);
        test.setHitboxVisible(true, Color.red);
        world.addEntity(test);

        Thread.sleep(1000);

        test.applyNewForce(new SlingshotForce(test, new Vector2D(50, 5), 5));

        SpringForce force = new SpringForce(new Vector2D(400, 200), 0.6, 0.1);
        world.addOtherRenderable(force);
        test.applyNewForce(force);
        test.applyNewForce(new AirResistance());
    }

}
