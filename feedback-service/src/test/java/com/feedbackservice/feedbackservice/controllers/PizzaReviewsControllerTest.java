package com.feedbackservice.feedbackservice.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.util.UriComponentsBuilder;

import com.feedbackservice.feedbackservice.dtos.PizzaReviewsCreateDto;
import com.feedbackservice.feedbackservice.dtos.PizzaReviewsReturnsDto;
import com.feedbackservice.feedbackservice.dtos.PizzaReviewsUpdateDto;
import com.feedbackservice.feedbackservice.services.PizzaReviewsService;

@ExtendWith(MockitoExtension.class)
public class PizzaReviewsControllerTest {

    @Mock
    MessageSource messageSource;

    @Mock
    PizzaReviewsService pizzaReviewsService;

    @InjectMocks
    PizzaReviewsController pizzaReviewsController;

    private final UriComponentsBuilder uriComponentsBuilder = 
            UriComponentsBuilder.fromUriString("http://localhost");

    @Test
    @DisplayName("getAllReviews вернет список из PizzaReviewsReturnDto")
    void getAllReviews_ReturnsListPizzaReviewsReturnsDto() {
        //given
        var reviews = IntStream.range(0, 3)
                .mapToObj(i -> new PizzaReviewsReturnsDto(
                        (long) i, "Review %d".formatted(i), i+1, (long) i, null, null))
                .toList();
        
        doReturn(reviews)
                .when(this.pizzaReviewsService).getAllReviews();
        
        //when
        var result = this.pizzaReviewsController.getAllReviews();

        //then
        assertEquals(IntStream.range(0, 3)
                .mapToObj(i -> new PizzaReviewsReturnsDto(
                        (long) i, "Review %d".formatted(i), i+1, (long) i, null, null))
                .toList(), result);

        verify(this.pizzaReviewsService).getAllReviews();
        verifyNoMoreInteractions(this.pizzaReviewsService);
    }

    @Test
    @DisplayName("getAllReviewsByPizzaId вернет список из PizzaReviewsReturnsDto")
    void getAllReviewsByPizzaId_ReturnsListPizzaReviewsReturnsDto() {
        //given
        var reviews = List.of(
            new PizzaReviewsReturnsDto((long) 1, "Review 1", 5, (long) 1, null, null),
            new PizzaReviewsReturnsDto((long) 2, "Review 2", 5, (long) 2, null, null),
            new PizzaReviewsReturnsDto((long) 3, "Review 3", 5, (long) 1, null, null)
        );
        
        doReturn(reviews.stream().filter(review -> review.getPizzaId() == 1).toList())
                .when(this.pizzaReviewsService).getAllReviewsByPizzaId((long) 1);

        //when
        var result = this.pizzaReviewsController.getAllReviewsByPizzaId((long) 1);

        //then
        assertEquals(List.of(
                new PizzaReviewsReturnsDto((long) 1, "Review 1", 5, (long) 1, null, null),
                new PizzaReviewsReturnsDto((long) 3, "Review 3", 5, (long) 1, null, null)
                ), result);

        verify(this.pizzaReviewsService).getAllReviewsByPizzaId((long) 1);
        verifyNoMoreInteractions(this.pizzaReviewsService);
    }

    @Test
    @DisplayName("getReviewById вернет PizzaReviewsReturnDto")
    void getReviewById_ReviewIsExsists_ReturnsPizzaReviewsReturnsDto() {
        //given
        var pizzaReviewsReturnsDto = new PizzaReviewsReturnsDto(
            (long) 1, "Review 1", 5, (long) 1, null, null);
        
        doReturn(Optional.of(pizzaReviewsReturnsDto))
                .when(this.pizzaReviewsService).getReviewById((long) 1);

        //when
        var result = this.pizzaReviewsController.getReviewById((long) 1);

        //then
        assertEquals(new PizzaReviewsReturnsDto(
                (long) 1, "Review 1", 5, (long) 1, null, null), result);

        verify(this.pizzaReviewsService).getReviewById((long) 1);
        verifyNoMoreInteractions(this.pizzaReviewsService);
    }

