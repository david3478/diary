package hello.diary_server.service;

import hello.diary_server.dto.request.CreateUpdateDiaryRequestDto;
import hello.diary_server.dto.response.EntityResponseDto;
import hello.diary_server.entity.Diary;
import hello.diary_server.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    public EntityResponseDto save(CreateUpdateDiaryRequestDto dto) {
        Diary diary = new Diary();
        diary.setContent(dto.getContent());
        LocalDateTime localDate = changeLongToDate(dto.getCreatedDate());
        diary.setCreatedDate(localDate);
        diary.setEmotionId(dto.getEmotionId());

        diaryRepository.save(diary);

        return new EntityResponseDto(diary);
    }

    private LocalDateTime changeLongToDate(Long ms) {
        Instant instant = Instant.ofEpochMilli(ms);
        return LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));
    }

    public List<EntityResponseDto> findByMonth(YearMonth yearMonth) {
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay(); // 2025-07-01T00:00:00
        LocalDateTime end = yearMonth.atEndOfMonth().atTime(23, 59, 59); // 2025-07-31T23:59:59

        List<EntityResponseDto> result =  new ArrayList<>();
        for(Diary diary : diaryRepository.findByCreatedDateBetween(start, end)) {
            result.add(new EntityResponseDto(diary));
        }
        return result;
    }


    public EntityResponseDto findById(Long id) {
        Optional<Diary> findDiary = diaryRepository.findById(id);
        //id에 해당하는 diary 없을 시 new EntityResponseDto() return
        if(findDiary.isEmpty()) {
            return getEmptyEntityResponseDto();
        }

        return new EntityResponseDto(findDiary.get());
    }

    @Transactional
    public EntityResponseDto update(Long id, CreateUpdateDiaryRequestDto dto) {
        Optional<Diary> byId = diaryRepository.findById(id);
        //id에 해당하는 diary 없을 시 new Diary()
        if(byId.isEmpty()) {
            return getEmptyEntityResponseDto();
        }
        Diary diary = byId.get();
        diary.setCreatedDate(changeLongToDate(dto.getCreatedDate()));
        diary.setContent(dto.getContent());
        diary.setEmotionId(dto.getEmotionId());

        return new EntityResponseDto(diary);
    }

    public Map<String, String> deleteById(Long id) {
        Map<String, String> result = new HashMap<>();
        if(diaryRepository.existsById(id)) {
            diaryRepository.deleteById(id);
            result.put("message", "일기 삭제 완료");
        } else {
            result.put("message", "삭제 실패 : 해당하는 ID가 없습니다.");
        }

        return result;
    }

    private static EntityResponseDto getEmptyEntityResponseDto() {
        return new EntityResponseDto();
    }
}
