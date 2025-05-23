package PraticeProject.bulideroattern;

public interface MainBuilder {
    void setBrand(String brand);
    void setProcessor(String processor);
    void setRam(int ram);
    void setStorage(int storage);
    void setGraphicsCard(String graphicsCard);
    Computer build();
}
