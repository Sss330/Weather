package model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @Column(name = "id", nullable = false)
    UUID id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    User userId;
    @Column(name = "expires_at", nullable = false)
    LocalDateTime expiresAt;
}

