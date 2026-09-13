package net.justonedev.physicsfun;

import lombok.Getter;
import net.justonedev.physicsfun.base.Entity;
import net.justonedev.physicsfun.base.Renderable;
import net.justonedev.physicsfun.base.Tickable;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class World implements Tickable {
    @Getter // against convention, but we can't clone this every frame
    private final List<Entity> entities;
    @Getter
    private final List<Renderable> otherRenderables;

    public World() {
        entities = new LinkedList<>();
        otherRenderables = new LinkedList<>();
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
        entities.forEach(Entity::tick);
    }
}
