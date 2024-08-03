package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import javax.net.ssl.SSLEngineResult;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    //2 test 1- addToCart 2-removeFromCart

   private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
  private  ItemRepository itemRepository=mock(ItemRepository.class);
    @Before
    public void setUp(){
        cartController=new CartController();
        TestUtils.injectObjects(cartController,"cartRepository",cartRepository);
        TestUtils.injectObjects(cartController,"userRepository",userRepository);
        TestUtils.injectObjects(cartController,"itemRepository",itemRepository);


    }
    @Test
    public void addToCartTest(){
        //prepeartion data
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

//mocking the behavior of calling repos
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        //call the function we going to test
        ResponseEntity<Cart> cartResponseEntity= cartController.addTocart(req);
        Cart returnedCart= cartResponseEntity.getBody();
        Item returnedItem= returnedCart.getItems().get(0);
        User returnedUser=returnedCart.getUser();
        //assertions
        //status code assertion
        Assertions.assertThat(cartResponseEntity.getStatusCodeValue())
                .isEqualTo(200);

        //assertions on user
        Assertions.assertThat(returnedUser.getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(returnedUser.getId()).isEqualTo(user.getId());

        //cart assertion
        Assertions.assertThat(returnedCart.getId()).isEqualTo(cart.getId());
        Assertions.assertThat(returnedCart.getTotal()).isEqualTo(cart.getTotal());

    }
    @Test
    public void addToCartWithWrongUserName(){
        //prepeartion data
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
                .username("Haleema")
                .build();

//mocking the behavior of calling repos
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        //call the function we're going to test
        ResponseEntity<Cart> cartResponseEntity= cartController.addTocart(req);
        Cart returnedCart= cartResponseEntity.getBody();
       // Item returnedItem= returnedCart.getItems().get(0);
       // User returnedUser=returnedCart.getUser();
        //assertions
        //status code assertion
        Assertions.assertThat(cartResponseEntity.getStatusCodeValue())
                .isEqualTo(404);


    }

    @Test
    public void addToCartWithWrongItemId(){
        //prepeartion data
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
                .itemId(5L)
                .username("test")
                .build();

//mocking the behavior of calling repos
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        //call the function we going to test
        ResponseEntity<Cart> cartResponseEntity= cartController.addTocart(req);
        Cart returnedCart= cartResponseEntity.getBody();
        // Item returnedItem= returnedCart.getItems().get(0);
        // User returnedUser=returnedCart.getUser();
        //assertions
        //status code assertion
        Assertions.assertThat(cartResponseEntity.getStatusCodeValue())
                .isEqualTo(404);


    }
    @Test
    public void removeFromCartTest(){

            //prepeartion data
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

//mocking the behavior of calling repos
            when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
            when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

            //call the function we going to test
            ResponseEntity<Cart> cartResponseEntity= cartController.removeFromcart(req);
            Cart returnedCart= cartResponseEntity.getBody();
            // Item returnedItem= returnedCart.getItems().get(0);
            // User returnedUser=returnedCart.getUser();
            //assertions
            //status code assertion
            Assertions.assertThat(cartResponseEntity.getStatusCodeValue())
                    .isEqualTo(200);
 }

}
