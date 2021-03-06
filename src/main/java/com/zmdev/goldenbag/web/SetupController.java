package com.zmdev.goldenbag.web;

import com.zmdev.goldenbag.domain.*;
import com.zmdev.goldenbag.service.*;
import com.zmdev.goldenbag.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/setup", produces = "application/json;charset=UTF-8")
public class SetupController extends BaseController {

    private PermissionService permissionService;

    private RoleService roleService;

    private UserService userService;

    private QuarterService quarterService;

    private DepartmentService departmentsService;

    private AssessmentTemplateService templateService;
    private AssessmentInputService assessmentInputService;
    private AssessmentProjectService assessmentProjectService;
    private AssessmentProjectItemRepository assessmentProjectItemRepository;
    private BasePermission[] basePermissions = {
            new BasePermission("view", "查看", true),
            new BasePermission("add", "添加", true),
            new BasePermission("edit", "编辑", true),
            new BasePermission("delete", "删除", true),
    };

    @Autowired
    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Autowired
    public void setQuarterService(QuarterService quarterService) {
        this.quarterService = quarterService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setDepartmentsService(DepartmentService departmentsService) {
        this.departmentsService = departmentsService;
    }

    @Autowired
    public void setTemplateService(AssessmentTemplateService templateService) {
        this.templateService = templateService;
    }

    @Autowired
    public void setAssessmentInputService(AssessmentInputService assessmentInputService) {
        this.assessmentInputService = assessmentInputService;
    }

    @Autowired
    public void setAssessmentProjectService(AssessmentProjectService assessmentProjectService) {
        this.assessmentProjectService = assessmentProjectService;
    }

    @Autowired
    public void setAssessmentProjectItemRepository(AssessmentProjectItemRepository assessmentProjectItemRepository) {
        this.assessmentProjectItemRepository = assessmentProjectItemRepository;
    }

    private void storePermission(String topModuleString, String moduleName, BasePermission[] basePermissions) {
        for (BasePermission basePermission : basePermissions) {
            Permission p = new Permission();
            p.setName(topModuleString + "." + moduleName + "." + basePermission.getActionName());
            p.setDisplayName(basePermission.getDisplayName());
            p.setDescription(basePermission.getDisplayName() + Permission.getModules().get(moduleName).substring(0, 2));
            p.setCreatedAt(new Date());
            p.setUpdatedAt(new Date());
            p.setMenuable(basePermission.getMenuable());
            permissionService.save(p);
        }
    }

    private void setupPermission() {
        Map<String, String[]> resources = new HashMap<>();
        resources.put("basic", new String[]{"department", "user", "role"});
        for (Map.Entry<String, String[]> entry : resources.entrySet()) {
            for (String moduleName : entry.getValue()) {
                storePermission(entry.getKey(), moduleName, basePermissions);
            }
        }

        Permission p = new Permission();
        p.setName("basic.role.permission");
        p.setDisplayName("查看指定角色的权限");
        p.setDescription("查看指定角色的权限");
        p.setCreatedAt(new Date());
        p.setUpdatedAt(new Date());
        p.setMenuable(true);
        permissionService.save(p);

        storePermission("basic", "quarter",
                new BasePermission[]{new BasePermission("view", "查看", true), new BasePermission("add", "添加", true), new BasePermission("edit", "编辑", true)});
        storePermission("basic", "permission", new BasePermission[]{new BasePermission("view", "查看", false)});

//        p = new Permission();
//        p.setName("basic.permission.allMenus");
//        p.setDisplayName("获取菜单");
//        p.setDescription("获取菜单");
//        p.setCreatedAt(new Date());
//        p.setUpdatedAt(new Date());
//        p.setMenuable(false);
//        permissionService.save(p);

        storePermission("template_module", "template", new BasePermission[]{
                new BasePermission("view", "查看", true),
                new BasePermission("add", "添加", true),
                new BasePermission("edit", "编辑", true),
        });

        p = new Permission();
        p.setName("template_module.template.export");
        p.setDisplayName("导出");
        p.setDescription("导出模板");
        p.setCreatedAt(new Date());
        p.setUpdatedAt(new Date());
        p.setMenuable(true);
        permissionService.save(p);

        p = new Permission();
        p.setName("assessment.directManagerScore.add");
        p.setDisplayName("直接经理评分");
        p.setDescription("直接经理评分");
        p.setCreatedAt(new Date());
        p.setUpdatedAt(new Date());
        p.setMenuable(true);
        permissionService.save(p);

        p = new Permission();
        p.setName("assessment.indirectManagerAuditComments.add");
        p.setDisplayName("间接经理建议");
        p.setDescription("间接经理建议");
        p.setCreatedAt(new Date());
        p.setUpdatedAt(new Date());
        p.setMenuable(true);
        permissionService.save(p);

        p = new Permission();
        p.setName("assessment.selfEvaluation.add");
        p.setDisplayName("员工自评");
        p.setDescription("员工自评");
        p.setCreatedAt(new Date());
        p.setUpdatedAt(new Date());
        p.setMenuable(true);
        permissionService.save(p);

        p = new Permission();
        p.setName("assessment.summary.show");
        p.setDisplayName("显示指定的考核记录");
        p.setDescription("显示指定的考核记录");
        p.setCreatedAt(new Date());
        p.setUpdatedAt(new Date());
        p.setMenuable(false);
        permissionService.save(p);

        p = new Permission();
        p.setName("assessment.summary.view");
        p.setDisplayName("查看");
        p.setDescription("查看考核记录");
        p.setCreatedAt(new Date());
        p.setUpdatedAt(new Date());
        p.setMenuable(true);
        permissionService.save(p);


        p = new Permission();
        p.setName("assessment.summary.batchExportByAssessmentIds");
        p.setDisplayName("批量导出");
        p.setDescription("批量导出考核记录");
        p.setCreatedAt(new Date());
        p.setUpdatedAt(new Date());
        p.setMenuable(true);
        permissionService.save(p);

        p = new Permission();
        p.setName("assessment.summary.batchExportByQuarterIds");
        p.setDisplayName("按季度批量导出");
        p.setDescription("按季度批量导出考核记录");
        p.setCreatedAt(new Date());
        p.setUpdatedAt(new Date());
        p.setMenuable(true);
        permissionService.save(p);
    }

    private void setupRole() {
        Role adminRole = new Role();
        adminRole.setName("管理员");
        adminRole.setDescription("管理员拥有所有权限");
        adminRole.setPermissions(permissionService.findAll());
        roleService.save(adminRole);

        Role directManagerRole = new Role();
        directManagerRole.setName("直接经理");
        directManagerRole.setDescription("直接经理可以给员工评分");
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permissionService.findByName("assessment.directManagerScore.add"));
        directManagerRole.setPermissions(permissions);
        roleService.save(directManagerRole);

        Role indirectManagerRole = new Role();
        indirectManagerRole.setName("间接经理");
        indirectManagerRole.setDescription("间接经理可以给员工建议");
        permissions = new ArrayList<>();
        permissions.add(permissionService.findByName("assessment.indirectManagerAuditComments.add"));
        indirectManagerRole.setPermissions(permissions);
        roleService.save(indirectManagerRole);

        Role employeeRole = new Role();
        employeeRole.setName("员工");
        employeeRole.setDescription("员工");
        permissions = new ArrayList<>();
        permissions.add(permissionService.findByName("assessment.selfEvaluation.add"));
        employeeRole.setPermissions(permissions);
        roleService.save(employeeRole);
    }

