package Library.Management.System.service;

import Library.Management.System.model.Book;
import Library.Management.System.model.Borrowing;
import Library.Management.System.repository.BookRepository;
import Library.Management.System.repository.BorrowingRepository;
import Library.Management.System.repository.LibrarySettingsRepository;
import Library.Management.System.model.LibrarySettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final LibrarySettingsRepository settingsRepository;

    public BorrowingService(
            BorrowingRepository borrowingRepository,
            BookRepository bookRepository,
            LibrarySettingsRepository settingsRepository) {

        this.borrowingRepository = borrowingRepository;
        this.bookRepository = bookRepository;
        this.settingsRepository = settingsRepository;
    }

    @Transactional
    public Borrowing issueBook(Long userId, Long bookId, LocalDate dueDate) {

        LibrarySettings settings = settingsRepository.findById(1L).orElseGet(() -> settingsRepository.save(new LibrarySettings()));
        if (borrowingRepository.countByUserIdAndStatus(userId, "Issued") >= settings.getMaxBooksPerUser()) {
            throw new RuntimeException("This user has reached the maximum limit of " + settings.getMaxBooksPerUser() + " issued books.");
        }
        if (dueDate == null) dueDate = LocalDate.now().plusDays(settings.getIssueDurationDays());
        if (dueDate.isBefore(LocalDate.now())) throw new RuntimeException("Due date cannot be in the past.");

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getAvailableQuantity() <= 0) {
            throw new RuntimeException("Book is not available");
        }

        Borrowing borrowing = new Borrowing();

        borrowing.setUserId(userId);
        borrowing.setBookId(bookId);
        borrowing.setIssueDate(LocalDate.now());
        borrowing.setDueDate(dueDate);
        borrowing.setStatus("Issued");

        book.setAvailableQuantity(book.getAvailableQuantity() - 1);

        bookRepository.save(book);

        return borrowingRepository.save(borrowing);
    }

    @Transactional
    public Borrowing returnBook(Long borrowingId) {

        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new RuntimeException("Borrowing record not found"));

        if (!"Issued".equals(borrowing.getStatus())) {
            throw new RuntimeException("This book has already been returned");
        }

        Book book = bookRepository.findById(borrowing.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        borrowing.setReturnDate(LocalDate.now());
        borrowing.setStatus("Returned");

        book.setAvailableQuantity(book.getAvailableQuantity() + 1);

        bookRepository.save(book);

        return borrowingRepository.save(borrowing);
    }
}