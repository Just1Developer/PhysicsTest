package net.justonedev.physicsfun;

import lombok.Getter;
import net.justonedev.physicsfun.base.Entity;
import net.justonedev.physicsfun.base.Renderable;
import net.justonedev.physicsfun.base.Tickable;
import net.justonedev.physicsfun.physics.Force;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class World implements Tickable {
    @Getter // against convention, but we can't clone this every frame
    private final List<Entity> entities;
    @Getter
    private final List<Renderable> otherRenderables;

    private final List<Force> globalForces;

    public World() {
        entities = new LinkedList<>();
        otherRenderables = new LinkedList<>();
        globalForces = new LinkedList<>();
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void addOtherRenderable(Renderable renderable) {
        otherRenderables.add(renderable);
    }

    public void removeOtherRenderable(Renderable renderable) {
        otherRenderables.remove(renderable);
    }

    public void addGlobalForce(Force force) {
        globalForces.add(force);
    }

    public void removeGlobalForce(Force force) {
        globalForces.remove(force);
    }

    public void startTicking(double tps) {
        long tickTime = Math.round(1e3 / tps);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tick();
            }
        }, 0L, tickTime);
    }

    @Override
    public void tick() {
        Queue<Force> expired = new LinkedList<>();
        globalForces.forEach((force) -> {
            force.tickForTargets(entities);
            if (force.isNone()) expired.add(force);
        });
        expired.forEach(globalForces::remove);
        entities.forEach(Entity::tick);
    }
}
