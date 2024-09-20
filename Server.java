import org.omg.CORBA.UserException;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManager;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;


public class Server {
    public static void main(String args[]) {
        java.util.Properties props = System.getProperties();
        int status = 0;
        org.omg.CORBA.ORB orb = null;

        try {
            orb = org.omg.CORBA.ORB.init(args, props);
            status = run(orb);
        } catch (Exception ex) {
            ex.printStackTrace();
            status = 1;
        }

        if (orb != null) {
            try {
                orb.destroy();
            } catch (Exception ex) {
                ex.printStackTrace();
                status = 1;
            }
        }
        System.exit(status);
    }

    static int run(org.omg.CORBA.ORB orb) throws UserException, IOException {
        POA rootPOA;
        POAManager manager;

        rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
        manager = rootPOA.the_POAManager();

        HelloImpl helloImpl = new HelloImpl();
        Hello hello = helloImpl._this(orb);

        String ref = orb.object_to_string(hello);
        String refFile = "Hello.ref";
        FileOutputStream file = new FileOutputStream(refFile);
        PrintWriter out = new PrintWriter(file);
        out.println(ref);
        file.close();

        try {
            if (manager != null) {
                manager.activate();
            }
            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }

        return 0;
    }
}