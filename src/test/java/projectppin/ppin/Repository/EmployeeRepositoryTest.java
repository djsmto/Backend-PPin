package projectppin.ppin.Repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import projectppin.ppin.DTO.EmployeeDTO;
import projectppin.ppin.Service.EmployeeService;
import projectppin.ppin.domain.CompanyList;
import projectppin.ppin.domain.DataLog;
import projectppin.ppin.domain.EmployeeList;
import projectppin.ppin.util.EmployeeIDGenerator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 데이터베이스 사용
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DataLogRepository dataLogRepository;  // 로그 확인을 위한 레포지토리

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        // 회사가 이미 데이터베이스에 존재하는지 확인하여 사전 작업을 건너뜀
        Optional<CompanyList> companyOpt = companyRepository.findById(1L);
        assertTrue(companyOpt.isPresent(), "회사 정보가 데이터베이스에 있어야 합니다. (Cnb=1)");
    }

    @Test
    public void Test1() {

        EmployeeList employee = new EmployeeList();
        employee.setName("홍길동");
        employee.setPhoneNum("010-2323-2311");
        employee.setEmail("123123@example.com");

        Long enb = 1L;
        employee.setEnb(enb);

        String customFourDigits = "6789";

        String generatedEmpID = EmployeeIDGenerator.generateEmployeeID(employee.getEnb(), customFourDigits);
        employee.setEmpID(generatedEmpID);

        Long companyId = 1L;
        Optional<CompanyList> companyOpt = companyRepository.findById(companyId);
        assertTrue(companyOpt.isPresent(), "회사 정보가 올바르게 설정되어 있어야 합니다.");
        employee.setCompany(companyOpt.get());

        EmployeeList savedEmployee = employeeRepository.save(employee);

        assertNotNull(savedEmployee);
        assertEquals(generatedEmpID, savedEmployee.getEmpID());
        assertEquals("홍길동", savedEmployee.getName());
        assertEquals("010-2323-2311", savedEmployee.getPhoneNum());
        assertEquals(1L, savedEmployee.getCompany().getCnb());
    }

    @Test
    public void Test2() {

        EmployeeList employee = new EmployeeList();
        employee.setName("이동동");
        employee.setPhoneNum("010-3232-3232");
        employee.setEmail("3332221@example.com");

        Long enb = 2L;
        employee.setEnb(enb);

        String customFourDigits = "9999";

        String generatedEmpID = EmployeeIDGenerator.generateEmployeeID(employee.getEnb(), customFourDigits);
        employee.setEmpID(generatedEmpID);

        Long companyId = 2L;
        Optional<CompanyList> companyOpt = companyRepository.findById(companyId);
        assertTrue(companyOpt.isPresent(), "회사 정보가 올바르게 설정되어 있어야 합니다.");
        employee.setCompany(companyOpt.get());

        EmployeeList savedEmployee = employeeRepository.save(employee);

    }
    @Test
    public void Test3() {

        EmployeeList employee = new EmployeeList();
        employee.setName("박학년");
        employee.setPhoneNum("010-4342-2277");
        employee.setEmail("123333@naver.com");

        Long enb = 4L;
        employee.setEnb(enb);

        String customFourDigits = "4955";

        String generatedEmpID = EmployeeIDGenerator.generateEmployeeID(employee.getEnb(), customFourDigits);
        employee.setEmpID(generatedEmpID);

        Long companyId = 4L;
        Optional<CompanyList> companyOpt = companyRepository.findById(companyId);
        assertTrue(companyOpt.isPresent(), "회사 정보가 올바르게 설정되어 있어야 합니다.");
        employee.setCompany(companyOpt.get());

        EmployeeList savedEmployee = employeeRepository.save(employee);
    }

