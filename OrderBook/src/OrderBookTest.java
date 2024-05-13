import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderBookTest {

    private OrderBook orderBook;

    @Before
    public void setUp() {
        orderBook = new OrderBook();
        orderBook.add(7, 100.0, 'B', 100000);
        orderBook.add(2, 99.5, 'B', 50000);
        orderBook.add(11, 101.0, 'O', 150000);
        orderBook.add(9, 102.0, 'O', 200000);
        orderBook.add(10, 102.0, 'O', 100000);
    }

    @Test
    public void testUpdateOrder() {
        orderBook.updateOrder(7, 200000);
        assertEquals(200000, orderBook.getOrder(7).getSize());
    }

    @Test
    public void testCancelOrder() {
        orderBook.cancelOrder(2);
        assertNull(orderBook.getOrder(2));
    }

    @Test
    public void testGetPriceForLevel(){
        assertNotNull(orderBook.getPriceForLevel('B', 1));
    }

    @Test
    public void testGetSizeForLevel(){
        assertEquals(300000, orderBook.getSizeForLevel('O', 2));
    }

    @Test
    public void testGetOrdersForSide(){
        assertNotNull(orderBook.getOrdersForSide('O'));
    }
    
    @Test
    public void testLog() {
        orderBook.log();
    }
}

