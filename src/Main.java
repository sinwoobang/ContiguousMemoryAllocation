import models.memory.Memory;
import models.memory.Process;
import views.MainView;


public class Main {
    static void sleep() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Memory memory = new Memory();
        MainView view = new MainView(memory);

        int[] fits;

        view.setCommand("핸드아웃 자료 처리 후 Input이 활성화됩니다.");

        sleep();

        Process p1 = new Process(1, 64 * Memory.KB);
        fits = memory.analyzeFit(p1);
        memory.allocate(fits[0], p1);

        view.setCommand("Request " + p1.toString());
        view.setSubCommand(String.format("Best fit : %d / Worst Fit : %d", fits[0], fits[1]));

        sleep();

        Process p2 = new Process(2, 64 * Memory.KB);
        fits = memory.analyzeFit(p2);
        memory.allocate(fits[0], p2);

        view.setCommand("Request " + p2.toString());
        view.setSubCommand(String.format("Best fit : %d / Worst Fit : %d", fits[0], fits[1]));

        sleep();

        Process p3 = new Process(3, 32 * Memory.KB);
        fits = memory.analyzeFit(p3);
        memory.allocate(fits[0], p3);

        view.setCommand("Request " + p3.toString());
        view.setSubCommand(String.format("Best fit : %d / Worst Fit : %d", fits[0], fits[1]));

        sleep();

        Process p4 = new Process(4, 16 * Memory.KB);
        fits = memory.analyzeFit(p4);
        memory.allocate(fits[0], p4);

        view.setCommand("Request " + p4.toString());
        view.setSubCommand(String.format("Best fit : %d / Worst Fit : %d", fits[0], fits[1]));

        sleep();

        memory.free(p1);

        view.setCommand("Free " + p1.toString());
        view.setSubCommand("");

        sleep();

        memory.free(p3);

        view.setCommand("Free " + p3.toString());

        sleep();

        Process p5 = new Process(5, 32 * Memory.KB);
        fits = memory.analyzeFit(p5);
        memory.allocate(fits[0], p5);

        view.setCommand("Request " + p5.toString());
        view.setSubCommand(String.format("Best fit : %d / Worst Fit : %d", fits[0], fits[1]));

        sleep();

        memory.free(p2);

        view.setCommand("Free " + p2.toString());
        view.setSubCommand("");

        sleep();

        memory.compaction();

        view.setCommand("COMPACTION");

        sleep();

        view.setCLIFinished(true);
    }
}
