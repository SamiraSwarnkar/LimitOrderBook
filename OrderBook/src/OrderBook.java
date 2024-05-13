import java.util.*;

public class OrderBook 
{
    private TreeMap<Double, Queue<Order>> bids;
    private TreeMap<Double, Queue<Order>> offers;
    private Map<Long, Order> orderMap;

    public OrderBook() 
    {
        bids = new TreeMap<>(Comparator.reverseOrder());
        offers = new TreeMap<>();
        orderMap = new HashMap<>();
    }

    //Given an Order, add it to the OrderBook
    public void add(long id, double price, char side, int size) 
    {
        if(side == 'B')
            addOrder(bids, id, price, 'B', size);
        else
            addOrder(offers, id, price, 'O', size);
    }

    private void addOrder(TreeMap<Double, Queue<Order>> side, long id, double price, char s, int size) 
    {
        Queue<Order> orders = side.getOrDefault(price, new LinkedList<>());
        Order newOrder = new Order(id, price, s, size);
        orders.add(newOrder);
        side.put(price, orders);
        orderMap.put(id, newOrder);
    }

    //Get order by id
    public Order getOrder(long orderId)
    {
        return orderMap.get(orderId);
    }
    
    //Given an order id and a new size, modify an existing order in the book to use the new size 
    //size modications do not affect time priority
    public void updateOrder(long orderId, int newsize) 
    {
        Order order = orderMap.get(orderId);
        if (order != null) {
            order.setSize(newsize); 
        } else 
        {
            System.out.println("\n UpdateOrder: Order not found");
        }
    }

    //Given an order id, remove an Order from the OrderBook
    public void cancelOrder(long orderId) 
    {
        Order order = orderMap.get(orderId);
        if (order != null) 
        {
            TreeMap<Double, Queue<Order>> side = order.getSide() == 'B' ? bids : offers;
            Queue<Order> orders = side.get(order.getPrice());
            orders.remove(order);
            if (orders.isEmpty()) {
                side.remove(order.getPrice());
            }
            orderMap.remove(orderId);
        } else 
        {
            System.out.println("\n CancelOrder: Order not found");
        }
    }

    //Given a side and a level (an integer value >0) return the price for that level 
    //(where level 1 represents the best price for a given side)
    public double getPriceForLevel(char s, int level)
    {
        int currlevel =0;
        double price=0;
        TreeMap<Double, Queue<Order>> side = s == 'B' ? bids : offers;
        for (Map.Entry<Double, Queue<Order>> entry : side.entrySet()) 
        {
            ++currlevel;
            if(level == currlevel)
            {
                price = entry.getKey();
                break;
            }
        }
        if(price == 0)
            System.out.println("No price found");
        return price;
    }

    //Given a side and a level return the total size available for that level
    public long getSizeForLevel(char s, int level)
    {
        int currlevel =0;
        long size=0;
        TreeMap<Double, Queue<Order>> side = s == 'B' ? bids : offers;
        for (Map.Entry<Double, Queue<Order>> entry : side.entrySet()) 
        {
            ++currlevel;
            if(level == currlevel)
            {
                for(Order order : entry.getValue()) 
                {
                    size=size+order.getSize();
                }
                break;
            }
        }
        if(size == 0)
            System.out.println("No orders found");
        return size;
    }

    //Given a side return all the orders from that side of the book, in level- and time-order
    public List<Order> getOrdersForSide(char s)
    {
        List<Order> orders = new ArrayList<>();
        int i=0;
        TreeMap<Double, Queue<Order>> side = s == 'B' ? bids : offers;
        for (Map.Entry<Double, Queue<Order>> entry : side.entrySet()) 
        {
            for(Order order : entry.getValue()) 
            {
                orders.add(order);
            }
        }
        return orders;
    }

    public void log() {
        System.out.println("Bids:");
        for (Map.Entry<Double, Queue<Order>> entry : bids.entrySet())
        {
            System.out.println("Price: " + entry.getKey());
            for (Order order : entry.getValue()) 
            {
                System.out.println("  " + order);
            }
        }

        System.out.println("Offers:");
        for (Map.Entry<Double, Queue<Order>> entry : offers.entrySet()) 
        {
            System.out.println("Price: " + entry.getKey());
            for (Order order : entry.getValue()) 
            {
                System.out.println("  " + order);
            }
        }
    }

    public static void main(String[] args) {
        OrderBook orderBook = new OrderBook();
        orderBook.add(7, 100.0, 'B', 100000);
        orderBook.add(2, 99.5, 'B', 50000);
        orderBook.add(11, 101.0, 'O', 150000);
        orderBook.add(9, 102.0, 'O', 200000);
        orderBook.add(10, 102.0, 'O', 100000);


        System.out.println("Initial Order Book:");
        orderBook.log();

        orderBook.updateOrder(7, 200000);
        System.out.println("\nAfter Updating Order 7:");
        orderBook.log();

        orderBook.cancelOrder(2);
        System.out.println("\nAfter Canceling Order 2:");
        orderBook.log();

        double price = orderBook.getPriceForLevel('B', 1);
        System.out.println("\nBid Price for Level 1:"+price);

        price = orderBook.getPriceForLevel('O', 2);
        System.out.println("\nOffer Price for Level 2:"+price);

        long size=orderBook.getSizeForLevel('O', 2);
        System.out.println("\nOffer total size for Level 2:"+size);

        System.out.println("\nAll orders in Offer side:");
        List<Order> orders = orderBook.getOrdersForSide('O');
        for (Order order : orders) {
            System.out.println(order);
        }
    }
}

