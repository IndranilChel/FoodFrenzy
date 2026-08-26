package com.foodfrenzy.foodfrenzyapp.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foodfrenzy.foodfrenzyapp.entities.Orders;
import com.foodfrenzy.foodfrenzyapp.entities.User;
import com.foodfrenzy.foodfrenzyapp.repositories.OrderRepository;

import java.util.List;

@Component
public class OrderServices
{
    @Autowired
    private OrderRepository orderRepository;
    public List<Orders> getOrders()
    {
        List<Orders> list=this.orderRepository.findAll();
        return list;
    }
    public void saveOrder(Orders order)
    {
        this.orderRepository.save(order);
    }

    public void updateOrder(int id,Orders order)
    {
        order.setOrderId(id);
        this.orderRepository.save(order);

    }

    public void deleteOrder(int id)
    {
        this.orderRepository.deleteById(id);
    }

    public List<Orders> getOrdersForUser(User user) {
        return orderRepository.findOrdersByUser(user);
    }

    public Orders getOrderById(int id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void updateOrder(Orders order) {
        orderRepository.save(order);
    }
}
