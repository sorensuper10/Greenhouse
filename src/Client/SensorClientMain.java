package Client;

public class SensorClientMain {
    public static void main(String[] args) {
        SensorClient temperatureClient = new SensorClient("temperature", "localhost", 5000);
        SensorClient humidityClient = new SensorClient("humidity", "localhost", 5000);
        SensorClient soilMoistureClient = new SensorClient("soil_moisture", "localhost", 5000);

        // Kør hver sensor på en separat tråd
        new Thread(() -> temperatureClient.sendData()).start();
        new Thread(() -> humidityClient.sendData()).start();
        new Thread(() -> soilMoistureClient.sendData()).start();
    }
}