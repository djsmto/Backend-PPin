package projectppin.ppin.Service;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectppin.ppin.DTO.DataLogDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
public class AttendanceService {

    @Autowired
    private DataLogService dataLogService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 출근 기록
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void clockIn(String userId) {
        // 클릭 시점에서 시간을 미리 설정
        LocalDateTime clockInTime = LocalDateTime.now();
        // DataLogDTO 객체 생성 및 데이터 설정
        DataLogDTO dataLogDTO = new DataLogDTO();
        dataLogDTO.setActionType("출근");
        dataLogDTO.setEntityType("출근 기록");
        dataLogDTO.setEntityId(0); // 출근 기록이므로 엔티티 ID는 0으로 처리
        dataLogDTO.setNewData("출근 기록: " + clockInTime.now().format(formatter));  // 포맷 적용
        dataLogDTO.setComments("출근 기록이 저장되었습니다.");

        // 로그 기록 호출
        dataLogService.logAction(dataLogDTO, userId);
    }

    // 퇴근 기록
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void clockOut(String userId) {
        // 클릭 시점에서 시간을 미리 설정
        LocalDateTime clockOutTime = LocalDateTime.now();
        // DataLogDTO 객체 생성 및 데이터 설정
        DataLogDTO dataLogDTO = new DataLogDTO();
        dataLogDTO.setActionType("퇴근");
        dataLogDTO.setEntityType("퇴근 기록");
        dataLogDTO.setEntityId(0); // 퇴근 기록이므로 엔티티 ID는 0으로 처리
        dataLogDTO.setNewData("퇴근 기록: " + clockOutTime.now().format(formatter));  // 포맷 적용
        dataLogDTO.setComments("퇴근 기록이 저장되었습니다.");

        // 로그 기록 호출
        dataLogService.logAction(dataLogDTO, userId);
    }
}
