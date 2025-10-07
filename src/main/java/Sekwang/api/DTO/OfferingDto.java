// src/main/java/Sekwang/api/DTO/OfferingDto.java
package Sekwang.api.DTO;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OfferingDto {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class CreateReq {
        @NotBlank
        private String username;          // Member PK (username)

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false, message = "금액은 0보다 커야 합니다.")
        private BigDecimal amount;        // 원화라도 DB는 BigDecimal 사용

        private String note;              // 메모(선택)
        // 프론트에서 보낸 시간이 있으면 사용하고, 없으면 서버 now() 사용하고 싶으면 필드 추가 가능
        // private LocalDateTime offeredAt;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class Res {
        private Long id;
        private String username;
        private BigDecimal amount;
        private String note;
        private LocalDateTime offeredAt;
    }

    /** 요약(이번 달 / 지난 달 / 총액) */
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class Summary {
        private BigDecimal thisMonth;   // 이번 달 합계
        private BigDecimal lastMonth;   // 지난 달 합계
        private BigDecimal total;       // 전체 합계
    }
}