    private void setupUser() {
        User user = new User();
        user.setId(9L);
        user.setPhone("13956460801");
        user.setName("taoyu");
        user.setRankCoefficient(1.4);
        user.setType(AssessmentTemplate.Type.MANAGER_TEMPLATE);
        List<Role> roles = new ArrayList<>();
        roles.add(roleService.findByName("管理员"));
        user.setRoles(roles);
        userService.save(user);

        User sunlong = new User();
        sunlong.setId(7L);
        sunlong.setPhone("15705547511");
        sunlong.setName("孙龙");
        sunlong.setDirectManager(user);
        sunlong.setRankCoefficient(1.0);
        sunlong.setType(AssessmentTemplate.Type.MANAGER_TEMPLATE);
        sunlong.setEntryDate(TimeUtil.getCurrentQuarterStartTime());
        List<Role> sunlong_as_roles = new ArrayList<>();
        sunlong_as_roles.add(roleService.findByName("员工"));// 员工
        sunlong_as_roles.add(roleService.findByName("直接经理"));// 直接经理
        sunlong.setRoles(sunlong_as_roles);
        userService.save(sunlong);

        User lili = new User();
        lili.setId(11L);
        lili.setPhone("13956460800");
        lili.setName("莉莉");
        lili.setDirectManager(sunlong);
        lili.setType(AssessmentTemplate.Type.STAFF_TEMPLATE);
        lili.setIndirectManager(user);
        lili.setRankCoefficient(0.8);
        lili.setEntryDate(TimeUtil.getCurrentQuarterStartTime());
        List<Role> lili_as_roles = new ArrayList<>();
        lili_as_roles.add(roleService.findByName("员工"));// 员工
        lili.setRoles(lili_as_roles);
        userService.save(lili);
    }

