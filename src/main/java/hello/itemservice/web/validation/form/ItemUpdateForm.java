package hello.itemservice.web.validation.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.Range;

@Data
public class ItemUpdateForm {


    @NotNull
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    // 수정 에서는 수량에 대한 제약 조건이 없다 (자유롭게 변경 가능)
    private Integer quantity;
}
