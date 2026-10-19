package Library.Management.System.repository;

import Library.Management.System.model.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    long countByUserIdAndStatus(Long userId, String status);
    boolean existsByBookIdAndStatus(Long bookId, String status);
}