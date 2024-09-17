package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GreenhouseServer {
    private static final int PORT = 5000;
    private static final String LOG_FILE = "greenhouse_data.log";
    private ExecutorService threadPool;

    public GreenhouseServer() {
        // Trådpool med faste antal tråde
        threadPool = Executors.newFixedThreadPool(10);
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String data;
            while ((data = reader.readLine()) != null) {
                logData(data);
                checkThreshold(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logData(String data) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkThreshold(String data) {
        String[] parts = data.split(":");
        String sensorType = parts[0];
        double value = Double.parseDouble(parts[1]);

        switch (sensorType) {
            case "temperature":
                if (value > 30.0) {
                    System.out.println("ALARM: Temperature exceeds high threshold! (" + value + "°C)");
                } else if (value < 10.0) {
                    System.out.println("ALARM: Temperature below low threshold! (" + value + "°C)");
                }
                break;
            case "humidity":
                if (value > 70) {
                    System.out.println("ALARM: Humidity exceeds high threshold! (" + value + "%)");
                } else if (value < 30) {
                    System.out.println("ALARM: Humidity below low threshold! (" + value + "%)");
                }
                break;
            case "soil_moisture":
                if (value < 20) {
                    System.out.println("ALARM: Soil moisture below threshold! (" + value + "%)");
                }
                break;
        }
    }

    public static void main(String[] args) {
        GreenhouseServer server = new GreenhouseServer();
        server.startServer();
    }
}