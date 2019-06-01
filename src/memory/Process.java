package memory;


public class Process extends AbstractResource {
    /* Process Class which is an allocated area in Main Memory. */
    public Process(int id, int sizeBytes) {
        super(sizeBytes);
        this.id = id;
    }
}
