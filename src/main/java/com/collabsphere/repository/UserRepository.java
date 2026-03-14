package com.collabsphere.repository;

import com.collabsphere.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.skills) LIKE LOWER(CONCAT('%', :skill, '%')) OR " +
           "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :skill, '%')) OR " +
           "LOWER(u.department) LIKE LOWER(CONCAT('%', :skill, '%'))")
    List<User> searchBySkill(@Param("skill") String skill);

    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(u.skills) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(u.department) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<User> searchUsers(@Param("query") String query);

    List<User> findByDepartment(String department);

    List<User> findByCollege(String college);
}
