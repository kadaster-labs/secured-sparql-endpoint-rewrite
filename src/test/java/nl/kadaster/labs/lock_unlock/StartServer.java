package nl.kadaster.labs.lock_unlock;

import org.apache.jena.fuseki.cmd.FusekiCmd;

public class StartServer {
    public static void main(String[] _args) {
        String[] args = { "--mem", "/ds" };
        FusekiCmd.main(args);
    }
}
