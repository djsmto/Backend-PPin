package projectppin.ppin.Service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectppin.ppin.DTO.DataLogDTO;
import projectppin.ppin.domain.CompanyList;
import projectppin.ppin.Repository.CompanyRepository;

import java.util.Optional;

@Service
@Transactional
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DataLogService dataLogService;

    // 회사 정보 생성
    public CompanyList createCompany(String position, String department, Long baseSalary) {
        CompanyList company = new CompanyList();
        company.setPosition(position);
        company.setDepartment(department);
        company.setBaseSalary(baseSalary);

        CompanyList createdCompany = companyRepository.save(company);

        // 회사 생성 시 Data Log 기록
        String newData = "회사 생성: 포지션: " + position + ", 부서: " + department + ", 기준 봉급: " + baseSalary;

        // DataLogDTO 객체 생성
        DataLogDTO dataLogDTO = new DataLogDTO();
        dataLogDTO.setActionType("회사 생성");
        dataLogDTO.setEntityType("회사");
        dataLogDTO.setEntityId(createdCompany.getCnb().intValue());
        dataLogDTO.setNewData(newData);
        dataLogDTO.setComments("회사 생성");

        // 로그 기록
        dataLogService.logAction(dataLogDTO, "admin");

        return createdCompany;
    }

    // 회사 정보 수정 (기준 봉급 변경 시 Data Log 기록 추가)
    public CompanyList updateCompany(Long cnb, String position, String department, Long baseSalary) {
        Optional<CompanyList> companyOpt = companyRepository.findById(cnb);
        if (companyOpt.isPresent()) {
            CompanyList company = companyOpt.get();

            // 이전 정보 저장
            String oldData = "기존 정보 - 직책: " + company.getPosition() + ", 부서: " + company.getDepartment() + ", 기준 봉급: " + company.getBaseSalary();

            // 회사 정보 업데이트
            company.setPosition(position);
            company.setDepartment(department);
            company.setBaseSalary(baseSalary);

            CompanyList updatedCompany = companyRepository.save(company);  // 수정된 정보 저장

            // 수정 후 Data Log 기록
            String newData = "새로운 정보 - 직책: " + position + ", 부서: " + department + ", 기준 봉급: " + baseSalary;

            // DataLogDTO 객체 생성
            DataLogDTO dataLogDTO = new DataLogDTO();
            dataLogDTO.setActionType("회사 수정");
            dataLogDTO.setEntityType("회사");
            dataLogDTO.setEntityId(updatedCompany.getCnb().intValue());
            dataLogDTO.setOldData(oldData);
            dataLogDTO.setNewData(newData);
            dataLogDTO.setComments("회사 정보 수정");

            // 로그 기록
            dataLogService.logAction(dataLogDTO, "admin");

            return updatedCompany;
        } else {
            throw new EntityNotFoundException("정보를 찾을 수 없습니다.");
        }
    }

    // 회사 정보 삭제
    public void deleteCompany(Long cnb) {
        Optional<CompanyList> companyOpt = companyRepository.findById(cnb);
        if (companyOpt.isPresent()) {
            CompanyList company = companyOpt.get();

            // 삭제 전 Data Log 기록
            String oldData = "삭제된 회사 정보 - 포지션: " + company.getPosition() + ", 부서: " + company.getDepartment() + ", 기준 봉급: " + company.getBaseSalary();

            // DataLogDTO 객체 생성
            DataLogDTO dataLogDTO = new DataLogDTO();
            dataLogDTO.setActionType("회사 삭제");
            dataLogDTO.setEntityType("회사");
            dataLogDTO.setEntityId(cnb.intValue());
            dataLogDTO.setOldData(oldData);
            dataLogDTO.setComments("회사 정보 삭제");

            // 로그 기록
            dataLogService.logAction(dataLogDTO, "admin");

            // 회사 정보 삭제
            companyRepository.delete(company);
        } else {
            throw new EntityNotFoundException("삭제할 정보를 찾을 수 없습니다.");
        }
    }
}
