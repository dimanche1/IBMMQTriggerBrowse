import com.ibm.mq.MQException;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {
    private static QueueManager qm = null;
    private static ScheduledExecutorService scheduledExecutorService;
    private static HandleArgs handleArgs;
    private static HandleFile handleFile;

    public static void main(String[] args) {

        handleArgs = new HandleArgs(args);

        try {
            handleFile = new HandleFile("output.txt");
        } catch (IOException e) {
            System.out.println("Can't create thr file. Stack trace:\n");
            e.printStackTrace();

            System.exit(1);
        }

        try {
            qm = new QueueManager(handleArgs.getHost(), handleArgs.getPort(), handleArgs.getChannel(),
                    handleArgs.getManager(), handleArgs.getLogin(), handleArgs.getPassword());
            System.out.println("Connected to MQ manager " + handleArgs.getManager());

            qm.accessQueue(handleArgs.getQueueName());
            System.out.println("Connected to queue " + handleArgs.getManager());

        } catch (MQException e) {
            System.out.println("MQ error: Completion code " + e.completionCode +
                    " Reason code " + e.reasonCode + " Stack trace: \n");
            e.printStackTrace();

            System.exit(1);
        }

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(new TriggerTask(qm, handleArgs.getQueueName(),
                handleArgs.getQueueDepthToTrigger(), handleArgs.getBrowseNMessages(), handleFile),
                1, handleArgs.getCheckEach(), TimeUnit.MILLISECONDS);
    }

    public static void close() {
        qm.close();
        scheduledExecutorService.shutdown();
        try {
            handleFile.close();
        } catch (IOException e) {
            System.out.println("Can't close the file. Stack trace:\n");
            e.printStackTrace();
        }
    }
}
