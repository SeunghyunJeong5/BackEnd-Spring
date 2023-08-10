package com.mysite.shop.order;

import com.mysite.shop.order.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemDto {

    public OrderItemDto(OrderItem orderItem, String imgUrl){
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }

    private String itemNm; //상품명	item 테이블
    private int count; //주문 수량	order 테이블
    
    private int orderPrice; //주문 금액	item 테이블
    private String imgUrl; //상품 이미지 경로	item-img 테이블

}