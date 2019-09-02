package socket;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MyHTTPServer {
    public static void main(String[] args) throws IOException {
        System.out.println("connection port 5000 .....");
        ServerSocket serve = new ServerSocket(5000);
        Socket s = serve.accept();
        PrintWriter out = new PrintWriter(s.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        BufferedOutputStream or = new BufferedOutputStream(s.getOutputStream());
        while (true) {
            String input = br.readLine();
            File file = new File("index.html");
            int fileLength = (int) file.length();
            if (input == null) {
                break;
            }
            if (input.contains("GET")) {
                byte[] fileData = readFileData(file, fileLength);
                out.println("HTTP/1.1 200 is  OK");
                out.println();
                out.flush();
                or.write(fileData, 0, fileLength);
                or.flush();
            }
        }
    }
    private static byte[] readFileData(File file, int fileLength) throws IOException {
        byte[] fileData = new byte[fileLength];
        FileInputStream fileIn = new FileInputStream(file);
        int f = fileIn.read(fileData);

        while (f == -1) {
            break;
        }
        return fileData;
    }


}
