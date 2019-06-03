import memory.Memory;
import memory.Process;


public class Main {

    public static void main(String[] args) {
        Memory memory = new Memory();

        int[] fits;

        Process p1 = new Process(1, 64 * Memory.KB);
        fits = memory.analyzeFit(p1);
        memory.allocate(fits[0], p1);

        Process p2 = new Process(2, 64 * Memory.KB);
        fits = memory.analyzeFit(p2);
        memory.allocate(fits[0], p2);

        Process p3 = new Process(3, 32 * Memory.KB);
        fits = memory.analyzeFit(p3);
        memory.allocate(fits[0], p3);

        Process p4 = new Process(4, 16 * Memory.KB);
        fits = memory.analyzeFit(p4);
        memory.allocate(fits[0], p4);

        memory.free(p1);
        memory.free(p3);

        Process p5 = new Process(5, 32 * Memory.KB);
        fits = memory.analyzeFit(p5);
        memory.allocate(fits[0], p5);

        memory.free(p2);

        memory.compaction();
    }
}