    @GetMapping("setup")
    public String setup() {
        setupPermission();
        setupRole();
        setupUser();
        setUpQuarter();// 初始化季度
        setUpDepartments();// 初始化部门
        setUpTemplate();// 初始化模板
        setUpAssessmentInputs();// 初始化模板 Inputs
        setUpAssessmentProjects();// 初始化模板 Projects
        return "ok";
    }

    private void setUpQuarter() {
        Quarter quarter = new Quarter();
        quarter.setName("2018第1季度");
        quarter.setCurrentQuarter(true);
        quarter.setPrice(49.5);
        // 考核开始时间暂时固定
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            quarter.setStartDate(simpleDateFormat.parse("2018-06-01 00:00:01"));
            quarter.setStartAssessmentDate(simpleDateFormat.parse("2018-07-31 00:00:01"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        quarterService.save(quarter);
    }

    private void setUpDepartments() {
        Department pDepartment = new Department("开发部门", null);
        departmentsService.save(pDepartment);
        departmentsService.save(new Department("开发组1", pDepartment));
        departmentsService.save(new Department("开发组2", pDepartment));
        departmentsService.save(new Department("开发组3", pDepartment));
        pDepartment = new Department("分析部门", null);
        departmentsService.save(pDepartment);
        departmentsService.save(new Department("分析组1", pDepartment));
        departmentsService.save(new Department("分析组2", pDepartment));
        departmentsService.save(new Department("分析组3", pDepartment));
    }

    private void setUpTemplate() {
        Quarter currentQuarter = quarterService.findCurrentQuarter();

        AssessmentTemplate staff_template = new AssessmentTemplate();
        staff_template.setName("员工模板");
        staff_template.setQuarter(currentQuarter);
        staff_template.setType(AssessmentTemplate.Type.STAFF_TEMPLATE);
        templateService.save(staff_template);

        AssessmentTemplate manager_template = new AssessmentTemplate();
        manager_template.setName("经理模板");
        manager_template.setQuarter(currentQuarter);
        manager_template.setType(AssessmentTemplate.Type.MANAGER_TEMPLATE);
        templateService.save(manager_template);
    }

    private void setUpAssessmentInputs() {
        Quarter current = quarterService.findCurrentQuarter();
        List<AssessmentTemplate> assessmentTemplates = templateService.findByQuarter(current);
        for (AssessmentTemplate template : assessmentTemplates) {
            AssessmentInput assessmentInput_1 = new AssessmentInput();
            assessmentInput_1.setAssessmentTemplate(template);
            assessmentInput_1.setTitle("1．工作总结（包括主要工作内容，计划完成情况、主要成果，个人取得的进步等）：");
            assessmentInputService.save(assessmentInput_1);
            AssessmentInput assessmentInput_2 = new AssessmentInput();
            assessmentInput_2.setAssessmentTemplate(template);
            assessmentInput_2.setTitle("2．你觉得自身工作中还存在哪些问题及改进计划？对公司的工作有何意见和建议？");
            assessmentInputService.save(assessmentInput_2);
            AssessmentInput assessmentInput_3 = new AssessmentInput();
            assessmentInput_3.setAssessmentTemplate(template);
            assessmentInput_3.setTitle("3．你下一个阶段的工作目标和计划：");
            assessmentInputService.save(assessmentInput_3);
        }
    }

    private void setUpAssessmentProjects() {
        Quarter current = quarterService.findCurrentQuarter();
        List<AssessmentTemplate> assessmentTemplates = templateService.findByQuarter(current);
        for (AssessmentTemplate template : assessmentTemplates) {
            AssessmentProject assessmentProject_1 = new AssessmentProject();
            assessmentProject_1.setSort(0);
            assessmentProject_1.setAssessmentTemplate(template);
            assessmentProject_1.setItems(null);
            assessmentProject_1.setTitle(" 基础操守");
            assessmentProjectService.save(assessmentProject_1);
            AssessmentProjectItem assessmentProjectItem_11 = new AssessmentProjectItem(10, "A.非常自觉遵守公司及驻场的各项规章制度，以身作则，起模范带头作用，有很强的自我约束能力。", assessmentProject_1);
            assessmentProjectItemRepository.save(assessmentProjectItem_11);
            AssessmentProjectItem assessmentProjectItem_12 = new AssessmentProjectItem(7, "B.较自觉遵守公司及驻场的规章制度，有较强的自我约束能力。", assessmentProject_1);
            assessmentProjectItemRepository.save(assessmentProjectItem_12);
            AssessmentProjectItem assessmentProjectItem_13 = new AssessmentProjectItem(4, "C.一般能遵守公司及驻场的规章制度，有一定的自我约束能力。", assessmentProject_1);
            assessmentProjectItemRepository.save(assessmentProjectItem_13);

            AssessmentProject assessmentProject_2 = new AssessmentProject();
            assessmentProject_2.setSort(0);
            assessmentProject_2.setAssessmentTemplate(template);
            assessmentProject_2.setItems(null);
            assessmentProject_2.setTitle(" 工作态度");
            assessmentProjectService.save(assessmentProject_2);
            AssessmentProjectItem assessmentProjectItem_21 = new AssessmentProjectItem(10, "A.对公司及公司文化有很高的认同感，有团队合作精神，具有积极正面的工作态度，昂扬向上的精神风貌，工作任劳任怨，认真负责，一丝不苟，不惧困难，敢于担当，主动性强。", assessmentProject_2);
            assessmentProjectItemRepository.save(assessmentProjectItem_21);
            AssessmentProjectItem assessmentProjectItem_22 = new AssessmentProjectItem(7, "B.认可公司文化，工作较为积极主动，有较强的责任心。", assessmentProject_2);
            assessmentProjectItemRepository.save(assessmentProjectItem_22);
            AssessmentProjectItem assessmentProjectItem_23 = new AssessmentProjectItem(4, "C.没有公司文化概念，但工作态度较为端正，无较大负面情绪。", assessmentProject_2);
            assessmentProjectItemRepository.save(assessmentProjectItem_23);

            AssessmentProject assessmentProject_3 = new AssessmentProject();
            assessmentProject_3.setSort(0);
            assessmentProject_3.setAssessmentTemplate(template);
            assessmentProject_3.setItems(null);
            assessmentProject_3.setTitle("岗位技能");
            assessmentProjectService.save(assessmentProject_3);
            AssessmentProjectItem assessmentProjectItem_31 = new AssessmentProjectItem(10, " A. 非常自觉遵守公司及驻场的各项规章制度，以身作则，起模范带头作用，有很强的自我约束能力。", assessmentProject_3);
            assessmentProjectItemRepository.save(assessmentProjectItem_31);
            AssessmentProjectItem assessmentProjectItem_32 = new AssessmentProjectItem(7, "B. 较自觉遵守公司及驻场的规章制度，有较强的自我约束能力。", assessmentProject_3);
            assessmentProjectItemRepository.save(assessmentProjectItem_32);
            AssessmentProjectItem assessmentProjectItem_33 = new AssessmentProjectItem(4, "C. 一般能遵守公司及驻场的规章制度，有一定的自我约束能力。", assessmentProject_3);
            assessmentProjectItemRepository.save(assessmentProjectItem_33);

            AssessmentProject assessmentProject_4 = new AssessmentProject();
            assessmentProject_4.setSort(0);
            assessmentProject_4.setAssessmentTemplate(template);
            assessmentProject_4.setItems(null);
            assessmentProject_4.setTitle("学习进步");
            assessmentProjectService.save(assessmentProject_4);
            AssessmentProjectItem assessmentProjectItem_41 = new AssessmentProjectItem(10, "A.具有丰富的专业知识和高超的本岗位工作技能，并在工作中能够得以充分发挥，是本岗位的能手，完全达到岗位要求。", assessmentProject_4);
            assessmentProjectItemRepository.save(assessmentProjectItem_41);
            AssessmentProjectItem assessmentProjectItem_42 = new AssessmentProjectItem(7, "B.具有较好的专业知识和本岗位较高的技能，工作中能够得到展现。", assessmentProject_4);
            assessmentProjectItemRepository.save(assessmentProjectItem_42);
            AssessmentProjectItem assessmentProjectItem_43 = new AssessmentProjectItem(4, "C.专业知识一般，技能初步符合职责需要。", assessmentProject_4);
            assessmentProjectItemRepository.save(assessmentProjectItem_43);

            AssessmentProject assessmentProject_5 = new AssessmentProject();
            assessmentProject_5.setSort(0);
            assessmentProject_5.setAssessmentTemplate(template);
            assessmentProject_5.setItems(null);
            assessmentProject_5.setTitle("工作效率");
            assessmentProjectService.save(assessmentProject_5);
            AssessmentProjectItem assessmentProject_51 = new AssessmentProjectItem(10, "A.效率高，执行力强，且能提前、创新和超额完成任务。", assessmentProject_5);
            assessmentProjectItemRepository.save(assessmentProject_51);
            AssessmentProjectItem assessmentProject_52 = new AssessmentProjectItem(7, "B.执行力较强，能够按时较好地完成任务。", assessmentProject_5);
            assessmentProjectItemRepository.save(assessmentProject_52);
            AssessmentProjectItem assessmentProject_53 = new AssessmentProjectItem(4, "C.执行力一般，能够基本完成任务。", assessmentProject_5);
            assessmentProjectItemRepository.save(assessmentProject_53);

            AssessmentProject assessmentProject_6 = new AssessmentProject();
            assessmentProject_6.setSort(0);
            assessmentProject_6.setAssessmentTemplate(template);
            assessmentProject_6.setItems(null);
            assessmentProject_6.setTitle("工作质量");
            assessmentProjectService.save(assessmentProject_6);
            AssessmentProjectItem assessmentProject_61 = new AssessmentProjectItem(10, "A.工作质量很高，保持高水准，发生错误率极低。", assessmentProject_6);
            assessmentProjectItemRepository.save(assessmentProject_61);
            AssessmentProjectItem assessmentProject_62 = new AssessmentProjectItem(7, "B.工作质量较高，正确率较高，有错很快改正。", assessmentProject_6);
            assessmentProjectItemRepository.save(assessmentProject_62);
            AssessmentProjectItem assessmentProject_63 = new AssessmentProjectItem(4, "C.工作质量一般，基本上符合要求，偶尔出现错误。", assessmentProject_6);
            assessmentProjectItemRepository.save(assessmentProject_63);
        }
    }

    private class BasePermission {
        private String actionName;
        private String displayName;
        private Boolean menuable;

        public BasePermission(String actionName, String displayName, Boolean menuable) {
            this.actionName = actionName;
            this.displayName = displayName;
            this.menuable = menuable;
        }

        public String getActionName() {
            return actionName;
        }

        public void setActionName(String actionName) {
            this.actionName = actionName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public Boolean getMenuable() {
            return menuable;
        }

        public void setMenuable(Boolean menuable) {
            this.menuable = menuable;
        }
    }
}
