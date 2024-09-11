package hexlet.code.model;

import hexlet.code.repository.UrlCheckRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@ToString
public class Url {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public Url(String name) {
        this.name = name;
    }

    public List<UrlCheck> getChecks() throws SQLException {
        return UrlCheckRepository.findByUrl(this.id);
    }

    public Optional<UrlCheck> getLastCheck() throws SQLException {
        return this.getChecks().stream().max(Comparator.comparing(UrlCheck::getCreatedAt));
    }

}
