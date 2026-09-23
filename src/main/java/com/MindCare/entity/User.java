package com.MindCare.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @ManyToMany
    @JsonIgnore // Prevent infinite recursion when serializing
    @JoinTable(
            name = "user_community",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "community_id")
    )
    private Set<Community> joinedCommunities = new HashSet<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<GroqResponse> aiResponses = new HashSet<>();

    // Constructors
    public User() {}

    public User(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }



    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Community> getJoinedCommunities() {
        return joinedCommunities;
    }

    public void setJoinedCommunities(Set<Community> joinedCommunities) {
        this.joinedCommunities = joinedCommunities;
    }

    public Set<GroqResponse> getAiResponses() {
        return aiResponses;
    }

    public void setAiResponses(Set<GroqResponse> aiResponses) {
        this.aiResponses = aiResponses;
    }

    // Utility method for joining community
    public void joinCommunity(Community community) {
        this.joinedCommunities.add(community);
        community.getUsers().add(this);
    }

    // Utility method to add AI response
    public void addGroqResponse(GroqResponse response) {
        aiResponses.add(response);
        response.setUser(this);
    }
}
