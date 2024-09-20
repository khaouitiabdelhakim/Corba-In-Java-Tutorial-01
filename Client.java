import org.omg.CORBA.ORB;

import java.io.*;

public class Client {
 public static void main(String[] args) {
  java.util.Properties props = System.getProperties();
  int status;
  ORB orb = null;

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

 static int run(org.omg.CORBA.ORB orb) throws IOException {
  org.omg.CORBA.Object obj;

  String refFile = "Hello.ref";
  FileInputStream file = new FileInputStream(refFile);
  BufferedReader in = new BufferedReader(new InputStreamReader(file));
  String ref = in .readLine();
  file.close();

  obj = orb.string_to_object(ref);

  Hello hello = HelloHelper.narrow(obj);
  hello.hello();
  return 0;
 }
}