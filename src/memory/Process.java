package memory;


public class Process extends AbstractResource {
    /* Process Class which is an allocated area in Main Memory. */
    public Process(int id, int sizeBytes) {
        super(sizeBytes);
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Process %d, %d KB", id, getSizeBytes() / 1024);
    }
}
