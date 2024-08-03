package com.example.demo.controllers;


import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    //submit and getOrderForUser

    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository=mock(OrderRepository.class);
    @Before
    public void setUp(){
        orderController=new OrderController();
        TestUtils.injectObjects(orderController,"userRepository",userRepository);
        TestUtils.injectObjects(orderController,"orderRepository",orderRepository);

    }
    @Test
    public void submitTestHappy(){
        User user= User.builder().username("test").build();

        Item item= Item.builder().description("A widget that is round")
                .price(BigDecimal.valueOf(2.99))
                .name("Round Widget")
                .id(1L)
                .build();
        List<Item> items= new ArrayList<>();
        items.add(item);
        Cart cart= Cart.builder().id(1L)
                .items( items)
                .user(user)
                .total(item.getPrice())
                .build();
        //since we cant add cart without user and vs
        user.setCart(cart);
        ModifyCartRequest req= ModifyCartRequest.builder()
                .quantity(1)
                .itemId(1L)
                .username("test")
                .build();

        //mocking
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        //to be tested woth the returned from submit

        UserOrder order= UserOrder.createFromCart(cart);

        //call finction we want to test
        ResponseEntity<UserOrder> returnedOrderResponse= orderController.submit("test");

        Assertions.assertEquals(200,returnedOrderResponse.getStatusCode().value());

        UserOrder returnedOrder=returnedOrderResponse.getBody();
        Assertions.assertEquals(order.getTotal(),returnedOrder.getTotal());



    }

    @Test
    public void submitTestBad(){
        User user= User.builder().username("test").build();

        Item item= Item.builder().description("A widget that is round")
                .price(BigDecimal.valueOf(2.99))
                .name("Round Widget")
                .id(1L)
                .build();
        List<Item> items= new ArrayList<>();
        items.add(item);
        Cart cart= Cart.builder().id(1L)
                .items( items)
                .user(user)
                .total(item.getPrice())
                .build();
        //since we cant add cart without user and vs
        user.setCart(cart);
        ModifyCartRequest req= ModifyCartRequest.builder()
                .quantity(1)
                .itemId(1L)
                .username("test")
                .build();

        //mocking
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        //to be tested woth the returned from submit

        UserOrder order= UserOrder.createFromCart(cart);

        //call finction we want to test
        ResponseEntity<UserOrder> returnedOrderResponse= orderController.submit("Haleema");

        Assertions.assertEquals(404,returnedOrderResponse.getStatusCode().value());

        UserOrder returnedOrder=returnedOrderResponse.getBody();
      //  Assertions.assertEquals(order.getTotal(),returnedOrder.getTotal());



    }

    @Test
    public void getOrderForUser(){
        User user= User.builder().username("test").build();

        Item item= Item.builder().description("A widget that is round")
                .price(BigDecimal.valueOf(2.99))
                .name("Round Widget")
                .id(1L)
                .build();
        List<Item> items= new ArrayList<>();
        items.add(item);
        Cart cart= Cart.builder().id(1L)
                .items( items)
                .user(user)
                .total(item.getPrice())
                .build();
        //since we cant add cart without user and vs
        user.setCart(cart);
        ModifyCartRequest req= ModifyCartRequest.builder()
                .quantity(1)
                .itemId(1L)
                .username("test")
                .build();

        //mocking
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        UserOrder order= UserOrder.createFromCart(cart);

        ResponseEntity<List<UserOrder>> returnedOrderResponse= orderController.getOrdersForUser("test");
//UserOrder returnedOrder= (UserOrder) returnedOrderResponse.getBody();
        Assertions.assertEquals(200,returnedOrderResponse.getStatusCode().value());
       // Assertions.assertEquals(order.getTotal(),returnedOrder.getTotal());

    }
}