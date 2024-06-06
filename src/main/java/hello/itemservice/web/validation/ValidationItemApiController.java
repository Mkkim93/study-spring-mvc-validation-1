package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.web.validation.form.ItemSaveForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {

        log.info("API 컨트롤러 호출");
        // api 로 호출 시 ItemSaveForm 에 대한 객체를 호출 한다. 이 때 컨트롤러가 동작한다
        // 컨트롤러 동작하지 않는 경우 : 호출하려는 ItemSaveForm 객체 타입이 맞지 않을 경우 Integer price != String price 값 입력
        // ex ) 현재 ItemSaveForm 의 price 는 Integer 로 선언 되었다 그러나 호출 시 문자열 "qq" 로 호출하게 되면 컨트롤가 자체가 동작하지 않고 .DefaultMessageSourceResolvable 에러가 발생한다.


        if (bindingResult.hasErrors()) {
            log.info("검증 오류 발생 errors={}", bindingResult);
            return bindingResult.getAllErrors();
        }

        log.info("성공 로직 실행");

        return form;
    }

}
