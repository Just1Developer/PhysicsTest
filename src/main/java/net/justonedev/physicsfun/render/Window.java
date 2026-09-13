package net.justonedev.physicsfun.render;

import lombok.Getter;
import net.justonedev.physicsfun.World;

import javax.swing.JFrame;

public class Window extends JFrame {

    @Getter
    private final Frame frame;

    public Window(World world, int width, int height) {
        this(world, width, height, "Physics Test");
    }

    public Window(World world, int width, int height, String title) {
        this.setSize(width, height);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle(title);

        this.frame = new Frame(world, width, height);
        this.add(frame);

        this.setVisible(true);
    }

    public void startRendering(double fps) {
        frame.startRendering(fps);
    }

}