//    @Test
//    @DisplayName("사원 정보 저장 시 DataLog 기록 확인")
//    public void Test4() {
//        // Given: 새로운 사원 정보 설정
//        EmployeeDTO employeeDTO = new EmployeeDTO();
//        employeeDTO.setName("김비서");
//        employeeDTO.setPhoneNum("010-5353-2333");
//        employeeDTO.setEmail("qpqpqpqp@naver.com");
//        employeeDTO.setCompanyId(2L);  // 회사 ID 설정
//        employeeDTO.setCustomFourDigits("1004");  // 커스텀 숫자 설정
//
//        // When: 사원을 저장
//        EmployeeDTO savedEmployee = employeeService.addEmployee(employeeDTO);
//
//        // Then: DataLog에 로그가 기록되었는지 확인
//        List<DataLog> logs = dataLogRepository.findByActionType("사원 생성");
//
//        // 로그가 비어있지 않은지 확인 (로그가 기록되었는지 확인)
//        assertFalse(logs.isEmpty(), "사원 생성 로그가 기록되지 않았습니다.");
//
//        // 가장 최근의 로그를 확인
//        DataLog log = logs.get(logs.size() - 1);  // 가장 최근 기록
//
//        String expectedNewData = "사원 ID: " + savedEmployee.getEmpID() +
//                ", 이름: " + savedEmployee.getName() +
//                ", 전화번호: " + savedEmployee.getPhoneNum() +
//                ", 이메일: " + savedEmployee.getEmail();
//
//        // 데이터가 일치하는지 확인
//        assertEquals("사원 생성", log.getActionType(), "액션 타입이 사원 생성이어야 합니다.");
//        assertEquals(savedEmployee.getEmpID(), log.getNewData().substring(6), "로그에 저장된 사원 ID가 일치해야 합니다.");
//        assertEquals("사원 생성이 완료되었습니다.", log.getComments(), "로그 코멘트가 일치해야 합니다.");
//    }
@Test
@DisplayName("사원 정보 저장 시 DataLog 기록 확인")
public void Test4() {
    // Given: 새로운 사원 엔티티 설정
    EmployeeList employee = new EmployeeList();
    employee.setName("손정현");
    employee.setPhoneNum("010-0003-0003");
    employee.setEmail("annotation1234@naver.com");
//    employee.setCompanyId(2L);  // 회사 ID 설정
    employee.setEmpID("2410-000003"); // 사원 ID 설정
    employee.setEmpPw(passwordEncoder.encode("1111")); // 비밀번호 설정
    employee.setResiNum("123456-1234567");

    // 회사 ID 설정 = 위에 employee.setCompanyId(2L);을 쓰면 에러가 나기 때문
    // 회사 엔티티 설정
    Long companyId = 24L;
    Optional<CompanyList> companyOpt = companyRepository.findById(companyId);
    assertTrue(companyOpt.isPresent(), "회사 정보가 존재해야 합니다.");
    CompanyList company = companyOpt.get();

    // 사원의 소속 회사 설정
    employee.setCompany(company);

    // When: 사원을 저장 (엔티티 사용)
    EmployeeList savedEmployee = employeeRepository.save(employee);

//    // Then: DataLog에 로그가 기록되었는지 확인
//    List<DataLog> logs = dataLogRepository.findByActionType("사원 생성");
//
//    // 로그가 비어있지 않은지 확인 (로그가 기록되었는지 확인)
//    assertFalse(logs.isEmpty(), "사원 생성 로그가 기록되지 않았습니다.");
//
//    // 가장 최근의 로그를 확인
//    DataLog log = logs.get(logs.size() - 1);  // 가장 최근 기록
//
//    String expectedNewData = "사원 ID: " + savedEmployee.getEmpID() +
//            ", 이름: " + savedEmployee.getName() +
//            ", 전화번호: " + savedEmployee.getPhoneNum() +
//            ", 이메일: " + savedEmployee.getEmail();
//
//    // 데이터가 일치하는지 확인
//    assertEquals("사원 생성", log.getActionType(), "액션 타입이 사원 생성이어야 합니다.");
//    assertEquals(savedEmployee.getEmpID(), log.getNewData().substring(6), "로그에 저장된 사원 ID가 일치해야 합니다.");
//    assertEquals("사원 생성이 완료되었습니다.", log.getComments(), "로그 코멘트가 일치해야 합니다.");
}


