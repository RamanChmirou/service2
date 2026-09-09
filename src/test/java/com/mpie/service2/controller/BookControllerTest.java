package com.mpie.service2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpie.service2.model.BookDto;
import com.mpie.service2.service.RentedBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // <- przy pomocy adnotacji @SpringBootTest mowie ze chce aby w ramach tego testu zotala
// odpalona instancja aplikacji -> spowoduje to prawdziwe odpalenie aplikacji czyli w ramach testu beda prawdziwe beany
@AutoConfigureMockMvc  // <- wskazuje ze chce aby zostal mi dostaarczony bean dla MockMvc ktory bede mogl wstrzyknac w tescie
public class BookControllerTest {
    @Autowired // mowie ze chce wstrzyknac pod to pole beana
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean // chce aby jak aplikacja bedzie odpalana to do kontesktu springa (kontenr z beanami) wstrzykniety zostal
            // mock beana RentedBookService a nie prawdziwy serwis -> do kontenera z beanami trafi Mockito.mock(RentedBookService.class)
    RentedBookService rentedBookService;

    @Test
    void shouldGetRentedBooks() throws Exception {
        List<BookDto> bookDtos = List.of(
                new BookDto("1", "x", "y", "z", null),
                new BookDto("2", "x", "y", "z", "konrad"),
                new BookDto("3", "x", "y", "z", "przemek")
        );
        when(rentedBookService.getRentedBooks(any())).thenReturn(bookDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/book"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].isbn").value("1"))
                .andExpect(jsonPath("$[0].author").value("x"))
                .andExpect(jsonPath("$[0].title").value("y"))
                .andExpect(jsonPath("$[0].category").value("z"))
                .andExpect(jsonPath("$[1].isbn").value("2"))
                .andExpect(jsonPath("$[2].isbn").value("3"));
    }
}
