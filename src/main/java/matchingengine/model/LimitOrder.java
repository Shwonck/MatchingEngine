package matchingengine.model;

import matchingengine.utils.IdGenerator;

public class LimitOrder implements Order {

    private final long id;
    private final String side;
    private double price;
    private int quantity;
    private long timestamp;


    // With timestamp, I can organize the priority queue by time FIFO
    public LimitOrder(String side, double price, int quantity) {
        this.id = IdGenerator.generateId();
        this.price = price;
        this.quantity = quantity;
        this.timestamp = System.currentTimeMillis();

        if ("buy".equalsIgnoreCase(side) || "sell".equalsIgnoreCase(side)) {
           // Ensure the side is always lowercase for consistency
            this.side = side.toLowerCase();
        } else {
            // default wrong inputs for order side as a buy order
            System.out.println("Invalid side provided. Defaulting to 'buy'.");
            this.side = "buy";

        }

        System.out.println("Order created: " + this.side + " " + this.quantity + " @ " + this.price + " Identifier: " + id);
    }

    @Override
    public double getOrderPrice() {
        return price;
    }

    @Override
    public int getOrderQty() {
        return quantity;
    }

    @Override
    public String getOrderType() {
        return "limit";
    }

    @Override
    public String getOrderSide() {
        return side;
    }

    @Override
    public void updateOrder(double price, int quantity) {
        this.price = price;
        this.quantity = quantity;
        this.timestamp = System.currentTimeMillis();     // Whenever I update my order I loose time priority and keep only value priority
    }

    @Override
    public long getTimestamp(){
        return timestamp;
    }

    @Override
    public long getId() {
        return id;
    }
}