//    @Test
//    @DisplayName("사원 정보 저장 시 DataLog 기록 확인 로그 일치여부")
//    public void Test5() {
//        // Given: 새로운 사원 정보 설정
//        EmployeeDTO employeeDTO = new EmployeeDTO();
//        employeeDTO.setName("이부장");
//        employeeDTO.setPhoneNum("010-5333-1234");
//        employeeDTO.setEmail("1233333@naver.com");
//        employeeDTO.setCompanyId(3L);  // 회사 ID 설정
//        employeeDTO.setCustomFourDigits("1003");  // 커스텀 숫자 설정
//
//        // When: 사원을 저장
//        EmployeeDTO savedEmployee = employeeService.addEmployee(employeeDTO);
//
//        // Then: DataLog에 로그가 기록되었는지 확인
//        List<DataLog> logs = dataLogRepository.findByActionType("사원 생성");
//
//        // 로그가 비어있지 않은지 확인 (로그가 기록되었는지 확인)
//        assertFalse(logs.isEmpty(), "사원 생성 로그가 기록되지 않았습니다.");
//
//        // 가장 최근의 로그를 확인
//        DataLog log = logs.get(logs.size() - 1);  // 가장 최근 기록
//
//        String expectedNewData = "사원 ID: " + savedEmployee.getEmpID() +
//                ", 이름: " + savedEmployee.getName() +
//                ", 전화번호: " + savedEmployee.getPhoneNum() +
//                ", 이메일: " + savedEmployee.getEmail();
//
//        // 데이터가 일치하는지 확인
//        assertEquals("사원 생성", log.getActionType(), "액션 타입이 사원 생성이어야 합니다.");
//        assertEquals(expectedNewData, log.getNewData(), "로그에 저장된 사원 데이터가 일치해야 합니다.");
//        assertEquals("사원 생성이 완료되었습니다.", log.getComments(), "로그 코멘트가 일치해야 합니다.");
//    }

    @Test
    @DisplayName("사원 정보 저장 시 DataLog 기록 확인 로그 일치여부")
    public void Test5() {
        // Given: 새로운 사원 엔티티 설정
        EmployeeList employee = new EmployeeList();
        employee.setEmpID("2410-000010");  // 사원 ID 설정
        employee.setEmpPw("1111"); // 비밀번호 설정
        employee.setName("이수학");
        employee.setPhoneNum("010-9993-1234");
        employee.setEmail("1233333@naver.com");
        employee.setResiNum("800101-1234567");  // 주민등록번호 설정

        // 회사 엔티티 설정
        Long companyId = 24L;
        Optional<CompanyList> companyOpt = companyRepository.findById(companyId);
        assertTrue(companyOpt.isPresent(), "회사 정보가 존재해야 합니다.");
        CompanyList company = companyOpt.get();

        // 사원의 소속 회사 설정
        employee.setCompany(company);

        // When: 사원을 저장 (엔티티 사용)
        EmployeeList savedEmployee = employeeRepository.save(employee);

//        // Then: DataLog에 로그가 기록되었는지 확인
//        List<DataLog> logs = dataLogRepository.findByActionType("사원 생성");
//
//        // 로그가 비어있지 않은지 확인 (로그가 기록되었는지 확인)
//        assertFalse(logs.isEmpty(), "사원 생성 로그가 기록되지 않았습니다.");
//
//        // 가장 최근의 로그를 확인
//        DataLog log = logs.get(logs.size() - 1);  // 가장 최근 기록
//
//        String expectedNewData = "사원 ID: " + savedEmployee.getEmpID() +
//                ", 이름: " + savedEmployee.getName() +
//                ", 전화번호: " + savedEmployee.getPhoneNum() +
//                ", 이메일: " + savedEmployee.getEmail();
//
//        // 데이터가 일치하는지 확인
//        assertEquals("사원 생성", log.getActionType(), "액션 타입이 사원 생성이어야 합니다.");
//        assertEquals(expectedNewData, log.getNewData(), "로그에 저장된 사원 데이터가 일치해야 합니다.");
//        assertEquals("사원 생성이 완료되었습니다.", log.getComments(), "로그 코멘트가 일치해야 합니다.");
    }

