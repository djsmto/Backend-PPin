package projectppin.ppin.security.filter;

import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import projectppin.ppin.DTO.EmployeeDTO;

import projectppin.ppin.util.JWTUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        // Preflight요청은 체크하지 않음
        if(request.getMethod().equals("OPTIONS")){
            return true;
        }

        String path = request.getRequestURI();

        log.info("check uri.............." + path);

        //api/member/ 경로의 호출은 체크하지 않음
        if(path.startsWith("/api/member/")) {
            return true;
        }

        //이미지 조회 경로는 체크하지 않는다면
        if(path.startsWith("/api/products/view/")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("------------------------JWTCheckFilter.......................");

        String authHeaderStr = request.getHeader("Authorization");

        try {
            //Bearer accestoken...
            String accessToken = authHeaderStr.substring(7);
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);

            log.info("JWT claims: " + claims);

            //filterChain.doFilter(request, response); //이하 추가

            Long enb = (Long) claims.get("enb");
            String empID = (String) claims.get("empID");
            String empPw = (String) claims.get("empPw");
            String name = (String) claims.get("name");
            String resiNum = (String) claims.get("resiNum");
            String phoneNum = (String) claims.get("phoneNum");
            String email = (String) claims.get("email");
            Integer loginErrCount = (Integer) claims.get("loginErrCount");
            Boolean del_flag = (Boolean) claims.get("del_flag");
            Long companyId = (Long) claims.get("companyId");
            List<String> roleNames = (List<String>) claims.get("roleNames");

            EmployeeDTO employeeDTO = new EmployeeDTO(enb, empID, empPw, name, resiNum, phoneNum, email, loginErrCount, del_flag.booleanValue(), companyId, roleNames);

            log.info("-----------------------------------");
            log.info(employeeDTO);
            log.info(employeeDTO.getAuthorities());

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(employeeDTO, empPw, employeeDTO.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        }catch(Exception e){

            log.error("JWT Check Error..............");
            log.error(e.getMessage());

            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();

        }
    }
}
