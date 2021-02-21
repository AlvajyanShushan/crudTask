package egs.task;

import egs.task.exceptions.EntityNotFoundException;
import egs.task.models.entities.Admin;
import egs.task.models.entities.Role;
import egs.task.repositories.AdminRepository;
import egs.task.repositories.RoleRepository;
import egs.task.utils.RoleConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRefreshed implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationRefreshed(RoleRepository roleRepository, AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        try {
//           seedData();
//    } catch (EntityNotFoundException e) {
//          e.printStackTrace();
//       }
    }

    private void seedData() throws EntityNotFoundException {

        Role adminRole = new Role();
        adminRole.setRoleName(RoleConstants.ADMIN_NAME);
        adminRole.setDescription("Admin - Has permission to perform admin tasks");
        adminRole.setHidden(false);
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName(RoleConstants.USER_NAME);
        userRole.setDescription("User - Has permission to perform user tasks");
        userRole.setHidden(false);
        roleRepository.save(userRole);

        Role guestRole = new Role();
        guestRole.setRoleName(RoleConstants.GUEST_NAME);
        guestRole.setDescription("Guest - Has permission to perform guest tasks");
        guestRole.setHidden(false);
        roleRepository.save(guestRole);

        Admin admin = new Admin();
        admin.setFirstName("Shushan");
        admin.setLastName("Alvajyan");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setEmail("alvajyanshushan@gmail.com");
        admin.setHidden(false);
        admin.setRole(roleRepository.findById(RoleConstants.ADMIN_ID).orElseThrow(() -> new EntityNotFoundException(Role.class, "Admin_Id")));
        adminRepository.save(admin);

    }
}