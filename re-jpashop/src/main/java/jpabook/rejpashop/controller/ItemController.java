package jpabook.rejpashop.controller;

import jpabook.rejpashop.domain.item.Book;
import jpabook.rejpashop.domain.item.Item;
import jpabook.rejpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    
    private final ItemService itemService;
    
    @GetMapping("/items/save")
    public String createForm(@RequestParam(required = false) Long id, Model model) {

        BookForm bookForm = new BookForm();

        if (id != null) {
            Book item = (Book) itemService.findOne(id);
            bookForm.bookToBookForm(item);
        }

        model.addAttribute("form", bookForm);
        return "items/saveItemForm";
    }

    @ResponseBody
    @PostMapping("/items/save")
    public String create(@RequestBody BookForm bookForm) {
        itemService.saveItem(Book.createBook(bookForm));
        return "redirect:/items";
    }
    
    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/list";
    }

}
