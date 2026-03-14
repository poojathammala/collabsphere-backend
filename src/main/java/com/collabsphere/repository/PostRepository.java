package com.collabsphere.repository;

import com.collabsphere.entity.Post;
import com.collabsphere.entity.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    List<Post> findByTypeOrderByCreatedAtDesc(PostType type);

    @Query("SELECT p FROM Post p WHERE " +
           "LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.skillsRequired) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.skillsOffered) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.domain) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Post> searchPosts(@Param("query") String query);

    @Query("SELECT p FROM Post p WHERE " +
           "LOWER(p.skillsRequired) LIKE LOWER(CONCAT('%', :skill, '%')) OR " +
           "LOWER(p.skillsOffered) LIKE LOWER(CONCAT('%', :skill, '%'))")
    List<Post> findBySkill(@Param("skill") String skill);

    Page<Post> findByDomainOrderByCreatedAtDesc(String domain, Pageable pageable);

    Page<Post> findByTypeAndOpenForCollaborationOrderByCreatedAtDesc(PostType type, boolean open, Pageable pageable);
}
