package sameuelesimeone.FitWell.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sameuelesimeone.FitWell.config.MailgunSender;
import sameuelesimeone.FitWell.dao.*;
import sameuelesimeone.FitWell.dto.CardWorkoutDTO;
import sameuelesimeone.FitWell.dto.GenerateCardDTO;
import sameuelesimeone.FitWell.dto.MailRequestCoachDTO;
import sameuelesimeone.FitWell.exceptions.BadRequestException;
import sameuelesimeone.FitWell.exceptions.NotFoundException;
import sameuelesimeone.FitWell.exceptions.UnauthorizedExeption;
import sameuelesimeone.FitWell.models.CardWorkout;
import sameuelesimeone.FitWell.models.NoteBook.StatW;
import sameuelesimeone.FitWell.models.Role;
import sameuelesimeone.FitWell.models.User;
import sameuelesimeone.FitWell.models.Workout;

import java.security.cert.CertificateRevokedException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CardWorkoutService {

    @Autowired
    CardWorkoutDAO cardWorkoutDAO;

    @Autowired
    WorkoutService workoutService;


    @Autowired
    WorkoutDAO workoutDAO;

    @Autowired
    SetDAO setDAO;

    @Autowired
    StatWDAO statWDAO;

    @Autowired
    UserService userService;

    @Autowired
    UserDAO userDAO;

    @Autowired
    MailgunSender mailgunSender;

    public List<CardWorkout> getAllCard(User current){
        if (current.getRole().size() == 1 || current.getRole().get(1).equals(Role.NUTRITIONIST)){
            return cardWorkoutDAO.findByUser(current);
        }else if (current.getRole().get(1).equals(Role.COACH)){
            System.out.println("ciao");
            return cardWorkoutDAO.findByCoach(current);
        }
            throw new BadRequestException("Invalid Role");
    }

    public List<CardWorkout> getAllCardCoach(User current){
        return cardWorkoutDAO.findByUser(current);
    }

    public CardWorkout save(CardWorkoutDTO cardWorkout, User currentUser){
        List<Workout> workoutList = cardWorkout.workouts_id().stream().map(el -> workoutService.findByID(UUID.fromString(el))).toList();
        if (currentUser.getRole().size() == 1){
            return createCard(cardWorkout,workoutList,currentUser,null);
        }else if (currentUser.getRole().get(1).equals(Role.COACH)){
            System.out.println("ciao di nuovo");
            User user = userService.findById(UUID.fromString(cardWorkout.user_id()));
            return  createCard(cardWorkout,workoutList,user,currentUser);
        }
        throw new UnauthorizedExeption("Invalid Role");
    }

    public CardWorkout saveNewCardCoach(CardWorkoutDTO cardWorkout, User currentUser){
        List<Workout> workoutList = cardWorkout.workouts_id().stream().map(el -> workoutService.findByID(UUID.fromString(el))).toList();
        return createCard(cardWorkout,workoutList,currentUser,null);
    }


    public CardWorkout createCard(CardWorkoutDTO cardWorkout, List<Workout> workoutList, User user ,User coach){
        CardWorkout newCard;

        if (coach != null){
             newCard = cardWorkoutDAO.save(new CardWorkout(cardWorkout.name(), workoutList, cardWorkout.restTimer(), user, coach));
        }else {
             newCard = cardWorkoutDAO.save(new CardWorkout(cardWorkout.name(), workoutList, cardWorkout.restTimer(), user, null));
        }

        workoutList.forEach(el -> {
            el.setCardWorkout(newCard);
            workoutDAO.save(el);
        });
        List<CardWorkout> cardWorkoutList = new ArrayList<>();
        cardWorkoutList.addAll(user.getWorkouts());
        cardWorkoutList.add(newCard);
        user.setWorkouts(cardWorkoutList);
        userDAO.save(user);

        return newCard;
    }

    public CardWorkout findById(UUID id){
        return cardWorkoutDAO.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    public CardWorkout modCardWorkout(UUID currentUserId, CardWorkoutDTO cardWorkout, UUID cardId){
        CardWorkout card = this.findById(cardId);
        if (card.getCoach() == null){
            if (card.getUser().getId().equals(currentUserId)){
                return cardWorkoutDAO.save(modCard(card, cardWorkout));
            }else {
                throw new UnauthorizedExeption("You can't modify this card workout");
            }
        }else {
            if (card.getCoach().getId().equals(currentUserId)){
                return cardWorkoutDAO.save(modCard(card, cardWorkout));
            }else {
                throw new UnauthorizedExeption("You can't modify this card workout");
            }
        }
    }

    public CardWorkout modCard(CardWorkout card, CardWorkoutDTO cardWorkout){
        List<Workout> workoutList = cardWorkout.workouts_id().stream().map(el -> workoutService.findByID(UUID.fromString(el))).toList();
        card.setName(cardWorkout.name());
        card.setRestTimer(cardWorkout.restTimer());
        card.setWorkouts(workoutList);
        workoutList.forEach(el -> {
            el.setCardWorkout(card);
            workoutDAO.save(el);
        });
        return card;
    }

    public void deleteCard(UUID cardId, User user) {
        CardWorkout card = this.findById(cardId);
        boolean isAdmin = user.getRole().contains(Role.ADMIN);
        boolean isCoach = user.getRole().contains(Role.COACH) && card.getCoach().getId().equals(user.getId());
        boolean isOwner = user.getRole().size() == 1 && card.getUser().getId().equals(user.getId());

        if (isOwner || isAdmin || isCoach) {
            card.getWorkouts().forEach(workouts -> {
                workouts.getSets().forEach(setDAO::delete);
                workoutDAO.delete(workouts);
            });
                List<StatW> stats = statWDAO.findByCardWorkout(card);
                if(!stats.isEmpty()){
                    stats.forEach(statWDAO::delete);
                }

            cardWorkoutDAO.delete(card);

        } else {
            throw new UnauthorizedExeption("You can't delete this workout");
        }
    }



    public CardWorkout generateCard(GenerateCardDTO generateCardDTO, UUID id){
        System.out.println(generateCardDTO.partMuscle());
        List<Workout> workoutList = workoutService.generateWorkout(generateCardDTO);
        System.out.println(workoutList);
        User acctualUser = userService.findById(id);
        CardWorkout newCard = cardWorkoutDAO.save(new CardWorkout(generateCardDTO.name(), workoutList, 60, acctualUser, null));
        workoutList.forEach(el -> {
            el.setCardWorkout(newCard);
            workoutDAO.save(el);
        });
        List<CardWorkout> cardWorkoutList = new ArrayList<>();
        cardWorkoutList.addAll(acctualUser.getWorkouts());
        cardWorkoutList.add(newCard);
        acctualUser.setWorkouts(cardWorkoutList);
        userDAO.save(acctualUser);
        return newCard;
    }

    public void requestOnCard(User user, MailRequestCoachDTO mail){
        User coach = userService.findById(UUID.fromString(mail.coachId()));
        CardWorkout card = null;
        if(mail.cardId() != null){
            card = this.findById(UUID.fromString(mail.cardId()));
        }
        switch (mail.function().toLowerCase()){
            case "create":
                mailgunSender.sendRequestCreateCard(user, coach);
                break;
            case "modify", "delete":
                mailgunSender.sendrequestOnCard(user, coach, card, mail.function());
                break;
            default:
                throw new BadRequestException("Invalid request");
        }
    }



}