    @Test
    @DisplayName("getReviewById выбросит ошибку NoSuchElementException")
    void getReviewById_ReviewIsNotExsists_ThrowNoSuchElementException() {
        //given

        //when
        var exception = assertThrows(NoSuchElementException.class, 
                () -> this.pizzaReviewsController.getReviewById((long) 1));

        //then
        assertEquals("{feedback.errors.404.not_found}", exception.getMessage());

        verify(this.pizzaReviewsService).getReviewById((long) 1);
        verifyNoMoreInteractions(this.pizzaReviewsService);
    }

    @Test
    @DisplayName("createReview вернет PizzaReviewsReturnsDto")
    void createReview_RequestIsValid_ReturnsPizzaReviewsReturnsDto() throws BindException {
        //given
        var pizzaReviewsCreateDto = new PizzaReviewsCreateDto(
                "Review 1", 1, (long) 1);
        var bindingResult = new MapBindingResult(Map.of(), "dto");

        var pizzaReviewsReturnsDto = new PizzaReviewsReturnsDto(
                (long) 1, "Review 1", 1, (long) 1, null, null);

        doReturn(pizzaReviewsReturnsDto)
                .when(this.pizzaReviewsService).createReview(
                        new PizzaReviewsCreateDto("Review 1", 1, (long) 1));

        //when
        var result = this.pizzaReviewsController.createReview(
                pizzaReviewsCreateDto, bindingResult, this.uriComponentsBuilder);

        //then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(URI.create("http://localhost/api/reviews/1"), result.getHeaders().getLocation());
        assertEquals(new PizzaReviewsReturnsDto(
                (long) 1, "Review 1", 1, (long) 1, null, null), result.getBody());

        verify(this.pizzaReviewsService).createReview(new PizzaReviewsCreateDto(
                "Review 1", 1, (long) 1));
        verifyNoMoreInteractions(this.pizzaReviewsService);
    }

    @Test
    @DisplayName("cereateReview вернет ProblemsDetails при не валидном запросе")
    void createReview_RequestIsInvalid_ThrowBindException() {
        //given
        var pizzaReviewsCreateDto = new PizzaReviewsCreateDto(
                "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, quis gravida magna mi a libero. Fusce vulputate eleifend sapien. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, metus. Nullam accumsan lorem in dui. Cras ultricies mi eu turpis hendrerit fringilla. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; In ac dui quis mi consectetuer lacinia. Nam pretium turpis et arcu. Duis arcu tortor, suscipit eget, imperdiet nec, imperdiet iaculis, ipsum. Sed aliquam ultrices mauris. Integer ante arcu, accumsan a, consectetuer eget, posuere ut, mauris. Praesent adipiscing. Phasellus ullamcorper ipsum rutrum nunc. Nunc nonummy metus. Vestibulum volutpat pretium libero. Cras id dui. Aenean ut eros et nisl sagittis vestibulum. Nullam nulla eros, ultricies sit amet, nonummy id, imperdiet feugiat, pede. Sed lectus. Donec mollis hendrerit risus. Phasellus nec sem in justo pellentesque facilisis. Etiam imperdiet imperdiet orci. Nunc nec neque. Phasellus leo dolor, tempus non, auctor et, hendrerit quis, nisi. Curabitur ligula sapien, tincidunt non, euismod vitae, posuere imperdiet, leo. Maecenas malesuada. Praesent congue erat at massa. Sed cursus turpis vitae tortor. Donec posuere vulputate arcu. Phasellus accumsan cursus velit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Sed aliquam, nisi quis porttitor congue, elit erat euismod orci, ac placerat dolor lectus quis orci. Phasellus consectetuer vestibulum elit. Aenean tellus metus, bibendum sed, posuere ac, mattis non, nunc. Vestibulum fringilla pede sit amet augue. In turpis. Pellentesque posuere. Praesent turpis. Aenean posuere, tortor sed cursus feugiat, nunc augue blandit nunc, eu sollicitudin urna dolor sagittis lacus. Donec elit libero, sodales nec, volutpat a, suscipit non, turpis. Nullam sagittis. Suspendisse pulvinar, augue ac venenatis condimentum, sem libero volutpat nibh, nec pellentesque velit pede quis nunc. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Fusce id purus. Ut varius tincidunt libero. Phasellus dolor. Maecenas vestibulum mollis diam.", 
                0, (long) 1);

        var bindingResult = new MapBindingResult(Map.of(), "dto");
        bindingResult.addError(
                new FieldError("dto", "review", "errors.review.size"));
        bindingResult.addError(
                new FieldError("dto", "rating", "errors.rating.min"));

        //when
        var exception = assertThrows(BindException.class, 
                () -> this.pizzaReviewsController.createReview(
                        pizzaReviewsCreateDto, bindingResult, this.uriComponentsBuilder));
        
        //then
        assertEquals(List.of(new FieldError("dto", "review", "errors.review.size"),
                new FieldError("dto", "rating", "errors.rating.min")), exception.getAllErrors());

        verifyNoInteractions(this.pizzaReviewsService);
    }
    
