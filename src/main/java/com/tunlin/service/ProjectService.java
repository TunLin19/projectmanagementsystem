package com.tunlin.service;

import com.tunlin.modal.Chat;
import com.tunlin.modal.Project;
import com.tunlin.modal.User;

import java.util.List;

public interface ProjectService {

    Project createProject(Project project, User owner) throws Exception;

    List<Project> getProjectByTeam(User user, String category, String tag) throws  Exception;

    Project getProjectById(Long projectId) throws Exception;

    void deleteProject(Long projectId, Long userId) throws Exception;

    Project updateProject(Project updateProject, Long id) throws Exception;

    void addUserToProject(Long projectId, Long userId) throws  Exception;

    void removeUserToProject(Long projectId, Long userId) throws  Exception;

    Chat getChatByProjectId(Long projectId) throws Exception;

    List<Project> searchProject(String keyword, User user) throws Exception;

}
