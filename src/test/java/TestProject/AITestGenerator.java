import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class AITestGenerator {
    // Replace with your actual API key
    private static final String API_KEY = "sk-proj-zheSwrD79Jyt4YIGzBxNT3BlbkFJMHZTO8kuKfYMB9B4FErM";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static String generateScenario(String userStory) {
        OkHttpClient client = new OkHttpClient();

        // Build the request JSON payload
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", "Generate a Gherkin feature file scenario for the following user story: " + userStory);

        JsonArray messages = new JsonArray();
        messages.add(message);

        JsonObject payload = new JsonObject();
        payload.addProperty("model", "gpt-3.5-turbo");
        payload.add("messages", messages);

        RequestBody body = RequestBody.create(
                payload.toString(), MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            // Parse the response JSON to extract the generated text
            String responseBody = response.body().string();
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray choices = jsonResponse.getAsJsonArray("choices");
            if (choices != null && choices.size() > 0) {
                JsonObject firstChoiceJsonObject = choices.get(0).getAsJsonObject();
                JsonObject messageObj = firstChoiceJsonObject.getAsJsonObject("message");
                if (messageObj != null && messageObj.has("content")) {
                    return messageObj.get("content").getAsString();
                }
            }
            return "No scenario generated.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error generating scenario.";
        }
    }
}

