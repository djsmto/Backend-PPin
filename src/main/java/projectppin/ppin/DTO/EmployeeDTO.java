package projectppin.ppin.DTO;

import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@Getter
//@Setter
//@ToString(exclude = {"empPw", "resiNum"})
//public class EmployeeDTO extends User {
//
//    private Long enb;
//    private String empID;
//    private String empPw;
//    private String name;
//    private String resiNum;
//    private String phoneNum;
//    private String email;
//    private int loginErrCount;
//    private boolean del_flag;
//
//    private Long companyId;
//
//    private String customFourDigits;
//
//    private List<String> roleNames = new ArrayList<>();
//
//    // DTO 생성자
//    public EmployeeDTO(Long enb, String empID, String empPw, String name, String resiNum, String phoneNum, String email, int loginErrCount, boolean del_flag, Long companyId, List<String> roleNames) {
//        super(
//                empID,
//                empPw,
//                roleNames.stream().map(str -> new SimpleGrantedAuthority("ROLE_"+str)).collect(Collectors.toList()));
//
//        this.enb = enb;
//        this.empID = empID;
//        this.empPw = empPw;
//        this.name = name;
//        this.resiNum = resiNum;
//        this.phoneNum = phoneNum;
//        this.email = email;
//        this.loginErrCount = loginErrCount;
//        this.del_flag = del_flag;
//        this.companyId = companyId;
//        this.roleNames = roleNames;
//    }
//
//    public Map<String, Object> getClaims() {
//        Map<String, Object> dataMap = new HashMap<>();
//
//        dataMap.put("enb", enb);
//        dataMap.put("empID", enb);
//        dataMap.put("empPw", enb);
//        dataMap.put("name", enb);
//        dataMap.put("resiNum", enb);
//        dataMap.put("phoneNum", enb);
//        dataMap.put("email", enb);
//        dataMap.put("loginErrCount", enb);
//        dataMap.put("del_flag", enb);
//        dataMap.put("companyId", enb);
//        dataMap.put("roleNames", enb);
//
//        return dataMap;
//    }
//}

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

    private Long enb;
    private String empID;
    private String empPw;
    private String name;
    private String resiNum;
    private String phoneNum;
    private String email;
    private int loginErrCount;
    private boolean del_flag;

    private Long companyId;

    private String customFourDigits;

    public Map<String, Object> getClaims() {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("enb", enb);
        dataMap.put("empID", empID); // empID로 수정
        dataMap.put("empPw", empPw); // empPw로 수정
        dataMap.put("name", name);
        dataMap.put("resiNum", resiNum);
        dataMap.put("phoneNum", phoneNum);
        dataMap.put("email", email);
        dataMap.put("loginErrCount", loginErrCount);
        dataMap.put("del_flag", del_flag);
        dataMap.put("companyId", companyId);

        return dataMap;
    }
}