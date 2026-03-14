package com.collabsphere.repository;

import com.collabsphere.entity.CollaborationRequest;
import com.collabsphere.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CollaborationRequestRepository extends JpaRepository<CollaborationRequest, Long> {
    List<CollaborationRequest> findByPostAuthorId(Long authorId);
    List<CollaborationRequest> findByRequesterId(Long requesterId);
    List<CollaborationRequest> findByPostIdAndStatus(Long postId, RequestStatus status);
    boolean existsByRequesterIdAndPostId(Long requesterId, Long postId);
}
