package sameuelesimeone.FitWell.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.config.MailgunSender;
import sameuelesimeone.FitWell.dao.UserDAO;
import sameuelesimeone.FitWell.dto.LoginDTO;
import sameuelesimeone.FitWell.dto.UserDTO;
import sameuelesimeone.FitWell.dto.UserLoginDTO;
import sameuelesimeone.FitWell.exceptions.BadRequestException;
import sameuelesimeone.FitWell.exceptions.UnauthorizedExeption;
import sameuelesimeone.FitWell.models.Gender;
import sameuelesimeone.FitWell.models.User;
import sameuelesimeone.FitWell.security.JWTTools;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private MailgunSender mailgunSender;


    public LoginDTO authUserAndGenerateToken(UserLoginDTO body) throws UnauthorizedExeption {
        User user = userService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), user.getPassword())){
            LoginDTO obJ = new LoginDTO(jwtTools.createToken(user), user.getRole().stream().map(el -> el.toString()).toList());
            return obJ;
        }else {
            throw new UnauthorizedExeption("Incorrect credentials");
        }
    }

    public User save(UserDTO user) {
        userDAO.findByEmail(user.email()).ifPresent(newUser ->{
            throw new BadRequestException("The email is already in use");
        });
        String avatar =  "https://ui-avatars.com/api/?name=" + user.name() + "+" + user.surname();
        switch (user.gender().toLowerCase()){
            case "man":
                User UserMan = new User(user.username(), user.email(), bcrypt.encode(user.password()), user.name(), user.surname(), Gender.MAN, avatar);
                User savedUserM = userDAO.save(UserMan);
                mailgunSender.sendRegistrationEmail(savedUserM);
                return savedUserM;
            case "woman":
                User UserWoman = new User(user.username(), user.email(), bcrypt.encode(user.password()), user.name(), user.surname(), Gender.WOMAN, avatar);
                User savedUserW= userDAO.save(UserWoman);
                mailgunSender.sendRegistrationEmail(savedUserW);
                return savedUserW;
            default:
                User UserWithoutGender = new User(user.username(), user.email(), bcrypt.encode(user.password()), user.name(), user.surname(), Gender.OTHER, avatar);
                User savedUserH = userDAO.save(UserWithoutGender);
                mailgunSender.sendRegistrationEmail(savedUserH);
                return savedUserH;
        }
    }
}
