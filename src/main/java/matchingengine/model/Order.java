package matchingengine.model;

public interface Order {

    String getOrderType();
    float getOrderPrice();
    int getOrderQty();
    void execute();
}
