# CORBA in Java: A Step-by-Step Guide

## Overview

This guide walks you through creating a simple CORBA application in Java, where a client communicates with a server to receive a message. We'll use Java 1.8 to implement CORBA (Common Object Request Broker Architecture) and define an IDL (Interface Definition Language) interface to facilitate communication between the client and server.

### Prerequisites

- **Java 1.8** must be installed on your machine.
- **CORBA IDL Compiler** (`idlj`) must be available.

### Steps

---

### 1. Install Java 1.8

Ensure you have Java 1.8 installed on your system. You can download it from the official Oracle archive:

- [Download Java SE 8](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html)

#### Installation Instructions:
1. Install Java by following the instructions on the website.
2. After installation, add the **bin** folder to your system's `PATH`. This allows Java and CORBA tools like `idlj` to be executed from the command line.

Example `PATH`:
```
C:\Program Files\Java\jdk1.8.0_202\bin
```

### 2. Verify Installation

Check if `idlj` (the IDL compiler) is available by running the following command in your terminal or command prompt:

```bash
idlj -version
```

If the command runs successfully and shows the version, you're ready to proceed.

### 3. Write an IDL File

Create a new file named `hello.idl`. This file defines the CORBA interface, which will be used for communication between the client and server.

```idl
interface Hello {
    void hello();
};
```

### 4. Compile the IDL File

Use the CORBA IDL compiler to generate the necessary classes for both client and server communication.

Run the following command:

```bash
idlj -fall hello.idl
```

#### After Compilation:
- A new directory named `hello` will be created.
- Several Java classes will be generated. These include stubs and skeletons necessary for CORBA communication.

Make sure to open and review the generated files to understand the communication structure.

### 5. Implement the Server and Client

Now that the IDL has been compiled, implement the server and client in Java. Use the generated classes to handle CORBA object communication.

#### HelloImpl.java

```java
public class HelloImpl extends HelloPOA {
    @Override
    public void hello() {
        System.out.println("Hello");
    }
}
```

#### Server.java

```java
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
```

#### Client.java

```java
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
```

### 6. Run the Server and Client

After implementing both the client and server:

1. **Run the Server**:
    - Open a terminal and execute the following command:
   ```bash
   java Server
   ```

2. **Run the Client**:
    - Open another terminal and execute the following command:
   ```bash
   java Client
   ```

If everything is set up correctly, the client will print `Hello from the server!` to the console.

---

### Summary

This guide showed how to implement a simple CORBA-based client-server system in Java. By following these steps, you've defined a CORBA interface, compiled the necessary Java classes, implemented the server and client, and executed the communication between them.