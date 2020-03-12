package me.shib.tools.prymate;

import java.io.FileNotFoundException;

public class PryMateProcessor extends Thread {

    private PryMateQueue queue;
    private PryMateDataManager dataManager;

    PryMateProcessor(PryMateQueue queue, PryMateDataManager dataManager) {
        this.queue = queue;
        this.dataManager = dataManager;
    }

    @Override
    public void run() {
        while (true) {
            try {
                queue.next().build(dataManager);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
