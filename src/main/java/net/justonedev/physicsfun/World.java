package net.justonedev.physicsfun;

import lombok.Getter;
import net.justonedev.physicsfun.base.Entity;

import java.util.LinkedList;
import java.util.List;

public class World {
    @Getter // against convention, but we can't clone this every frame
    private final List<Entity> entities;

    public World() {
        entities = new LinkedList<>();
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }
}
