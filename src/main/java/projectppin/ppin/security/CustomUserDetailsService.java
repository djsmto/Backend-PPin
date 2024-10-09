package projectppin.ppin.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectppin.ppin.domain.CompanyList;
import projectppin.ppin.domain.CompanyRole;
import projectppin.ppin.domain.EmployeeList;
import projectppin.ppin.DTO.EmployeeDTO;
import projectppin.ppin.Repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * CustomUserDetailsService
 */
@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String empID) throws UsernameNotFoundException {
        // 1. EmployeeList에서 empID로 사용자 조회
        EmployeeList employee = employeeRepository.findByEmpID(empID)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found with empID: " + empID));
        log.info("employee : " + employee);

        // 2. 해당 직원이 속한 회사의 권한(roles) 리스트 조회
        CompanyList company = employee.getCompany();
        List<CompanyRole> companyRoles = company.getCompanyRoleList();

        // 3. 권한 리스트를 GrantedAuthority 객체 리스트로 변환
        List<GrantedAuthority> authorities = companyRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());

        // 4. CustomUserDetails 생성: 로그인 정보와 권한을 가진 UserDetails로 사용
        return new CustomUserDetails(
                employee.getEmpID(),      // 사용자 아이디 (empID)
                employee.getEmpPw(),      // 비밀번호 (Spring Security가 자동으로 비교)
                employee.getCompany().getCnb(), // 소속 회사 ID
                authorities                 // 권한 리스트
        );
    }
}