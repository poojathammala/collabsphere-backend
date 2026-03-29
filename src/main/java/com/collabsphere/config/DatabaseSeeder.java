package com.collabsphere.config;

import com.collabsphere.entity.Comment;
import com.collabsphere.entity.Post;
import com.collabsphere.entity.PostType;
import com.collabsphere.entity.User;
import com.collabsphere.repository.CommentRepository;
import com.collabsphere.repository.PostRepository;
import com.collabsphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            System.out.println("Seeding database with initial users, posts, and comments...");

            // Create users
            User arjun = createUser("arjun@collabsphere.com", "arjun", "Arjun Sharma", "3rd", "AI/ML");
            User priya = createUser("priya@collabsphere.com", "priya", "Priya Patel", "4th", "Web Development");
            User riya = createUser("riya@collabsphere.com", "riya", "Riya Singh", "2nd", "Design");

            // Create posts
            Post post1 = createPost(arjun, "Looking for Frontend developer", 
                "Building an AI powered task manager. Need someone good with React.",
                PostType.PROJECT, "React, CSS", "Python, ML", "AI/ML");
                
            Post post2 = createPost(priya, "Can help with React and Node.js", 
                "I have 2 years of experience with MERN stack. Let me know if anyone needs help or mentorship.",
                PostType.SKILL_SHARE, "None", "React, Node.js, MongoDB", "Web Development");
                
            Post post3 = createPost(riya, "UI/UX Design for your ideas", 
                "I am a designer looking to collaborate on interesting projects to build my portfolio.",
                PostType.PROJECT, "Frontend Developer", "Figma, UI/UX", "Design");

            // Create comments
            createComment(priya, post1, "Hey Arjun, I'm interested! I have experience with React.");
            createComment(arjun, post2, "Priya, could you mentor me on Node.js auth?");
            createComment(priya, post3, "Riya, let's collaborate! I can build out your designs.");

            System.out.println("Database seeding completed!");
        } else {
            System.out.println("Database already contains data, skipping seeding.");
        }
    }

    private User createUser(String email, String username, String fullName, String year, String skills) {
        User user = User.builder()
                .email(email)
                .username(username)
                .fullName(fullName)
                .password(passwordEncoder.encode("password123"))
                .year(year)
                .skills(skills)
                // Initialize collections
                .posts(new ArrayList<>())
                .sentRequests(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return userRepository.save(user);
    }

    private Post createPost(User author, String title, String description, PostType type, 
                            String skillsRequired, String skillsOffered, String domain) {
        Post post = Post.builder()
                .title(title)
                .description(description)
                .type(type)
                .author(author)
                .skillsRequired(skillsRequired)
                .skillsOffered(skillsOffered)
                .domain(domain)
                .openForCollaboration(true)
                .likesCount(0)
                .collaboratorsCount(0)
                // Initialize collections
                .comments(new ArrayList<>())
                .collaborationRequests(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return postRepository.save(post);
    }

    private Comment createComment(User author, Post post, String content) {
        Comment comment = Comment.builder()
                .author(author)
                .post(post)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        return commentRepository.save(comment);
    }
}
