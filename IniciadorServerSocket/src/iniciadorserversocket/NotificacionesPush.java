
package iniciadorserversocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotificacionesPush {

    // token server Amy = "AAAA3072p3Q:APA91bFXK40U3IOHQOeSDuUS163yNP_zMF5Jn1OaxKdHF4Xd8VYkDJfuVYBnpIG08Y1i-GWbIl6P-HTZPyq1CidKSkJ3AK3ukwxOfgv7WOPKvi091z3ZP9_hOJnBQt0MpjKaT_LHU5C-"
    // token dispositivo Amy = "dKjpbX6GS5631-Mb7g4axK:APA91bES1d2XrTr4n3uVd9T8GXWIrOZSmhaeu6Y_7V-KiSGJsx-hhD5xLPaumr64aYa7R1QaFURZeoUzqm51OF-fN06igA3E74VcLlNZw2LR1PYCzNQs_YzYzsUsI8-nUEKilVszH-uz"
    private static final String serverToken = "AAAAERWRpMA:APA91bGl5XG8XmNNurhfR1IeLK4DObgUOS2mPljC7vaAAvwrYZ3dAgElxmDYyDwM6URo5rtKfFjzikNDkh_LmQwb_xlon-oaMFW2M-Kky-GZpjZjgmwmaJynUNPNgIr0jTumPkJgJjxW ";
    private static final String deviceToken = "cVjtFcO8RM6BC-Hv69hctD:APA91bHHRGIbMrCy22WyoBKWkDcFjqKNqGUko7hYpaFM0lGBtvzuKzktU0YaClMRevaxYkOVssGrFbpTgj7Ut_hhxW1E9BsGan0EozJFb3gWLG5U9P73Dvu52C5UcgpHllWsdrwYexW2";
    private static final String urlFirebaseAppString = "https://fcm.googleapis.com/fcm/send";
    private static URL url = null;
    private static HttpURLConnection connection = null;

    private static URL getUrl() {
        if (url == null) {
            try {
                url = new URL(urlFirebaseAppString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return url;
    }

    private static HttpURLConnection getConnection() {
        if (connection == null) {
            try {
                connection = (HttpURLConnection) getUrl().openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "key=" + serverToken);
                connection.setDoOutput(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
    
    private static String getJsonInputString(String titleString, String bodyString) {
        return "{\"registration_ids\": [\"" + deviceToken + "\"], \"notification\": { \"body\": \"" + bodyString + "\", \"title\": \"" + titleString + "\" }}";
    }

    public static void sendNotification(String title, String body) {
        try {
            try ( OutputStream os = getConnection().getOutputStream()) {
                String json = getJsonInputString(title, body);
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            try ( BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }                
            }
            connection = null;
            url = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}