//    @Test
//    @DisplayName("주민번호 생성 및 비밀번호 자동 생성 테스트")
//    public void Test6() {
//        // Given: 새로운 사원 정보 설정
//        EmployeeDTO employeeDTO = new EmployeeDTO();
//        employeeDTO.setName("강부장");
//        employeeDTO.setResiNum("820101-1234567");
//        employeeDTO.setPhoneNum("010-1212-1232");
//        employeeDTO.setEmail("1233333@naver.com");
//        employeeDTO.setCompanyId(9L);  // 회사 ID 설정
//        employeeDTO.setCustomFourDigits("1009");  // 커스텀 숫자 설정
//
//        // When: 사원을 저장
//        EmployeeDTO savedEmployee = employeeService.addEmployee(employeeDTO);
//
//        // Then: DataLog에 로그가 기록되었는지 확인
//        List<DataLog> logs = dataLogRepository.findByActionType("사원 생성");
//
//        // 로그가 비어있지 않은지 확인 (로그가 기록되었는지 확인)
//        assertFalse(logs.isEmpty(), "사원 생성 로그가 기록되지 않았습니다.");
//
//        // 가장 최근의 로그를 확인
//        DataLog log = logs.get(logs.size() - 1);  // 가장 최근 기록
//
//        String expectedNewData = "사원 ID: " + savedEmployee.getEmpID() +
//                ", 이름: " + savedEmployee.getName() +
//                ", 전화번호: " + savedEmployee.getPhoneNum() +
//                ", 이메일: " + savedEmployee.getEmail();
//
//        // 데이터가 일치하는지 확인
//        assertEquals("사원 생성", log.getActionType(), "액션 타입이 사원 생성이어야 합니다.");
//        assertEquals(expectedNewData, log.getNewData(), "로그에 저장된 사원 데이터가 일치해야 합니다.");
//        assertEquals("사원 생성이 완료되었습니다.", log.getComments(), "로그 코멘트가 일치해야 합니다.");
//    }

//    @Test
//    @DisplayName("사원 수정 테스트")
//    public void Test7() {
//        // Given: 기존 사원 정보 불러오기 및 수정할 정보 설정
//        Long employeeId = 4L;  // 수정할 사원의 ID
//        EmployeeDTO existingEmployee = employeeService.findEmployeeByEnb(employeeId);
//        assertNotNull(existingEmployee, "사원이 존재해야 합니다.");
//
//        // 수정할 정보 설정 (예: 전화번호, 비밀번호)
//        existingEmployee.setPhoneNum("010-9876-1222");  // 전화번호 수정
//        existingEmployee.setEmpPw("admin1234");         // 비밀번호 수정
//
//        // When: 사원 정보를 수정
//        EmployeeDTO updatedEmployee = employeeService.updateEmployee(existingEmployee);  // 수정 메서드 호출
//
//        // Then: 수정된 정보가 올바르게 저장되었는지 확인
//        assertEquals("010-9876-1222", updatedEmployee.getPhoneNum(), "전화번호가 수정되어야 합니다.");
//        assertEquals("admin1234", updatedEmployee.getEmpPw(), "비밀번호가 수정되어야 합니다.");
//
//        // 추가: 로그에 수정 기록이 남았는지 확인
//        // 로그 조회 로직을 추가하여 로그가 올바르게 기록되었는지 확인
//        // 예시:
//        // List<DataLog> logs = dataLogRepository.findByActionType("사원 수정");
//        // assertFalse(logs.isEmpty(), "사원 수정 로그가 기록되지 않았습니다.");
//    }

