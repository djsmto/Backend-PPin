package projectppin.ppin.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import projectppin.ppin.DTO.EmployeeDTO;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    @WithMockUser(username = "testUser")  // Mocked 사용자 인증 정보
    public void testAddEmployee() {
        // Given: 새로운 사원 정보 설정
        EmployeeDTO newEmployeeDTO = new EmployeeDTO();
        newEmployeeDTO.setName("김비서");
        newEmployeeDTO.setCustomFourDigits("1201");  // 고유번호 4자리 설정
        newEmployeeDTO.setResiNum("970808-1234567");  // 주민등록번호 설정
        newEmployeeDTO.setPhoneNum("010-4545-5348");
        newEmployeeDTO.setEmail("123233@gmail.com");
        newEmployeeDTO.setCompanyId(201L);  // 회사 ID 설정

        // When: 사원을 추가
        EmployeeDTO createdEmployee = employeeService.addEmployee(newEmployeeDTO);

    }


    @Test
    @WithMockUser(username = "testUser")  // Mocked 사용자 인증 정보
    public void testAddEmplo2yee() {
        // Given: 새로운 사원 정보 설정 (setter 메서드 사용)
        EmployeeDTO newEmployeeDTO = new EmployeeDTO();  // 기본 생성자 사용
        newEmployeeDTO.setEmpID("emp001");
        newEmployeeDTO.setEmpPw("password123");  // 암호화되기 전의 비밀번호
        newEmployeeDTO.setName("John Doe");
        newEmployeeDTO.setResiNum("123456-1234567");  // 주민등록번호
        newEmployeeDTO.setPhoneNum("010-1234-5678");
        newEmployeeDTO.setEmail("john.doe@example.com");
        newEmployeeDTO.setCompanyId(1L);  // 회사 ID
        newEmployeeDTO.setLoginErrCount(0);
        newEmployeeDTO.setDel_flag(false);
        newEmployeeDTO.setCustomFourDigits("1234");  // 사용자 지정 4자리 번호

        // When: 사원을 추가
        EmployeeDTO createdEmployee = employeeService.addEmployee(newEmployeeDTO);

        // Then: 테스트 결과 검증
        assertNotNull(createdEmployee);  // 사원이 생성되었는지 확인
        assertNotNull(createdEmployee.getEmpID());  // 사원 ID가 생성되었는지 확인
        assertEquals("John Doe", createdEmployee.getName());  // 사원 이름이 올바르게 설정되었는지 확인
        assertEquals(1L, createdEmployee.getCompanyId());  // 회사 ID가 올바르게 설정되었는지 확인
    }
}