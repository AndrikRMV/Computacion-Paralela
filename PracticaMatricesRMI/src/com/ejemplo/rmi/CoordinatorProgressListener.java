package com.ejemplo.rmi;

public interface CoordinatorProgressListener {
    /**
     * Called when a worker starts processing a block.
     * @param workerIndex index of the worker (0..N-1)
     * @param startRow first row (inclusive) of the block
     * @param endRow last row (exclusive)
     */
    void onBlockStart(int workerIndex, int startRow, int endRow);

    /**
     * Called when a worker finishes processing a block.
     */
    void onBlockDone(int workerIndex, int startRow, int endRow);
}