//    @Test
//    @DisplayName("사원 추가 테스트 ")
//    public void Test8() {
//        // Given: 새로운 사원 정보 설정
//        EmployeeDTO newEmployee = new EmployeeDTO();
//        newEmployee.setName("이부장");
//        newEmployee.setPhoneNum("010-7987-1234");
//        newEmployee.setEmail("helloworld@gmail.com");
//        newEmployee.setResiNum("830711-1234567");  // 주민등록번호
//        newEmployee.setCustomFourDigits("1015");  // 커스텀 4자리
//        newEmployee.setCompanyId(15L);  // 회사 ID 설정
//
//        // When: 사원을 추가
//        EmployeeDTO savedEmployee = employeeService.addEmployee(newEmployee);
//
//        // Then: 추가된 사원 정보가 올바르게 저장되었는지 확인
//        assertNotNull(savedEmployee.getEnb(), "사원 고유번호(enb)가 null이면 안됩니다.");
//        assertEquals("이부장", savedEmployee.getName(), "저장된 사원의 이름이 일치하지 않습니다.");
//        assertEquals("010-7987-1234", savedEmployee.getPhoneNum(), "저장된 사원의 전화번호가 일치하지 않습니다.");
//        assertEquals("helloworld@gmail.com", savedEmployee.getEmail(), "저장된 사원의 이메일이 일치하지 않습니다.");
//
//        // 주민등록번호 앞 6자리로 비밀번호가 설정되었는지 확인
//        assertEquals("830711", savedEmployee.getEmpPw(), "비밀번호가 주민등록번호 앞 6자리로 설정되지 않았습니다.");
//
//        // 로그 기록 확인: "사원 생성" 로그가 기록되었는지 확인
//        List<DataLog> logs = dataLogRepository.findByActionType("사원 생성");
//        assertFalse(logs.isEmpty(), "사원 생성 로그가 기록되지 않았습니다.");
//
//        // 로그에서 가장 최근 기록 확인
//        DataLog log = logs.get(logs.size() - 1);
//        assertEquals("사원 생성", log.getActionType(), "로그 액션 타입이 사원 생성이어야 합니다.");
//        assertEquals("이부장", log.getNewData().split(", ")[1].split(": ")[1], "로그에 저장된 사원 이름이 일치하지 않습니다.");
//    }

//    @Test
//    @DisplayName("사원 더미 생성 ")
//    public void Test9() {
//        // Given: 새로운 사원 정보 설정
//        EmployeeDTO newEmployee = new EmployeeDTO();
//        newEmployee.setName("Ethan Martinez");
//        newEmployee.setPhoneNum("010-1111-2222");
//        newEmployee.setEmail("Plancon2@gmail.com");
//        newEmployee.setResiNum("980511-6234567");  // 주민등록번호
//        newEmployee.setCustomFourDigits("1108");  // 커스텀 4자리
//        newEmployee.setCompanyId(8L);  // 회사 ID 설정
//
//        // When: 사원을 추가
//        EmployeeDTO savedEmployee = employeeService.addEmployee(newEmployee);
//    }

    @Test
    @WithMockUser(username = "testUser")
    public void Test6() {
        // 사원 추가용 DTO 설정
        EmployeeDTO newEmployeeDTO = new EmployeeDTO();
        newEmployeeDTO.setName("John Doe");
        newEmployeeDTO.setCustomFourDigits("1234");  // 고유번호 4자리 설정
        newEmployeeDTO.setResiNum("123456-1234567");  // 주민등록번호 설정
        newEmployeeDTO.setPhoneNum("010-1234-5678");
        newEmployeeDTO.setEmail("john.doe@example.com");
        newEmployeeDTO.setCompanyId(1L);  // 회사 ID 1번 사용

        // 사원 생성
        EmployeeDTO createdEmployee = employeeService.addEmployee(newEmployeeDTO);

        // 테스트 결과 검증
        assertNotNull(createdEmployee);
        assertNotNull(createdEmployee.getEmpID());  // 사원 ID 자동 생성 확인
        assertNotNull(createdEmployee.getEmpPw());  // 비밀번호 자동 생성 확인
        assertNotNull(createdEmployee.getCompanyId());  // 회사 ID 설정 확인
    }

}