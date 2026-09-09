package com.mpie.service2.service;

import com.mpie.service2.mapper.BookMapper;
import com.mpie.service2.model.Book;
import com.mpie.service2.model.BookDto;
import com.mpie.service2.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RentedBookServiceTest {

    RentedBookService rentedBookService;
    BookRepository bookRepository;
    BookMapper bookMapper;

    @BeforeEach // metoda oznaczona adnotacja Before Each wykona sie przed kazdym testem
    // czesto wiec jest ona uzywana do tworzenia instancji klasy ktora testuje oraz mockow
    void setup() {
        // wskazuje ze book repository ma byc mockiem sam bede mowil co ma zwracac
        this.bookRepository = Mockito.mock(BookRepository.class);
        this.bookMapper = Mappers.getMapper(BookMapper.class);
        this.rentedBookService = new RentedBookService(bookRepository, bookMapper);
    }

    // metodaKtoraTestuje_stanKtoryTestuje_CoSiePowinnStac
    @Test
    void listen_isAllowedCategory_BookSaved() {
        // given - sekcja w ktora tworze wszystkie instancje klas potzrebne do przeprowadzenia testu
        // oraz ustawiam co moje mocki maja zwrocic
        Book book = new Book("123", "X", "Y", "Fantasy", "Konrad");
        Book bookSaved = new Book("123", "X", "Y", "Fantasy", null);

        when(bookRepository.findById("123")).thenReturn(Optional.of(bookSaved));
        when(bookRepository.save(any())).thenReturn(bookSaved);
        // when - wykonanie testu -> czyli tutaj zazwyczaj mam 1 linijke wywolania metody ktora testuje
        rentedBookService.listen(book);
        // then - wykonanie asercji -> czyli weryfikacja czy wszystko zadziało się tak jak powinno
        assertEquals(book.getBorrower(), bookSaved.getBorrower());
    }

    @Test
    void getRentedBooks_BooksExist_BooksReturned() {
        // given - sekcja w ktora tworze wszystkie instancje klas potzrebne do przeprowadzenia testu
        // oraz ustawiam co moje mocki maja zwrocic
        Book book1 = new Book("123", "X1", "Y1", "Fantasy", "Konrad");
        Book book2 = new Book("124", "X2", "Y2", "Fantasy", "Konrad");
        Book book3 = new Book("125", "X3", "Y3", "Fantasy", "Konrad");

        PageImpl<Book> page = new PageImpl<>(List.of(book1, book2, book3));
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(page);

        // when - wykonanie testu -> czyli tutaj zazwyczaj mam 1 linijke wywolania metody ktora testuje
        List<BookDto> result = rentedBookService.getRentedBooks(PageRequest.of(1,2));

        // then - wykonanie asercji -> czyli weryfikacja czy wszystko zadziało się tak jak powinno
        assertAll(
                () -> assertEquals(3, result.size()),
                () -> assertEquals("123", result.get(0).isbn()),
                () -> assertEquals("124", result.get(1).isbn()),
                () -> assertEquals("125", result.get(2).isbn())
        );
    }


}