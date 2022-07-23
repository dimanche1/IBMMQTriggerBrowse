import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;

public class QueueManager {
    private final String host;
    private final int port;
    private final String channel;
    private final String manager;
    private String userId = null;
    private String password = null;
    private final MQQueueManager qmngr;
    private final String queueName = null;
    private MQQueue queue;

    public QueueManager(String host, int port, String channel, String manager) throws MQException {
        this.host = host;
        this.port = port;
        this.channel = channel;
        this.manager = manager;
        this.qmngr = createQueueManager();
    }

    public QueueManager(String host, int port, String channel, String manager, String userId) throws MQException {
        this.host = host;
        this.port = port;
        this.channel = channel;
        this.manager = manager;
        this.userId = userId;
        this.qmngr = createQueueManager();
    }

    public QueueManager(String host, int port, String channel, String manager, String userId, String password) throws MQException {
        this.host = host;
        this.port = port;
        this.channel = channel;
        this.manager = manager;
        this.userId = userId;
        this.password = password;
        this.qmngr = createQueueManager();
    }

    private MQQueueManager createQueueManager() throws MQException {
        MQEnvironment.hostname = host;
        MQEnvironment.port = port;
        MQEnvironment.channel = channel;
        MQEnvironment.userID = userId;
        MQEnvironment.password = password;

        MQEnvironment.properties.put(MQConstants.TRANSPORT_PROPERTY, MQConstants.TRANSPORT_MQSERIES);

        return new MQQueueManager(manager);
    }

    public void accessQueue(String queueName) throws MQException {
        queue = qmngr.accessQueue(queueName, MQConstants.MQOO_INQUIRE | MQConstants.MQOO_INPUT_AS_Q_DEF | MQConstants.MQOO_BROWSE);
    }

    public int getQueueDepth() throws MQException {
        return queue.getCurrentDepth();
    }

    public  MQQueueManager getQmngr() {
        return qmngr;
    }

    public MQQueue getQueue() {
        return queue;
    }

    public void close() {
        try {
            queue.close();
            qmngr.close();
            System.out.println("Disconnected from queue manager.");
        } catch (MQException e) {
            System.out.println("MQ error: Completion code " + e.completionCode +
                    " Reason code " + e.reasonCode + " Stack trace: \n");
            e.printStackTrace();
        }
    }
}
