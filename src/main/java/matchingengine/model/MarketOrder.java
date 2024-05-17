package matchingengine.model;
import matchingengine.utils.IdGenerator;

public class MarketOrder implements Order{

    private final long id;
    private final String side;
    private int quantity;
    private long timestamp;

    public MarketOrder(String side, int quantity) {
        this.id = IdGenerator.generateId();
        this.quantity = quantity;
        this.timestamp = System.currentTimeMillis();

        if ("buy".equalsIgnoreCase(side) || "sell".equalsIgnoreCase(side)) {
            // Ensure the side is always lowercase for consistency
            this.side = side.toLowerCase();
        } else {
            // Default to buy if an invalid side is provided
            System.out.println("Invalid side provided. Defaulting to 'buy'.");
            this.side = "buy";
        }
        System.out.println("Order created: " + this.side + " " + this.quantity + " Identifier: " + this.id);
    }


    /* Market Orders doesn't have price, so I default the buy order as a huge amount and the sell order the opposite
       because with this I don't get error output when I match these orders
    */
    @Override
    public double getOrderPrice() {
        if (side.equalsIgnoreCase("buy")) {
            return 9999999999.9;
        } else {
            return -1;
        }
    }

    @Override
    public int getOrderQty() {
        return quantity;
    }

    @Override
    public String getOrderType() {
        return "market";
    }

    @Override
    public String getOrderSide() {
        return side;
    }

    @Override
    public void updateOrder(double price, int quantity) {
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
