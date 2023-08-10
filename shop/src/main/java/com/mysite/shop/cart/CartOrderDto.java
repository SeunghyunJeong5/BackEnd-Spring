package com.mysite.shop.cart;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartOrderDto {

    private Long cartItemId;

    private List<CartOrderDto> cartOrderDtoList;	//뒤의 cartOrderDtoList는 클라이언트에서 넘어오는 값임.
    												//그걸 다시 CartOrderDto 라는 리스트에 넣는거임.(같은 리스트형식임)

}