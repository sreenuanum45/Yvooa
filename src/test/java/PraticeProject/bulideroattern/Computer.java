package PraticeProject.bulideroattern;

public class Computer {
    private String brand;
    private String processor;
    private int ram;
    private int storage;
    private String graphicsCard;
    private Computer(String brand, String processor, int ram, int storage, String graphicsCard) {
        this.brand = brand;
        this.processor = processor;
        this.ram = ram;
        this.storage = storage;
        this.graphicsCard = graphicsCard;
    }
    public static class Builder {
        private String brand;
        private String processor;
        private int ram;
        private int storage;
        private String graphicsCard;
        public Builder setBrand(String brand) {
            this.brand = brand;
            return this;
        }
        public Builder setProcessor(String processor) {
            this.processor = processor;
            return this;
        }
        public Builder setRam(int ram) {
            this.ram = ram;
            return this;
        }
        public Builder setStorage(int storage) {
            this.storage = storage;
            return this;
        }
        public Builder setGraphicsCard(String graphicsCard) {
            this.graphicsCard = graphicsCard;
            return this;
        }
        public Computer build() {
            return new Computer(brand, processor, ram, storage, graphicsCard);
        }
    }
}
