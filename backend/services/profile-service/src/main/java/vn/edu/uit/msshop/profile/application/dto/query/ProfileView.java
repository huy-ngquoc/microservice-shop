package vn.edu.uit.msshop.profile.application.dto.query;

public record ProfileView(
        String id,
        String fullName,
        String email,
        String phoneNumber,
        String address,
        String avatarUrl) {
}
