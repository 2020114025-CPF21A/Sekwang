package Sekwang.Domain;

import jakarta.persistence.*;
import lombok.*;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="members")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Member {
    @Id
    private String username;

    @Column(nullable=false)
    private String passwordHash;

    @Column(nullable=false)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Status status = Status.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role = Role.MEMBER;

    public enum Status { ACTIVE, INACTIVE }
    public enum Role { ADMIN, STAFF, LEADER, MEMBER }
}