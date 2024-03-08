package sameuelesimeone.FitWell.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sameuelesimeone.FitWell.config.MailgunSender;
import sameuelesimeone.FitWell.dao.UserDAO;
import sameuelesimeone.FitWell.dto.RoleDTO;
import sameuelesimeone.FitWell.exceptions.NotFoundException;
import sameuelesimeone.FitWell.models.Role;
import sameuelesimeone.FitWell.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    MailgunSender mailgunSender;


    public Page<User> getUsers(int pageN, int pageS, String OrderBy) {
        if (pageS > 20) pageS = 20;
        Pageable pageable = PageRequest.of(pageN, pageS, Sort.by(OrderBy));
        return userDAO.findAll(pageable);
    }

    public User findById(UUID id) {
        return userDAO.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    public User update(UUID id, User userUp) {
        User found = this.findById(id);
        found.setName(userUp.getName());
        found.setSurname(userUp.getSurname());
        found.setEmail(userUp.getEmail());
        found.setPassword(userUp.getPassword());
        return userDAO.save(found);
    }

    public void deleteUser(UUID id) {
        User found = this.findById(id);
        userDAO.delete(found);
        mailgunSender.sendDeleteAccount(found);
    }

    public User findByEmail(String email){
        return userDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("Email "+ email + " non trovata"));
    }

    public User updateRole(UUID id, RoleDTO role) throws Exception {
        User found = this.findById(id);
        List<Role> roles = new ArrayList<>();
        roles.addAll(found.getRole());
        switch (role.role().toLowerCase()){
            case "admin":
                roles.add(Role.ADMIN);
                found.setRole(roles);
                this.userDAO.save(found);
                break;
            case "coach":
                roles.add(Role.COACH);
                found.setRole(roles);
                this.userDAO.save(found);
                break;
            case "nutritionist":
                roles.add(Role.NUTRITIONIST);
                found.setRole(roles);
                this.userDAO.save(found);
                break;
            default:
                throw new Exception("Invalid role");
        }
        return found;
    }
}
