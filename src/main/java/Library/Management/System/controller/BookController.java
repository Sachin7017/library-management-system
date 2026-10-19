package Library.Management.System.controller;

import Library.Management.System.model.Book;
import Library.Management.System.repository.BookRepository;
import org.springframework.web.bind.annotation.*;
import Library.Management.System.repository.BorrowingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin
public class BookController {

    private final BookRepository bookRepository;
private final BorrowingRepository borrowingRepository;

public BookController(
        BookRepository bookRepository,
        BorrowingRepository borrowingRepository) {

    this.bookRepository = bookRepository;
    this.borrowingRepository = borrowingRepository;
}

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @PostMapping
    public Book addBook(@RequestBody Book book) {

        if (book.getTitle() == null || book.getTitle().isBlank()
                || book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new IllegalArgumentException("Book title and author are required.");
        }

        if (book.getQuantity() == null || book.getQuantity() < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }

        book.setAvailableQuantity(book.getQuantity());

        return bookRepository.save(book);
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String keyword) {
        return bookRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                        keyword, keyword);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (updatedBook.getTitle() == null || updatedBook.getTitle().isBlank()
                || updatedBook.getAuthor() == null || updatedBook.getAuthor().isBlank()) {
            throw new IllegalArgumentException("Book title and author are required.");
        }

        if (updatedBook.getQuantity() == null || updatedBook.getQuantity() < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }

        int borrowedQuantity = book.getQuantity() - book.getAvailableQuantity();

        if (updatedBook.getQuantity() < borrowedQuantity) {
            throw new IllegalArgumentException(
                    "Quantity cannot be less than the number of books currently borrowed.");
        }

        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setCategory(updatedBook.getCategory());
        book.setIsbn(updatedBook.getIsbn());
        book.setQuantity(updatedBook.getQuantity());
        book.setAvailableQuantity(updatedBook.getQuantity() - borrowedQuantity);

        return bookRepository.save(book);
    }
    @DeleteMapping("/{id}")
public void deleteBook(@PathVariable Long id) {

    Book book = bookRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Book not found"));

    boolean currentlyBorrowed =
            borrowingRepository.existsByBookIdAndStatus(id, "Issued");

    if (currentlyBorrowed) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "This book cannot be deleted because it is currently issued.");
    }

    long borrowingHistoryCount = borrowingRepository.findAll()
            .stream()
            .filter(borrowing -> id.equals(borrowing.getBookId()))
            .count();

    if (borrowingHistoryCount > 0) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "This book has borrowing history and cannot be permanently deleted.");
    }

    bookRepository.delete(book);
}
}