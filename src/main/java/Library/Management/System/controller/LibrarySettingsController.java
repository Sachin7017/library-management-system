package Library.Management.System.controller;

import Library.Management.System.model.LibrarySettings;
import Library.Management.System.repository.LibrarySettingsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin
public class LibrarySettingsController {
    private final LibrarySettingsRepository repository;
    public LibrarySettingsController(LibrarySettingsRepository repository) { this.repository = repository; }

    @GetMapping
    public LibrarySettings getSettings() {
        return repository.findById(1L).orElseGet(() -> repository.save(new LibrarySettings()));
    }

    @PutMapping
    public LibrarySettings updateSettings(@RequestBody LibrarySettings incoming) {
        if (incoming.getLibraryName() == null || incoming.getLibraryName().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Library name is required.");
        if (incoming.getIssueDurationDays() < 1 || incoming.getIssueDurationDays() > 365)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Issue duration must be between 1 and 365 days.");
        if (incoming.getMaxBooksPerUser() < 1 || incoming.getMaxBooksPerUser() > 100)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximum books must be between 1 and 100.");
        if (incoming.getLateFinePerDay() < 0 || incoming.getLateFinePerDay() > 100000)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fine must be zero or more.");
        LibrarySettings saved = repository.findById(1L).orElseGet(LibrarySettings::new);
        saved.setId(1L);
        saved.setLibraryName(incoming.getLibraryName().trim());
        saved.setEmail(incoming.getEmail() == null ? "" : incoming.getEmail().trim());
        saved.setPhone(incoming.getPhone() == null ? "" : incoming.getPhone().trim());
        saved.setAddress(incoming.getAddress() == null ? "" : incoming.getAddress().trim());
        saved.setIssueDurationDays(incoming.getIssueDurationDays());
        saved.setMaxBooksPerUser(incoming.getMaxBooksPerUser());
        saved.setLateFinePerDay(incoming.getLateFinePerDay());
        return repository.save(saved);
    }
}
