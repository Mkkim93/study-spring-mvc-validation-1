package hello.itemservice.domain.item;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class Item {

    /**
     * 수정 요구 사항이 추가되어 제품을 등록할때와 수정할때의 제약조건이 다르게 설정 groups 사용
     *
     * ** 방법 : 인터페이스 구분 후 제약조건을 필드마다 다르게 설정
     * 1. UpdateCheck.class : 제품을 수정할 때
     * 2. SaveCheck.class : 제품을 등록 할 때
     */

//    @NotNull(groups = UpdateCheck.class) // 제품 수정 시에만 에러
    private Long id;

//    @NotNull(groups = {SaveCheck.class, UpdateCheck.class}) // 제품 수정 및 등록 시 모두 에러
    private String itemName;

//    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
//    @Range(min = 1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class})
    private Integer price;

//    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
//    @Max(value = 9999, groups = SaveCheck.class)
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }



}
