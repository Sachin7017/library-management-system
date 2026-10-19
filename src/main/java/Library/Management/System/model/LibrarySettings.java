package Library.Management.System.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "library_settings")
public class LibrarySettings {
    @Id
    private Long id = 1L;
    private String libraryName = "My Library";
    private String email = "";
    private String phone = "";
    private String address = "";
    private int issueDurationDays = 14;
    private int maxBooksPerUser = 3;
    private double lateFinePerDay = 5.0;

    public LibrarySettings() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLibraryName() { return libraryName; }
    public void setLibraryName(String libraryName) { this.libraryName = libraryName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public int getIssueDurationDays() { return issueDurationDays; }
    public void setIssueDurationDays(int issueDurationDays) { this.issueDurationDays = issueDurationDays; }
    public int getMaxBooksPerUser() { return maxBooksPerUser; }
    public void setMaxBooksPerUser(int maxBooksPerUser) { this.maxBooksPerUser = maxBooksPerUser; }
    public double getLateFinePerDay() { return lateFinePerDay; }
    public void setLateFinePerDay(double lateFinePerDay) { this.lateFinePerDay = lateFinePerDay; }
}
