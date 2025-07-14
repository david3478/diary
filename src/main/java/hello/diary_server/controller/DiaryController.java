package hello.diary_server.controller;

import hello.diary_server.dto.request.CreateUpdateDiaryRequestDto;
import hello.diary_server.dto.response.EntityResponseDto;
import hello.diary_server.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
@Tag(name = "감정 일기 API")
public class DiaryController {


    private final DiaryService diaryService;

    @PostMapping
    @Operation(summary = "일기 추가", description = "requestBody에 createdDate, emotionId, content 필요")
    public EntityResponseDto save(@RequestBody CreateUpdateDiaryRequestDto dto) {
        return diaryService.save(dto);
    }

    @GetMapping
    @Operation(summary = "월별로 일기 조회", description = "requestParam에 month=yyyy-mm 필요")
    public List<EntityResponseDto> findMonth(@RequestParam YearMonth month) {
        return diaryService.findByMonth(month);
    }

    @GetMapping("/{id}")
    @Operation(summary = "id로 조회", description = "id 포함 전체 일기 정보 응답")
    public EntityResponseDto findById(@PathVariable Long id) {
        return diaryService.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "일기 수정", description = "수정된 일기 정보 응답 -> id 입력 잘못될 시 빈 객체 응답")
    public EntityResponseDto update(@PathVariable Long id, @RequestBody CreateUpdateDiaryRequestDto dto) {
        return diaryService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "일기 삭제", description = "삭제 시 -> 삭제 완료 메시지 | id 입력 잘못될 시 -> 해당하는 아이디가 없습니다.")
    public Map<String, String> deleteById(@PathVariable Long id) {

        return diaryService.deleteById(id);
    }

}
