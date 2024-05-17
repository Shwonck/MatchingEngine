package matchingengine.engine;

import matchingengine.model.Order;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class OrderBook {
    private final PriorityQueue<Order> buyOrders;
    private final PriorityQueue<Order> sellOrders;
    private final Map<Long, Order> orderMap; // for quick lookup of orders by ID using Hash method for searching

    public OrderBook() {
        // Comparator for buy orders (max-heap for price), considering timestamp for FIFO
        Comparator<Order> buyOrderComparator = (o1, o2) -> {
            int priceComparison = Double.compare(o2.getOrderPrice(), o1.getOrderPrice());
            if (priceComparison == 0) {
                return Long.compare(o1.getTimestamp(), o2.getTimestamp());
            }
            return priceComparison;
        };
        buyOrders = new PriorityQueue<>(buyOrderComparator);

        // comparator for sale orders (min-heap for prices), considering timestamp for FIFO
        Comparator<Order> sellOrderComparator = (o1, o2) -> {
            int priceComparison = Double.compare(o1.getOrderPrice(), o2.getOrderPrice());
            if (priceComparison == 0) {
                return Long.compare(o1.getTimestamp(), o2.getTimestamp());
            }
            return priceComparison;
        };
        sellOrders = new PriorityQueue<>(sellOrderComparator);
        orderMap = new HashMap<>();
    }


    public void buy(@NotNull Order order) {
        if (order.getOrderSide().equalsIgnoreCase("buy")) {
            // add to PriorityQueue and to ID hashtable
            buyOrders.add(order);
            orderMap.put(order.getId(), order);
            matchOrders();
        }
    }

    public void sell(@NotNull Order order) {
        if (order.getOrderSide().equalsIgnoreCase("sell")) {
            // add to PriorityQueue and to ID hashtable
            sellOrders.add(order);
            orderMap.put(order.getId(), order);
            matchOrders();
        }
    }

    public boolean cancelOrder(long orderId) {
        Order order = orderMap.remove(orderId);
        if (order == null) {
            return false;
        }

        // remove from order respective PriorityQueue (buy or sell)
        if (order.getOrderSide().equalsIgnoreCase("buy")) {
            buyOrders.remove(order);
        } else if (order.getOrderSide().equalsIgnoreCase("sell")) {
            sellOrders.remove(order);
        }

        return true;
    }


    public boolean updateOrder(long orderId, double newPrice, int newQuantity) {
        Order order = orderMap.get(orderId);
        if (order == null) {
            return false;
        }

        // remove order from PriorityQueue
        if (order.getOrderSide().equalsIgnoreCase("buy")) {
            buyOrders.remove(order);
        } else if (order.getOrderSide().equalsIgnoreCase("sell")) {
            sellOrders.remove(order);
        }

        // update information AND timestamp
        order.updateOrder(newPrice, newQuantity);

        // add again on PriorityQueue
        if (order.getOrderSide().equalsIgnoreCase("buy")) {
            buyOrders.add(order);
        } else if (order.getOrderSide().equalsIgnoreCase("sell")) {
            sellOrders.add(order);
        }
        return true;
    }

    public void matchOrders() {
        boolean ordersMatched;

        // keep matching orders until no more matches are possible
        do {
            ordersMatched = false; // reset the flag for each iteration
            Iterator<Order> buyIterator = buyOrders.iterator();
            while (buyIterator.hasNext()) {
                Order buyOrder = buyIterator.next();
                Iterator<Order> sellIterator = sellOrders.iterator();
                while (sellIterator.hasNext()) {
                    Order sellOrder = sellIterator.next();
                    if (buyOrder.getOrderType().equalsIgnoreCase("Market") && sellOrder.getOrderType().equalsIgnoreCase("Market")){
                        break;
                    }
                    if (buyOrder.getOrderPrice() >= sellOrder.getOrderPrice()) {
                        int executedQuantity = Math.min(buyOrder.getOrderQty(), sellOrder.getOrderQty());
                        if (sellOrder.getOrderType().equalsIgnoreCase("Market")){
                            System.out.println("Trade, price: " + buyOrder.getOrderPrice() + ", qty: " + executedQuantity);

                        } else {
                            System.out.println("Trade, price: " + sellOrder.getOrderPrice() + ", qty: " + executedQuantity);
                        }
                        // update quantities after match
                        buyOrder.updateOrder(buyOrder.getOrderPrice(), buyOrder.getOrderQty() - executedQuantity);
                        sellOrder.updateOrder(sellOrder.getOrderPrice(), sellOrder.getOrderQty() - executedQuantity);

                        // remove fully filled orders (which qty is equal to 0)
                        if (buyOrder.getOrderQty() == 0) {
                            buyIterator.remove(); // Remove the buy order using the iterator
                            orderMap.remove(buyOrder.getId());
                            ordersMatched = true; // change the flag to indicate that orders were matched
                            break; // exit the loop if buy order is fully filled and go back to outside loop to keep searching matches
                        }
                        if (sellOrder.getOrderQty() == 0) {
                            sellIterator.remove(); // remove the sell order using the iterator
                            orderMap.remove(sellOrder.getId());
                            ordersMatched = true;
                        }
                    }
                }
                if (ordersMatched) {
                    break; // exiting the loop if orders were matched
                }
            }
        } while (ordersMatched); // continue looking for matching orders until no more matches are found
    }

}
