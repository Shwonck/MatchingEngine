package matchingengine.model;

// I did an Order interface so the system can have more scalability later
public interface Order {

    String getOrderType();
    String getOrderSide();
    double getOrderPrice();
    int getOrderQty();
    long getId();
    void updateOrder(double price, int quantity);
    long getTimestamp();
}
