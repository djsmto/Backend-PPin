package projectppin.ppin.domain;

public enum CompanyRole {
    STAFF("Staff"),
    HR_PLAN("HR Plan"),
    HR_MANAGEMENT("HR Management"),
    HR_PLAN_MANAGER("HR Plan Manager"),
    HR_MANAGEMENT_MANAGER("HR Management Manager"),
    CEO("CEO");

    private final String roleName; // 역할 이름 필드 추가

    // 생성자
    CompanyRole(String roleName) {
        this.roleName = roleName;
    }

    // 역할 이름 반환 메서드
    public String getRoleName() {
        return roleName;
    }
}