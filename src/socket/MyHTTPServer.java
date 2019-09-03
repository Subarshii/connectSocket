package socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MyHTTPServer {
    public static void main(String[] args) throws IOException {
        System.out.println("connection port 5000 .....");
        ServerSocket serve = null;
        PrintWriter out = null;
        Socket s = null;
        BufferedReader br = null;
        BufferedOutputStream or = null;
        try {
            serve = new ServerSocket(5000);
            s = serve.accept();
            out = new PrintWriter(s.getOutputStream());
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            or = new BufferedOutputStream(s.getOutputStream());
            String input = br.readLine();
            String[] split = input.split(" ");

            String filename = split[1];
            File file = new File(".", filename);

            int fileLength = (int) file.length();
            if (input.contains("GET")) {
                byte[] fileData = readFile(file, fileLength);
                out.println("GET");
                out.println("HTTP/1.1 200 OK");
                out.println();
                out.flush();
                or.write(fileData, 0, fileLength);
                or.flush();
            }
        } catch (FileNotFoundException fn) {
            try {
                String notfound = "fileNotFound.html";
                File filenotFound = new File(".", notfound);
                int fileLength = (int) filenotFound.length();

                byte[] fileData = readFile(filenotFound, fileLength);
                out.println("GET");
                out.println("HTTP/1.1 400 FILE NOT FOUND");
                out.println();
                out.flush();
                or.write(fileData, 0, fileLength);
                or.flush();
            } catch (IOException ioe) {
                System.out.println(" Error Exception " + ioe.getMessage());
            }

        } finally {
            out.close();
            br.close();
            or.close();

        }

    }


    private static byte[] readFile(File file, int fileLength) throws IOException {
        byte[] fileData = new byte[fileLength];
        try (InputStream fileIn = new FileInputStream(file)) {
            int f = fileIn.read(fileData);
            while (f == -1) {
                break;
            }

        }
        return fileData;
    }


}
