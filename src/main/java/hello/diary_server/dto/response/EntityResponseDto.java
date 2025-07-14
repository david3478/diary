package hello.diary_server.dto.response;

import hello.diary_server.entity.Diary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntityResponseDto {
    private Long id;
    private Long createdDate;
    private Long emotionId;
    private String content;
    public EntityResponseDto(Diary diary) {
        this.id = diary.getId();
        this.createdDate = convertToMs(diary.getCreatedDate());
        this.emotionId = diary.getEmotionId();
        this.content = diary.getContent();
    }

    private Long convertToMs(LocalDateTime paramDate) {
        // LocalDateTime → Instant (타임존 고려)
        Instant instant = paramDate.atZone(ZoneId.of("Asia/Seoul")).toInstant();
        // Instant → 밀리초
        return instant.toEpochMilli();
    }
}
