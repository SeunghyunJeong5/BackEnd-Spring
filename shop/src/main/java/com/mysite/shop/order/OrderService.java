package com.mysite.shop.order;

import com.mysite.shop.order.OrderDto;
import com.mysite.shop.entity.*;
import com.mysite.shop.item.ItemRepository;
import com.mysite.shop.member.Member;
import com.mysite.shop.member.MemberRepository;
import com.mysite.shop.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.mysite.shop.order.OrderHistDto;
import com.mysite.shop.order.OrderItemDto;
import com.mysite.shop.item.Item;
import com.mysite.shop.item.ItemImg;
import com.mysite.shop.item.ItemImgRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.thymeleaf.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;

    private final MemberRepository memberRepository;

    private final OrderRepository orderRepository;

    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email){

        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {

        List<Order> orders = orderRepository.findOrders(email, pageable); //email을 가지고 사용자가 주문한 정보를 갖고있는 list
        Long totalCount = orderRepository.countOrder(email);				//email을 가지고 order list를 만든다든것

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order); //주문객체리스트를 받아와서 주문객체들을 기반으로 주문내역을 만듦(바깥쪽 for는 주문id, 날짜, 상태를 루프돌려서 끌어옴)
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {							//안쪽의 for 문은 하나의 order에 대한 안에 아이템들의 정보를 루프돌림
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn
                        (orderItem.getItem().getId(), "Y");		//대표이미지 가져옴
                OrderItemDto orderItemDto =
                        new OrderItemDto(orderItem, itemImg.getImgUrl());		//상품명, 수량, 금액, 이미지경로 가져옴
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }

        return true;
    }

    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

    
    
    // 장바구니에서 값을 읽어와서 주문 테이블에 저장하는 메소드(item id, count, email 저장)
    public Long orders(List<OrderDto> orderDtoList, String email){

        Member member = memberRepository.findByEmail(email);
        
        //orderItemList 선언 : Order : order_item 
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderDto orderDto : orderDtoList) {
            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);	//order,item까지 같이 save됨. 매핑되어있어서

        return order.getId();
    }

}