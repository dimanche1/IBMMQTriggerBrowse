import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.constants.MQConstants;

import java.io.EOFException;
import java.io.IOException;

public class TriggerTask implements Runnable{
    private final QueueManager qmngr;
//    private final String queueName;
    private final int browseNMessages;
    private final HandleFile handleFile;
    private final int queueDepthToTrigger;


    public TriggerTask(QueueManager qmngr, String queueName, int queueDepthToTrigger, int browseNMessages, HandleFile handleFile) {
        this.qmngr = qmngr;
//        this.queueName = queueName;
        this.queueDepthToTrigger = queueDepthToTrigger;
        this.browseNMessages = browseNMessages;
        this.handleFile = handleFile;
    }

    @Override
    public void run() {
//        MQQueue queue = null;
//
//        try {
//            queue = qmgr.accessQueue(queueName, MQConstants.MQOO_INQUIRE | MQConstants.MQOO_INPUT_AS_Q_DEF | MQConstants.MQOO_BROWSE);
//
//            int depth = queue.getCurrentDepth();
//
//            if (depth > queueDepthToTrigger) {
//                browseMessages(queue);
//            }
//        } catch (MQException e) {
//            System.out.println("MQ error: Completion code " + e.completionCode +
//                    " Reason code " + e.reasonCode + " Stack trace: \n");
//            e.printStackTrace();
//        }

        try {
            System.out.println(qmngr.getQueueDepth());
            if (qmngr.getQueueDepth() >= queueDepthToTrigger) {
                browseNMessages(qmngr.getQueue());
            }
        } catch (MQException e) {
            System.out.println("MQ error: Completion code " + e.completionCode +
                    " Reason code " + e.reasonCode + " Stack trace: \n");
            e.printStackTrace();
        }
    }

    private void browseNMessages(MQQueue queue) {
        MQMessage message = new MQMessage();
        MQGetMessageOptions gmo = new MQGetMessageOptions();
        gmo.options = MQConstants.MQGMO_WAIT | MQConstants.MQGMO_BROWSE_FIRST;
        gmo.matchOptions = MQConstants.MQMO_NONE;

        for (int i = 0; i < browseNMessages; i++ ) {
            try {
                queue.get(message, gmo);
                String messageStr = message.readStringOfByteLength(message.getMessageLength());

                handleFile.write("@start=========Message-N-" + (i + 1) + "=========start@\n" +
                                    messageStr + "\n" +
                                    "@end=========Message-N-" + (i + 1) + "=========end@");

                message.clearMessage();
                gmo.options = MQConstants.MQGMO_WAIT | MQConstants.MQGMO_BROWSE_NEXT;

            } catch (MQException e) {
                if (e.completionCode == 2 && e.reasonCode == MQConstants.MQRC_NO_MSG_AVAILABLE) {
                    System.out.println("All messages are read");
                } else {
                    System.out.println("MQ error: Completion code " + e.completionCode +
                            " Reason code " + e.reasonCode + " Stack trace: \n");
                    e.printStackTrace();
                }
                break;
            } catch (EOFException e) {
                e.printStackTrace();
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        App.close();
    }
}
