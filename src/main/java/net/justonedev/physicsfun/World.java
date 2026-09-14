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

    // To avoid concurrent modification exception
    private final Queue<Entity> entityAddQueue;
    private final Queue<Entity> entityRemoveQueue;
    private final Queue<Renderable> renderableAddQueue;
    private final Queue<Renderable> renderableRemoveQueue;
    private final Queue<Force> forceAddQueue;
    private final Queue<Force> forceRemoveQueue;

    @Getter // against convention, but we can't clone this every frame
    private final List<Entity> entities;
    @Getter
    private final List<Renderable> otherRenderables;

    private final List<Force> globalForces;

    public World() {
        entities = new LinkedList<>();
        otherRenderables = new LinkedList<>();
        globalForces = new LinkedList<>();

        entityAddQueue = new LinkedList<>();
        entityRemoveQueue = new LinkedList<>();
        forceAddQueue = new LinkedList<>();
        forceRemoveQueue = new LinkedList<>();
        renderableAddQueue = new LinkedList<>();
        renderableRemoveQueue = new LinkedList<>();
    }

    public void addEntity(Entity entity) {
        entityAddQueue.add(entity);
    }

    public void removeEntity(Entity entity) {
        entityRemoveQueue.add(entity);
    }

    public void addOtherRenderable(Renderable renderable) {
        renderableAddQueue.add(renderable);
    }

    public void removeOtherRenderable(Renderable renderable) {
        renderableRemoveQueue.add(renderable);
    }

    public void addGlobalForce(Force force) {
        forceAddQueue.add(force);
    }

    public void removeGlobalForce(Force force) {
        forceRemoveQueue.add(force);
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
        updateQueues();

        Queue<Force> expired = new LinkedList<>();
        globalForces.forEach((force) -> {
            force.tickForTargets(entities);
            if (force.isNone()) expired.add(force);
        });
        expired.forEach(globalForces::remove);
        for (Entity entity : entities) {
            entity.tick();
        }
    }

    private void updateQueues() {
        if (!entityAddQueue.isEmpty()) {
            entities.addAll(entityAddQueue);
            entityAddQueue.clear();
        }
        if (!entityRemoveQueue.isEmpty()) {
            entities.removeAll(entityRemoveQueue);
            entityRemoveQueue.clear();
        }
        if (!renderableAddQueue.isEmpty()) {
            otherRenderables.addAll(renderableAddQueue);
            renderableAddQueue.clear();
        }
        if (!renderableRemoveQueue.isEmpty()) {
            otherRenderables.removeAll(renderableRemoveQueue);
            renderableRemoveQueue.clear();
        }
        if (!forceAddQueue.isEmpty()) {
            globalForces.addAll(forceAddQueue);
            forceAddQueue.clear();
        }
        if (!forceRemoveQueue.isEmpty()) {
            globalForces.removeAll(forceRemoveQueue);
            forceRemoveQueue.clear();
        }
    }
}
