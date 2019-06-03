package models.memory;

import java.util.ArrayList;


public class Memory {
    /* Main Memory Class */
    public final static int KB = 1024;
    private ArrayList<AbstractResource> resources = new ArrayList<>();

    public Memory() {
        /* The default constructor which has a role of making a 256KB Memory Object. */
        resources.add(new Hole(256 * KB));
    }

    public AbstractResource getResourceById(int id) {
        return resources
                .stream()
                .filter(res -> res.id == id)
                .findFirst()
                .orElseThrow();
    }

    public boolean hasResource(int id) {
        return resources.stream().anyMatch(res -> res.id == id);
    }

    public int[] analyzeFit(Process process) {
        /* The function which finds the best fit and the worst fit for allocation of process. */

        int bestFitIndex = -1, worstFitIndex = -1;
        int bestBytesGap = Integer.MAX_VALUE, worstBytesGap = Integer.MIN_VALUE;
        int sizeBytes = process.getSizeBytes();
        for (int i = 0; i < resources.size(); i++) {
            AbstractResource res = resources.get(i);
            if (res instanceof Process) {
                continue;
            }

            int sizeBytesRes = res.getSizeBytes();
            if (sizeBytesRes >= sizeBytes) {
                int bytesGap = sizeBytesRes - sizeBytes;

                if (bytesGap <= bestBytesGap) {
                    bestFitIndex =  i;
                    bestBytesGap = bytesGap;
                }
                if (bytesGap >= worstBytesGap) {
                    worstFitIndex = i;
                    worstBytesGap = bytesGap;
                }
            }
        }
        System.out.format("Best Fit : %d, Worst Fit : %d\n", bestFitIndex, worstFitIndex);
        return new int[]{bestFitIndex, worstFitIndex};
    }

    public void allocate(int index, Process process) {
        /* The function which allocates a process to a specific index in Memory(member: resources). */

        if (index == -1) {
            System.out.format("**REJECTED ALLOCATION %d**\n", index);
            printResourcesStatistics();
            return;
        }

        AbstractResource resource = resources.get(index);
        resources.remove(index);
        resources.add(index, process);
        int sizeBytesRemain = resource.getSizeBytes() - process.getSizeBytes();
        if (sizeBytesRemain > 0) {
            resources.add(index + 1, new Hole(sizeBytesRemain));
        }

        System.out.format("**ALLOCATED %d**\n", index);
        printResourcesStatistics();
    }

    public void free(Process process) {
        /* The function which makes a specific process free. */

        Hole hole = new Hole(process.getSizeBytes());

        int index = resources.indexOf(process);
        resources.remove(index);
        resources.add(index, hole);
        System.out.format("**FREE %d**\n", index);

        // Coalesce
        int previousIndex = index;
        while (--previousIndex >= 0) {
            AbstractResource res = resources.get(previousIndex);
            if (res instanceof Hole) {
                coalesce((Hole)res, hole);
                previousIndex = --index;
                System.out.println("**COALESCE THE PREVIOUS HOLE**");
            } else {
                break;
            }
        }

        int nextIndex = index;
        while (++nextIndex < resources.size()) {
            AbstractResource res = resources.get(nextIndex);
            if (res instanceof Hole) {
                Hole originalHole = (Hole)resources.get(index);
                coalesce(originalHole, (Hole)res);
                nextIndex = index;
                System.out.println("**COALESCE THE NEXT HOLE**");
            } else {
                break;
            }
        }

        printResourcesStatistics();
    }

    public void compaction() {
        /* The functions which compacts holes. */
        int indexFirstHole = -1;
        boolean isFirst = true;

        int sumHoleBytes = 0;
        ArrayList<AbstractResource> tmpResources = new ArrayList<>();
        for (int i = 0 ; i < resources.size(); i++) {
            AbstractResource resource = resources.get(i);
            if (resource instanceof Hole) {
                if (isFirst) {
                    isFirst = false;
                    indexFirstHole = i;
                } else {
                    tmpResources.add(resource);
                }
                sumHoleBytes += resource.getSizeBytes();
            }
        }

        for (AbstractResource resource : tmpResources) {
            resources.remove(resource);
        }

        resources.get(indexFirstHole).setSizeBytes(sumHoleBytes);

        System.out.println("**Compaction**");
        printResourcesStatistics();
    }

    private void coalesce(Hole holeFirst, Hole holeSecond) {
        /* The function which coalesces two holes. It is used in the end of the function free. */
        int indexFirst = resources.indexOf(holeFirst);
        int totalSizeBytes = holeFirst.getSizeBytes() + holeSecond.getSizeBytes();
        resources.remove(holeFirst);
        resources.remove(holeSecond);
        resources.add(indexFirst, new Hole(totalSizeBytes));
    }

    private String getResourceString() {
        /* The function which casts the member resource to String. It is usually used to print resources. */
        StringBuilder buffer = new StringBuilder("| ");
        for (AbstractResource res : resources) {
            String sizeKB = String.format("%dKB", res.getSizeBytes() / 1024);
            String resourceType;
            if (res instanceof Process) {
                resourceType = String.format("(Process %d)", res.getId());
            } else {
                resourceType = "(Hole)";
            }

            buffer.append(sizeKB);
            buffer.append(resourceType);
            buffer.append(" | ");
        }
        return buffer.toString();
    }

    public String getResourcesStatisticsString() {
        /* The function which get String value of statistics of the member resources.
         * Bytes which are free, the average value of that and The number of allocated processes are provided.
         * Warning : The below code is inefficient. I just wanted to try using Stream API.
         * */

        long numBlocks = resources.stream().filter(res -> res instanceof Hole).mapToInt(AbstractResource::getSizeBytes).count();
        int averageSize = (int) resources.stream().filter(res -> res instanceof Hole).mapToInt(AbstractResource::getSizeBytes).average().orElse(0) / 1024;
        int sizeBytesFree = resources.stream().filter(res -> res instanceof Hole).mapToInt(AbstractResource::getSizeBytes).sum() / 1024;

        return String.format(
                "Memory Resources Size Info : %s\n" +
                        "%dK : Free, %d block(s), average size = %dK",
                getResourceString(), sizeBytesFree, numBlocks, averageSize
        );
    }

    private void printResourcesStatistics() {
        System.out.println(getResourcesStatisticsString());
        System.out.println();
    }
}
