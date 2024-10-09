package projectppin.ppin.Service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectppin.ppin.DTO.DataLogDTO;
import projectppin.ppin.DTO.EmployeeDTO;
import projectppin.ppin.Repository.CompanyRepository;
import projectppin.ppin.Repository.EmployeeRepository;
import projectppin.ppin.domain.CompanyList;
import projectppin.ppin.domain.EmployeeList;
import projectppin.ppin.util.EmployeeIDGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DataLogService dataLogService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 사원 추가
    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO) {
        boolean uniqueCodeExists = employeeRepository.existsByEmpID(employeeDTO.getEmpID());

        if (uniqueCodeExists) {
            throw new IllegalArgumentException("고유번호가 이미 존재합니다.");
        }

        EmployeeList newEmployee = new EmployeeList();
        EmployeeList savedEmployee = employeeRepository.save(newEmployee);

        String generatedEmpID = EmployeeIDGenerator.generateEmployeeID(savedEmployee.getEnb(), employeeDTO.getCustomFourDigits());
        savedEmployee.setEmpID(generatedEmpID);

        setPasswordForEmployee(savedEmployee, employeeDTO);
        updateEmployeeDetails(savedEmployee, employeeDTO);

        EmployeeList updatedEmployee = employeeRepository.save(savedEmployee);

        // 로그 기록
        logAction("사원 생성", updatedEmployee);

        return convertToDTO(updatedEmployee);
    }

    // 사원 수정
    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO) {
        EmployeeList existingEmployee = employeeRepository.findById(employeeDTO.getEnb())
                .orElseThrow(() -> new IllegalArgumentException("해당 사원을 찾을 수 없습니다."));

        if (employeeDTO.getEmpPw() != null && !employeeDTO.getEmpPw().isEmpty()) {
            existingEmployee.setEmpPw(employeeDTO.getEmpPw());
        }

        updateEmployeeDetails(existingEmployee, employeeDTO);
        EmployeeList updatedEmployee = employeeRepository.save(existingEmployee);

        // 로그 기록
        logAction("사원 수정", updatedEmployee);

        return convertToDTO(updatedEmployee);
    }

    // 사원 삭제
    public boolean deleteEmployee(Long enb) {
        Optional<EmployeeList> employeeOpt = employeeRepository.findById(enb);
        if (employeeOpt.isPresent()) {
            EmployeeList employee = employeeOpt.get();

            // 로그 기록
            logAction("사원 삭제", employee);

            employeeRepository.delete(employee);
            return true;
        } else {
            throw new EntityNotFoundException("사원을 찾을 수 없습니다.");
        }
    }

    // 공통: 사원 정보 업데이트
    private void updateEmployeeDetails(EmployeeList employee, EmployeeDTO employeeDTO) {
        employee.setName(employeeDTO.getName());
        employee.setResiNum(employeeDTO.getResiNum());
        employee.setPhoneNum(employeeDTO.getPhoneNum());
        employee.setEmail(employeeDTO.getEmail());
        employee.setLoginErrCount(employeeDTO.getLoginErrCount());

        if (employeeDTO.getCompanyId() != null) {
            CompanyList company = companyRepository.findById(employeeDTO.getCompanyId()).orElse(null);
            employee.setCompany(company);
        }
    }

    // 공통: 비밀번호 설정 로직
    private void setPasswordForEmployee(EmployeeList employee, EmployeeDTO employeeDTO) {
        if (employeeDTO.getEmpPw() == null || employeeDTO.getEmpPw().isEmpty()) {
            String resiNum = employeeDTO.getResiNum();
            if (resiNum != null && resiNum.length() >= 6) {
                String generatedPassword = resiNum.substring(0, 6);
                employee.setEmpPw(passwordEncoder.encode(generatedPassword));
            } else {
                throw new IllegalArgumentException("주민등록번호 형식이 잘못되었습니다.");
            }
        } else {
            employee.setEmpPw(passwordEncoder.encode(employeeDTO.getEmpPw()));
        }
    }

    // 공통: 로그 기록 (로그인한 사용자 이름으로 기록)
    private void logAction(String action, EmployeeList employee) {
        // 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName(); // 로그인한 사용자의 이름 또는 ID

        String newData = "사원 ID: " + employee.getEmpID() + ", 이름: " + employee.getName() +
                ", 전화번호: " + employee.getPhoneNum() + ", 이메일: " + employee.getEmail();

        DataLogDTO dataLogDTO = new DataLogDTO();
        dataLogDTO.setActionType(action);
        dataLogDTO.setEntityType("사원");
        dataLogDTO.setEntityId(employee.getEnb().intValue());
        dataLogDTO.setNewData(newData);
        dataLogDTO.setComments(action + "이 완료되었습니다.");

        // 'admin' 대신 로그인한 사용자 정보 기록
        dataLogService.logAction(dataLogDTO, loggedInUser);
    }

    // 엔티티를 DTO로 변환
    public EmployeeDTO convertToDTO(EmployeeList employee) {
        return new EmployeeDTO(
                employee.getEnb(),
                employee.getEmpID(),
                employee.getEmpPw(),
                employee.getName(),
                employee.getResiNum(),
                employee.getPhoneNum(),
                employee.getEmail(),
                employee.getLoginErrCount(),
                employee.isDel_flag(),
                (employee.getCompany() != null) ? employee.getCompany().getCnb() : null,
                null
        );
    }

    // 특정 사원 조회
    public EmployeeDTO findEmployeeByEnb(Long enb) {
        EmployeeList employee = employeeRepository.findById(enb).orElse(null);
        return (employee != null) ? convertToDTO(employee) : null;
    }

    // 모든 사원 조회
    public List<EmployeeDTO> findAllEmployees() {
        List<EmployeeList> employees = employeeRepository.findAll();
        return employees.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 모든 회사 조회
    public List<CompanyList> findAllCompanies() {
        return companyRepository.findAll();
    }
}