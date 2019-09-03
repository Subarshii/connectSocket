package socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyHTTPServer {
    public static void main(String[] args) throws IOException {
        System.out.println("connection port 5000 .....");
        ServerSocket serve = null;
        PrintWriter pw = null;
        Socket s = null;
        BufferedReader br = null;
        BufferedOutputStream bout = null;
        try {
            serve = new ServerSocket(5000);
            s = serve.accept();
            pw = new PrintWriter(s.getOutputStream());
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            bout = new BufferedOutputStream(s.getOutputStream());


            String input = br.readLine();
            String[] split = input.split(" ");
            String filename = split[1];
            Path path = Paths.get(".", filename);

            try (InputStream inputpath = Files.newInputStream(path);
                 BufferedInputStream bintput = new BufferedInputStream(inputpath)) {
                if (input.contains("GET")) {
                    readFile(pw, bout, bintput, "HTTP/1.1 200 OK");
                }
            }
        } catch (NoSuchFileException nfe) {
            String notfileHTML = "fileNotFound.html";
            Path pathNotFound = Paths.get(notfileHTML);
            try (InputStream in = Files.newInputStream(pathNotFound);
                 BufferedInputStream bindel = new BufferedInputStream(in)) {
                readFile(pw, bout, bindel, "HTTP/1.1 404 Page Not Found ");
            }
        } finally {
            bout.close();
            br.close();
            pw.close();
            s.close();
            serve.close();

        }

    }

    private static void readFile(PrintWriter pw, BufferedOutputStream bout, BufferedInputStream bindel, String
            println) throws IOException {
        byte[] bytebuffer = new byte[1024];
        while (true) {
            int byteread = bindel.read(bytebuffer, 0, bytebuffer.length);
            if (byteread == -1) {
                break;
            }
            pw.println(println);
//            pw.println(getConTenType());
            pw.println();
            pw.flush();
            bout.write(bytebuffer, 0, byteread);
            bout.flush();
        }
    }

    public static String getConTenType(String filetype) {
        if (filetype.endsWith(".htm") || filetype.endsWith(".html")) {
            return "text/html";
        } else if (filetype.endsWith("xml")) {
            return "application/xml";
        } else
            return "text/plain";

    }

}

