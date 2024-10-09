package projectppin.ppin.security.handler;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import projectppin.ppin.domain.CompanyList;
import projectppin.ppin.domain.CompanyRole;
import projectppin.ppin.security.CustomUserDetails;
import projectppin.ppin.util.JWTUtil;
import projectppin.ppin.Repository.CompanyRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {

    private final CompanyRepository companyRepository; // 리포지토리 주입

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("-------------------------------------");
        log.info(authentication);
        log.info("-------------------------------------");

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // cnb 값 가져오기 (예: customUserDetails에서 가져오도록 설정)
        Long cnb = customUserDetails.getCompanyId();

        // 회사와 관련된 역할 목록 가져오기
        CompanyList company = companyRepository.getWithRoles(cnb);
        List<String> roleNames = company.getCompanyRoleList().stream()
                .map(CompanyRole::getRoleName) // 역할의 이름을 가져옵니다.
                .collect(Collectors.toList());

        Map<String, Object> claims = new HashMap<>();

        // JWT에 역할 포함
        claims.put("roleNames", roleNames);

        String accessToken = JWTUtil.generateToken(claims, 10);
        String refreshToken = JWTUtil.generateToken(claims, 60 * 24);

        claims.put("accessToken", accessToken);
        claims.put("refreshToken", refreshToken);

        Gson gson = new Gson();
        String jsonStr = gson.toJson(claims);

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonStr);
        printWriter.close();
    }
}