package vn.edu.uit.msshop.shared.adapter.out.cloudinary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

final class CloudinaryPublicIdsTest {

    @Test
    @DisplayName("TEMP_PREFIX_LENGTH matches the actual length of TEMP_PREFIX")
    void tempPrefixLength_MatchesActualPrefixLength() {
        Assertions.assertThat(CloudinaryPublicIds.TEMP_PREFIX)
                .as("TEMP_PREFIX_LENGTH must equal the actual length of TEMP_PREFIX")
                .hasSize(CloudinaryPublicIds.TEMP_PREFIX_LENGTH);
    }

    @Test
    @DisplayName("Strip the temp/ prefix when extracting a key from a temp public id")
    void extractKeyFromTemp_TempPublicId_ReturnsStrippedKey() {
        Assertions.assertThat(CloudinaryPublicIds.extractKeyFromTemp("temp/shoes-logo"))
                .as("extractKeyFromTemp should strip the leading 'temp/' prefix")
                .isEqualTo("shoes-logo");
    }

    @Test
    @DisplayName("Throw IllegalArgumentException when public id is outside the temp folder")
    void extractKeyFromTemp_NonTempPublicId_ThrowsException() {
        Assertions.assertThatThrownBy(() -> CloudinaryPublicIds.extractKeyFromTemp("brands/shoes-logo"))
                .as("extractKeyFromTemp should strip the leading 'temp/' prefix")
                .isInstanceOf(IllegalArgumentException.class);
    }
}
