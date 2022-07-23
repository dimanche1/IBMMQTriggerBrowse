import org.apache.commons.cli.*;

public class HandleArgs {
    private final String[] args;
    private CommandLine cmd;

    public HandleArgs(String[] args) {
        this.args = args;

        handle();
    }

    private void handle() {
        Options options = new Options();

        Option host = new Option("h", "host", true, "host");
        host.setRequired(true);
        options.addOption(host);

        Option port = new Option("p", "port", true, "port");
        port.setRequired(true);
        options.addOption(port);

        Option channel = new Option("c", "channel", true, "channel");
        channel.setRequired(true);
        options.addOption(channel);

        Option manager = new Option("m", "manager", true, "MQ manager name");
        manager.setRequired(true);
        options.addOption(manager);

        Option login = new Option("l", "login", true, "login");
        login.setRequired(false);
        options.addOption(login);

        Option password = new Option("pass", "password", true, "password");
        password.setRequired(false);
        options.addOption(password);

        Option queueName = new Option("qn", "queue-name", true, "name of the queue to monitor");
        queueName.setRequired(true);
        options.addOption(queueName);

        Option checkEach = new Option("ce", "check-each", true, "check queue's depth each n milliseconds");
        checkEach.setRequired(true);
        options.addOption(checkEach);

        Option queueDepthToTrigger = new Option("qd", "queue-depth", true, "queue depth to trigger");
        queueDepthToTrigger.setRequired(true);
        options.addOption(queueDepthToTrigger);

        Option browseNMessages = new Option("bn", "browse-n", true, "browse n messages");
        browseNMessages.setRequired(true);
        options.addOption(browseNMessages);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("IBMMQTriggerBrowse saves n messages " +
                    "to the file from the monitored\nqueue when the queue's depth is reached.\n Then the application closes.\n\n", options);

            System.exit(1);
        }
    }

    public String getHost() { return cmd.getOptionValue("host"); }

    public int getPort() { return Integer.parseInt(cmd.getOptionValue("port")); }

    public String getChannel() { return cmd.getOptionValue("channel"); }

    public String getManager() { return cmd.getOptionValue("manager"); }

    public String getLogin() { return cmd. getOptionValue("login"); }

    public String getPassword() { return cmd. getOptionValue("password"); }

    public String getQueueName() { return cmd. getOptionValue("queue-name"); }

    public long getCheckEach() { return Long.parseLong(cmd.getOptionValue("check-each")); }

    public int getQueueDepthToTrigger() { return Integer.parseInt (cmd. getOptionValue("queue-depth")); }

    public int getBrowseNMessages() { return Integer.parseInt (cmd. getOptionValue("browse-n")); }
}
