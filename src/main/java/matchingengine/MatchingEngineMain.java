package matchingengine;

import matchingengine.engine.OrderBook;
import matchingengine.model.LimitOrder;
import matchingengine.model.MarketOrder;
import matchingengine.model.Order;
import java.util.Scanner;

public class MatchingEngineMain {
    public static void main(String[] args) {

        OrderBook orderBook = new OrderBook();
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("Enter commands (limit, market, cancel, update) followed by parameters. Type 'exit' to quit.");

        while (true) {
            System.out.print("> ");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            // With this I can map input commands and direct to the right method

            String[] parts = input.split(" ");
            if (parts.length == 0) {
                continue;
            }

            String command = parts[0].toLowerCase();
            switch (command) {
                case "limit":
                    if (parts.length != 4) {
                        System.out.println("Usage: limit <Buy/Sell> <price> <quantity>");
                        break;
                    }
                    try {
                        String side = parts[1];
                        double price = Double.parseDouble(parts[2]);
                        int quantity = Integer.parseInt(parts[3]);
                        Order limitOrder = new LimitOrder(side, price, quantity);
                        if (side.equalsIgnoreCase("buy")) {
                            orderBook.buy(limitOrder);
                        } else if (side.equalsIgnoreCase("sell")) {

                            orderBook.sell(limitOrder);
                        } else {
                            System.out.println("Invalid order type. Use 'Buy' or 'Sell'.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid price or quantity format.");
                    }
                    break;
                case "market":
                    if (parts.length != 3) {
                        System.out.println("Usage: market <Buy/Sell> <quantity>");
                        break;
                    }
                    try {
                        String side = parts[1];
                        int quantity = Integer.parseInt(parts[2]);
                        Order marketOrder = new MarketOrder(side, quantity);
                        if (side.equalsIgnoreCase("buy")) {
                            orderBook.buy(marketOrder);
                        } else if (side.equalsIgnoreCase("sell")) {
                            orderBook.sell(marketOrder);
                        } else {
                            System.out.println("Invalid order side. Use 'Buy' or 'Sell'.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity format.");
                    }
                    break;
                case "cancel":
                    if (parts.length != 3) {
                        System.out.println("Usage: cancel order <orderId>");
                        break;
                    }
                    try {
                        long orderId = Long.parseLong(parts[2]);
                        if (orderBook.cancelOrder(orderId)) {
                            System.out.println("Order " + orderId + " canceled.");
                        } else {
                            System.out.println("Order " + orderId + " not found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid orderId format.");
                    }
                    break;
                case "update":
                    if (parts.length != 4) {
                        System.out.println("Usage: update <orderId> <newPrice> <newQuantity>");
                        break;
                    }
                    try {
                        long orderId = Long.parseLong(parts[1]);
                        double newPrice = Double.parseDouble(parts[2]);
                        int newQuantity = Integer.parseInt(parts[3]);
                        if (orderBook.updateOrder(orderId, newPrice, newQuantity)) {
                            System.out.println("Order " + orderId + " updated.");
                        } else {
                            System.out.println("Order " + orderId + " not found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid orderId, newPrice, or newQuantity format.");
                    }
                    break;
                default:
                    System.out.println("Unknown command. Available commands: limit, market, cancel, update.");
                    break;
            }
        }

        scanner.close();
    }
}
