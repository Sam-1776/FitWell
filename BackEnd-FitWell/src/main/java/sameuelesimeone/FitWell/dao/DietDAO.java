package sameuelesimeone.FitWell.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sameuelesimeone.FitWell.models.Diet.Diet;
import sameuelesimeone.FitWell.models.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface DietDAO extends JpaRepository<Diet, UUID> {

    List<Diet> findByUser(User user);
    List<Diet> findByNutritionist(User nutritionist);

}
