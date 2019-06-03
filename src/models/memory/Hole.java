package models.memory;

import java.util.Random;


public class Hole extends AbstractResource {
    /* Hole Class which is a free area in Main Memory. */
    public Hole(int sizeBytes) {
        super(sizeBytes);
        this.id = new Random().nextInt(1000000) + 999999;  // To prevent being duplicate.
    }
}