    @Test
    void updateReview_RequestIsValidAndReviewIsExsists_ReturnsVoid() throws BindException {
        //given
        var pizzaReviewsUpdateDto = new PizzaReviewsUpdateDto("Review 1", 5);
        var bindingResult = new MapBindingResult(Map.of(), "dto");

        //when 
        var result = this.pizzaReviewsController.updateReview((long) 1, pizzaReviewsUpdateDto, bindingResult);

        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        
        verify(this.pizzaReviewsService).updateReview((long) 1, 
                new PizzaReviewsUpdateDto("Review 1", 5));
        verifyNoMoreInteractions(this.pizzaReviewsService);
    }

    @Test
    void updateReview_RequestIsValidAndReviewIsNotExsists_ThrowNoSuchElementException() {
        //given
        var pizzaReviewsUpdateDto = new PizzaReviewsUpdateDto("Review 1", 5);
        var bindingResult = new MapBindingResult(Map.of(), "dto");

        doThrow(new NoSuchElementException("{feedback.errors.404.not_found}"))
                .when(this.pizzaReviewsService)
                        .updateReview((long) 2, new PizzaReviewsUpdateDto("Review 1", 5));

        //when
        var exception = assertThrows(NoSuchElementException.class, 
                () -> this.pizzaReviewsController.updateReview((long) 2, pizzaReviewsUpdateDto, bindingResult));

        //then
        assertEquals("{feedback.errors.404.not_found}", exception.getMessage());
        
        verify(this.pizzaReviewsService).updateReview((long) 2, 
                new PizzaReviewsUpdateDto("Review 1", 5));
        verifyNoMoreInteractions(this.pizzaReviewsService);
    }

    @Test
    void updateReview_RequestIsInValidAndPizzaIsExsists_ThrowBindException() {
        //given
        var pizzaReviewsUpdateDto = new PizzaReviewsUpdateDto("Review 1", 0);
        var bindingResult = new MapBindingResult(Map.of(), "dto");
        bindingResult.addError(
                new FieldError("dto", "rating", "errors.rating.min"));

        //when
        var exception = assertThrows(BindException.class, 
                () -> this.pizzaReviewsController.updateReview((long) 1, pizzaReviewsUpdateDto, bindingResult));

        //then
        assertEquals(List.of(
                new FieldError("dto", "rating", "errors.rating.min")), exception.getAllErrors());
        
        verifyNoInteractions(this.pizzaReviewsService);
    }

    @Test
    void updatePizza_RequestIsINvalidAndPizzaNotExsists_ThrowBindException() {
        //given
        var pizzaReviewsUpdateDto = new PizzaReviewsUpdateDto("Review 1", 0);
        var bindingResult = new MapBindingResult(Map.of(), "dto");
        bindingResult.addError(
                new FieldError("dto", "rating", "errors.rating.min"));

        //when
        var exception = assertThrows(BindException.class, 
                () -> this.pizzaReviewsController.updateReview((long) 2, pizzaReviewsUpdateDto, bindingResult));

        //then
        assertEquals(List.of(
                new FieldError("dto", "rating", "errors.rating.min")), exception.getAllErrors());
        
        verifyNoInteractions(this.pizzaReviewsService);
    }
}
