package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;


@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @InitBinder // 컨트롤러 내에서 항상 먼저 검증을 시작 (@InitBinder <- @Validated)
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(itemValidator);
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    @PostMapping("/add") // reject, rejectValue : 에러코드의 경로를 찾아준다.
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes, Model model) {

        // 검증 로직 생략 가능 이유 : 상단에 @InitBinder 를 검증 로직으로 선언하고 메서드가 실행될 때 항상 동작 하기 때문에
        // addItemV6() 메서드 내부에 @Validated 어노테이션을 정의하면 해당 메서드가 실행 될때 불러온 검증 로직이 동작한다.
        // itemValidator.validate(item, bindingResult);


        log.info("objectName={}", bindingResult.getObjectName()); // ObjectName = item
        log.info("target={}", bindingResult.getTarget()); // Target = Item(id, itemName, price, quantity)


        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) { // -> error 가 있다면
            log.info("errors={}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    // @PostMapping("/add") // reject, rejectValue : 에러코드의 경로를 찾아준다.
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes, Model model) {

        // 검증 로직 Validator 의 메서드를 호출 하였다.
        itemValidator.validate(item, bindingResult);


        log.info("objectName={}", bindingResult.getObjectName()); // ObjectName = item
        log.info("target={}", bindingResult.getTarget()); // Target = Item(id, itemName, price, quantity)




        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) { // -> error 가 있다면
            log.info("errors={}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }


////    @PostMapping("/add")
//    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult,// BindingResult 객체는 모델 객체(Item) 다음으로 정의 해야한다.
//                            RedirectAttributes redirectAttributes, Model model) {
//
//        // 오류 결과를 담는 객체 (검증 오류 결과 보관) = BindingResult 로 생성
//
//
//        // 검증 로직
//        // 1) hashText() : 주어진 문자열이 null 이 아니고, 길이가 0보다 크고, 공백이 아닌 문자가 포함되어 있는지를 확인합니다.
//        if (!StringUtils.hasText(item.getItemName())) { // -> itemName 이 hashText() 의 '부정'이면 아래 에러 메세시를 출력
////            errors.put("itemName", "상품 이름은 필수입니다.");
//            bindingResult.addError(new FieldError("item", "itemName","상품 이름은 필수 입니다." ));
//        }
//
//        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
////            errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용 합니다.");
//            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용 합니다."));
//        }
//
//        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
////            errors.put("quantity", "수량은 최대 9,999 까지 허용 합니다.");
//            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용 합니다."));
//        }
//
//        // 특정 필드가 아닌 복할 룰 검증
//        if (item.getPrice() != null && item.getQuantity() != null) {
//            int resultPrice = item.getPrice() * item.getQuantity();
//            if (resultPrice < 10000) {
////                errors.put("globalError", "가격 * 수량의 합은 10,000 원 이상이어야 합니다. 현재 값 = " + resultPrice);
//                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000 원 이상이어야 합니다. 현재 값 = " + resultPrice));
//            }
//        }
//
//        // 검증에 실패하면 다시 입력 폼으로
//        if (bindingResult.hasErrors()) { // -> error 가 있다면
//            log.info("errors={}", bindingResult);
////            model.addAttribute("errors", errors); bindingResult 는 모델이 담을 필요 없음 (뷰로 바로 넘어감)
//            return "validation/v2/addForm";
//        }
//
//        // 성공 로직
//        Item savedItem = itemRepository.save(item);
//        redirectAttributes.addAttribute("itemId", savedItem.getId());
//        redirectAttributes.addAttribute("status", true);
//        return "redirect:/validation/v2/items/{itemId}";
//    }
//
////    @PostMapping("/add") // 검증로직 후 입력한 데이터 값이 남아있도록 설정
//    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult,// BindingResult 객체는 모델 객체(Item) 다음으로 정의 해야한다.
//                            RedirectAttributes redirectAttributes, Model model) {
//
//        // 오류 결과를 담는 객체 (검증 오류 결과 보관) = BindingResult 로 생성
//
//        if (!StringUtils.hasText(item.getItemName())) {
//            // FieldError() 내부에 있는 codes 와 arguments 는 오류 발생 시 오류 코드로 메시지를 찾기 위해 사용 된다.
//            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수 입니다."));
//        }
//
//        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
//
//            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~ 1,000,000 까지 허용 합니다."));
//        }
//
//        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
//
//            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 최대 9,999 까지 허용 합니다."));
//        }
//
//        // 특정 필드가 아닌 복할 룰 검증
//        if (item.getPrice() != null && item.getQuantity() != null) {
//            int resultPrice = item.getPrice() * item.getQuantity();
//            if (resultPrice < 10000) {
//
//                bindingResult.addError(new ObjectError("item", null, null,"가격 * 수량의 합은 10,000 원 이상이어야 합니다. 현재 값 = " + resultPrice));
//            }
//        }
//
//        // 검증에 실패하면 다시 입력 폼으로
//        if (bindingResult.hasErrors()) { // -> error 가 있다면
//            log.info("errors={}", bindingResult);
//            return "validation/v2/addForm";
//        }
//
//        // 성공 로직
//        Item savedItem = itemRepository.save(item);
//        redirectAttributes.addAttribute("itemId", savedItem.getId());
//        redirectAttributes.addAttribute("status", true);
//        return "redirect:/validation/v2/items/{itemId}";
//    }
//
////    @PostMapping("/add") // errors.properties 활용
//    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult,// BindingResult 객체는 모델 객체(Item) 다음으로 정의 해야한다.
//                            RedirectAttributes redirectAttributes, Model model) { // 오류 결과를 담는 객체 (검증 오류 결과 보관) = BindingResult 로 생성
//
//        log.info("objectName={}", bindingResult.getObjectName()); // ObjectName = item
//        log.info("target={}", bindingResult.getTarget()); // Target = Item(id, itemName, price, quantity)
//
//        if (!StringUtils.hasText(item.getItemName())) {
//            // FieldError() 내부에 있는 codes 와 arguments 는 오류 발생 시 오류 코드로 메시지를 찾기 위해 사용 된다.
//            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
//        }
//
//        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
//
//            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
//        }
//
//        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
//
//            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
//        }
//
//        // 특정 필드가 아닌 복할 룰 검증
//        if (item.getPrice() != null && item.getQuantity() != null) {
//            int resultPrice = item.getPrice() * item.getQuantity();
//            if (resultPrice < 10000) {
//
//                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice},null));
//            }
//        }
//
//        // 검증에 실패하면 다시 입력 폼으로
//        if (bindingResult.hasErrors()) { // -> error 가 있다면
//            log.info("errors={}", bindingResult);
//            return "validation/v2/addForm";
//        }
//
//        // 성공 로직
//        Item savedItem = itemRepository.save(item);
//        redirectAttributes.addAttribute("itemId", savedItem.getId());
//        redirectAttributes.addAttribute("status", true);
//        return "redirect:/validation/v2/items/{itemId}";
//    }
//
//    @PostMapping("/add") // reject, rejectValue : 에러코드의 경로를 찾아준다.
//    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult,
//                            RedirectAttributes redirectAttributes, Model model) {
//
//        log.info("objectName={}", bindingResult.getObjectName()); // ObjectName = item
//        log.info("target={}", bindingResult.getTarget()); // Target = Item(id, itemName, price, quantity)
//
//        // 아래 if 조건문 과 동일한 코드 : 공백과 같은 단순한 기능만 제공 (복잡한 조건은 if 문을 사용하자)
//        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
//
////       if (!StringUtils.hasText(item.getItemName())) { // 문자 입력이 누락될 때 오류 메세지 출력
////            bindingResult.rejectValue("itemName", "required");
////    }
//         if (item.getItemName().contains(" ")) { // (띄어쓰기 X) 띄어쓰기 입력 시 오류 메세지 출력
//            bindingResult.rejectValue("itemName", "notEmpty");
//        }
//
//        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
//
//            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
//        }
//
//        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
//
//            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
//        }
//
//        // 특정 필드가 아닌 복할 룰 검증
//        if (item.getPrice() != null && item.getQuantity() != null) {
//            int resultPrice = item.getPrice() * item.getQuantity();
//            if (resultPrice < 10000) {
//
////           bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice},null));
//                // ObjectError 의 경우 이미 객체의 이름을 알고 있기 때문에 생략 하고 에러 코드인(totalPriceMin)만 호출하면 된다.
//                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
//            }
//        }
//
//        // 검증에 실패하면 다시 입력 폼으로
//        if (bindingResult.hasErrors()) { // -> error 가 있다면
//            log.info("errors={}", bindingResult);
//            return "validation/v2/addForm";
//        }
//
//        // 성공 로직
//        Item savedItem = itemRepository.save(item);
//        redirectAttributes.addAttribute("itemId", savedItem.getId());
//        redirectAttributes.addAttribute("status", true);
//        return "redirect:/validation/v2/items/{itemId}";
//    }

}

