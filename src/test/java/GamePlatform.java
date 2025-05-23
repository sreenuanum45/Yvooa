public class GamePlatform {
    public static double calculateFinalSpeed(double initialSpeed, int[] inclinations) {
        double currentSpeed = initialSpeed;
        for (int angle : inclinations) {
            currentSpeed -= angle;
            if (currentSpeed <= 0) {
                return 0.0;
            }
        }
        return currentSpeed;
    }

    public static void main(String[] args) {
        System.out.println(calculateFinalSpeed(60.0, new int[] { 0, 30, 0, -45, 0 }));
    }
}