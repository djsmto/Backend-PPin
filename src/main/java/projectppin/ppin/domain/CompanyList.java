package projectppin.ppin.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CompanyList")
@ToString(exclude = "companyRoleList")
public class CompanyList {

    @Id
    private Long cnb;

    private String position;
    private String department;
    private Long baseSalary;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<CompanyRole> companyRoleList = new ArrayList<>();

    public void addRole(CompanyRole companyRole){
        companyRoleList.add(companyRole);
    }
}
