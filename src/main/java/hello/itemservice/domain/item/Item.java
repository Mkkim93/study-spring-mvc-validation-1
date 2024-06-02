package hello.itemservice.domain.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class Item {

    private Long id;

    @NotBlank // message 별도 지정 가능
    private String itemName;

    @NotNull // message 별도 지정 가능
    @Range(min = 1000, max = 1000000) // message 별도 지정 가능
    private Integer price;

    @NotNull
    @Range(max = 9999)
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }



}
