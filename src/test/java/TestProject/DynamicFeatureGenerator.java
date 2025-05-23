package TestProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DynamicFeatureGenerator {
    public static void main(String[] args) {
        String userStory = "yuvoo https://pm-uat.yvooa.com/SignUp";
        String scenario = AITestGenerator.generateScenario(userStory);

        // Write the generated scenario to a .feature file
        try {
            Files.write(Paths.get("src/test/resources/features/generated.feature"), scenario.getBytes());
            System.out.println("Feature file generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
