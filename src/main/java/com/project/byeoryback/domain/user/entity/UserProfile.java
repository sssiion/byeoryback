package com.project.byeoryback.domain.user.entity;

import com.project.byeoryback.domain.user.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "full_profile")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String profilePhoto;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    private LocalDate birthDate;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = 50)
    private String bio;

    public void update(String profilePhoto, String name, String nickname, LocalDate birthDate, String phone,
            Gender gender, String bio) {
        this.profilePhoto = profilePhoto;
        this.name = name;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.phone = phone;
        this.gender = gender;
        this.bio = bio;
    }
}
