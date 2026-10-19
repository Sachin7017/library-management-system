package Library.Management.System.controller;

import Library.Management.System.model.Borrowing;
import Library.Management.System.repository.BorrowingRepository;
import Library.Management.System.repository.LibrarySettingsRepository;
import Library.Management.System.model.LibrarySettings;
import Library.Management.System.service.BorrowingService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
@CrossOrigin
public class BorrowingController {

    private final BorrowingRepository borrowingRepository;
    private final BorrowingService borrowingService;
    private final LibrarySettingsRepository settingsRepository;

    public BorrowingController(
            BorrowingRepository borrowingRepository,
            BorrowingService borrowingService,
            LibrarySettingsRepository settingsRepository) {

        this.borrowingRepository = borrowingRepository;
        this.borrowingService = borrowingService;
        this.settingsRepository = settingsRepository;
    }

    @GetMapping
    public List<Borrowing> getAllBorrowings() {
        LibrarySettings settings = settingsRepository.findById(1L).orElseGet(() -> settingsRepository.save(new LibrarySettings()));
        LocalDate today = LocalDate.now();
        List<Borrowing> records = borrowingRepository.findAll();
        for (Borrowing item : records) {
            LocalDate effectiveDate = item.getReturnDate() != null ? item.getReturnDate() : today;
            long lateDays = item.getDueDate() == null || !effectiveDate.isAfter(item.getDueDate())
                    ? 0 : ChronoUnit.DAYS.between(item.getDueDate(), effectiveDate);
            item.setLateDays(lateDays);
            item.setFineAmount(lateDays * settings.getLateFinePerDay());
        }
        return records;
    }

    @PostMapping("/issue")
    public Borrowing issueBook(
            @RequestParam Long userId,
            @RequestParam Long bookId,
            @RequestParam String dueDate) {

        return borrowingService.issueBook(
                userId,
                bookId,
                LocalDate.parse(dueDate));
    }

    @PostMapping("/return/{borrowingId}")
    public Borrowing returnBook(@PathVariable Long borrowingId) {
        return borrowingService.returnBook(borrowingId);
    }
}