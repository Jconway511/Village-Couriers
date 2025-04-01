package com.example.villagecouriers;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import android.content.Context;
import android.content.res.AssetManager;

public class OrderActivityTest {
    private OrderActivity orderActivity;
    private Context mockContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        orderActivity = new OrderActivity();
        mockContext = Mockito.mock(Context.class);
        try {
            java.lang.reflect.Method method = AppCompatActivity.class.getDeclaredMethod("attachBaseContext", Context.class);
            method.setAccessible(true);
            method.invoke(orderActivity, mockContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadOrdersFromFile() throws Exception {
        String json = "[{\"id\":1,\"item_name\":\"Item1\",\"item_quantity\":\"2\",\"item_price\":\"10.0\",\"item_image\":\"image1.png\"}]";
        BufferedReader reader = new BufferedReader(new InputStreamReader(new java.io.ByteArrayInputStream(json.getBytes())));
        Mockito.when(mockContext.openFileInput("orders.json")).thenReturn(reader);

        List<ItemOrder> itemOrders = orderActivity.readOrdersFromFile();
        assertNotNull(itemOrders);
        assertEquals(1, itemOrders.size());
        assertEquals("Item1", itemOrders.get(0).getItem_name());
}
}

