package projectppin.ppin.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectppin.ppin.DTO.DataLogDTO;

@Service
@Transactional
public class VacationService {
    @Autowired
    private DataLogService dataLogService;

//    public void logVacationUsage(String userId, int usedDays) {
//        // 남은 연차 계산 및 로그 기록
//        int remainingDays = 20 - usedDays;
//        String oldData = "남은 연차: " + 20;
//        String newData = "남은 연차: " + remainingDays;
//        dataLogService.logAction("admin",userId, "연차 사용", null, oldData, newData, "연차 사용: " + usedDays + "일");
//    }
//
//    public void carryOverAnnualLeave(String userId) {
//        // 이월 로직 및 로그 기록
//        int remainingDays = 20 - 12;
//        int carryOverDays = remainingDays > 5 ? 5 : Math.max(remainingDays, 0);
//        String oldData = "이월 전 남은 연차: " + remainingDays;
//        String newData = "이월된 연차: " + carryOverDays;
//        dataLogService.logAction("admin",userId, "연차 이월", null, oldData, newData, "이월된 연차: " + carryOverDays + "일");
//    }

    public void logVacationUsage(String userId, int usedDays) {
        // 남은 연차 계산
        int remainingDays = 20 - usedDays;

        // 이전 데이터 및 새로운 데이터 설정
        String oldData = "남은 연차: " + 20;
        String newData = "남은 연차: " + remainingDays;

        // DataLogDTO 생성 및 값 설정
        DataLogDTO dataLogDTO = new DataLogDTO();
        dataLogDTO.setActionType("연차 사용");
        dataLogDTO.setEntityType("연차");
        dataLogDTO.setOldData(oldData);
        dataLogDTO.setNewData(newData);
        dataLogDTO.setComments("연차 사용: " + usedDays + "일");

        // 로그 기록
        dataLogService.logAction(dataLogDTO, userId);
    }

    public void carryOverAnnualLeave(String userId) {
        // 연차 이월 로직
        int remainingDays = 20 - 12;
        int carryOverDays = remainingDays > 5 ? 5 : Math.max(remainingDays, 0);

        // 이전 데이터 및 새로운 데이터 설정
        String oldData = "이월 전 남은 연차: " + remainingDays;
        String newData = "이월된 연차: " + carryOverDays;

        // DataLogDTO 생성 및 값 설정
        DataLogDTO dataLogDTO = new DataLogDTO();
        dataLogDTO.setActionType("연차 이월");
        dataLogDTO.setEntityType("연차");
        dataLogDTO.setOldData(oldData);
        dataLogDTO.setNewData(newData);
        dataLogDTO.setComments("이월된 연차: " + carryOverDays + "일");

        // 로그 기록
        dataLogService.logAction(dataLogDTO, userId);
    }
}
