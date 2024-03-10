package jpabook.rejpashop.controller;

import jpabook.rejpashop.domain.item.Book;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {

    private  Long id;

    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;

    public void bookToBookForm(Book item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.stockQuantity = item.getStockQuantity();
        this.author = item.getAuthor();
        this.isbn = item.getIsbn();
    }
}
