# LimitOrderBook
Description:

A limit order book stores customer orders on a price time priority basis. The highest bid and lowest offer
are considered "best" with all other orders stacked in price levels behind. 

OrderBook class supports the following use-cases:

• Given an Order, add it to the OrderBook (order additions are expected to occur extremely frequently)

• Given an order id, remove an Order from the OrderBook (order deletions are expected to occur at approximately 60% of the rate of order additions)

• Given an order id and a new size, modify an existing order in the book to use the new size (size modifications do not affect time priority)

• Given a side and a level (an integer value >0) return the price for that level (where level 1 represents the best price for a given side). For example, given side=B and level=2 return the second best bid price.

• Given a side and a level return the total size available for that level

• Given a side return all the orders from that side of the book, in level- and time-order

Design: 
There are two classes:

Order: It represents an Order with id, price, size and size information.

OrderBook: This class contains Orders as those are placed, updated or removed.

The OrderBook class has three main data structures:

1.
Map<Long, Order> orderMap : This is a Hashmap with order id as key and Order as Value. This is required as while canceling or updating, the bid/offer information will not be present and Order needs to be found based on id.

2. and 3.
   
TreeMap<Double, Queue<Order>> bids

TreeMap<Double, Queue<Order>> offers

These are TreeMap with Price as Key and List of orders as value. A TreeMap instead of HashMap is used so the data is stored in order of prices. The bids are stored with reverseOrder comparator(Decreasing order) because the highest bid is considered best whereas offers are stored using default comparator(Increasing order) as lowest offer is considered best. 

The Orders for each side are stored as Queue because Queue implemented as LinkedList<> provides FIFO operation and fast insertion and removal. The orders need to be stored with time priority which means order received first should be processed first in the list.
