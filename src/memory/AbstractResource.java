package memory;


public abstract class AbstractResource {
    /* AbstractResource Class that represents resources in Main Memory such as Hole, Process. */

    int id;
    private int sizeBytes;

    protected AbstractResource(int sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public int getId() {
        return id;
    }

    public int getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(int sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractResource) {
            return id == ((AbstractResource) obj).getId();
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return id;
    }
}
