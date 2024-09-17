package Client;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class SensorClient {
    private String sensorType;
    private String serverIP;
    private int serverPort;

    public SensorClient(String sensorType, String serverIP, int serverPort) {
        this.sensorType = sensorType;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public void sendData() {
        Random random = new Random();
        try (Socket socket = new Socket(serverIP, serverPort)) {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            while (true) {
                // Simulerer tilfældige data fra sensoren
                double value = sensorType.equals("temperature") ? random.nextDouble() * 40 :
                        random.nextDouble() * 100;
                writer.println(sensorType + ":" + value);
                Thread.sleep(5000); // Send data hvert 5. sekund
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}