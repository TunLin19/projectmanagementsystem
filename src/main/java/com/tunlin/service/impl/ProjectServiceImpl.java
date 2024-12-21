package com.tunlin.service.impl;

import com.tunlin.modal.Chat;
import com.tunlin.modal.Project;
import com.tunlin.modal.User;
import com.tunlin.repository.ProjectRepository;
import com.tunlin.service.ChatService;
import com.tunlin.service.ProjectService;
import com.tunlin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    @Override
    public Project createProject(Project project, User owner) throws Exception {

        Project createProject = new Project();

        createProject.setOwner(owner);
        createProject.setTags(project.getTags());
        createProject.setName(project.getName());
        createProject.setCategory(project.getCategory());
        createProject.setDescription(project.getDescription());
        createProject.getTeam().add(owner);

        Project saveProject = projectRepository.save(createProject);

        Chat chat = new Chat();
        chat.setProject(saveProject);

        Chat projectChat = chatService.createChat(chat);
        saveProject.setChat(projectChat);
        return saveProject;

    }

    @Override
    public List<Project> getProjectByTeam(User user, String category, String tag) throws Exception {

        List<Project> projects = projectRepository.findByTeamContainingOrOwner(user, user);

        if (category !=null){
            projects = projects.stream().filter(project -> project.getCategory().equals(category))
                    .collect(Collectors.toList());
        }
        if (tag !=null){
            projects = projects.stream().filter(project -> project.getTags().equals(tag))
                    .collect(Collectors.toList());
        }
        return projects;

    }

    @Override
    public Project getProjectById(Long projectId) throws Exception {

        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null){
            throw new Exception("Project not found");
        }
        return project;
    }

    @Override
    public void deleteProject(Long projectId, Long userId) throws Exception {

        getProjectById(projectId);
        projectRepository.deleteById(projectId);

    }

    @Override
    public Project updateProject(Project updateProject, Long id) throws Exception {

        Project project = getProjectById(id);

        project.setName(updateProject.getName());
        project.setDescription(updateProject.getDescription());
        project.setTags(updateProject.getTags());

        return projectRepository.save(project);

    }

    @Override
    public void addUserToProject(Long projectId, Long userId) throws Exception {

        Project project = getProjectById(projectId);
        User user = userService.findUserById(userId);
        if (!project.getTeam().contains(user)){
            project.getChat().getUsers().add(user);
            project.getTeam().add(user);
        }
        projectRepository.save(project);

    }

    @Override
    public void removeUserToProject(Long projectId, Long userId) throws Exception {

        Project project = getProjectById(projectId);
        User user = userService.findUserById(userId);
        if (!project.getTeam().contains(user)){
            project.getChat().getUsers().remove(user);
            project.getTeam().remove(user);
        }
        projectRepository.save(project);

    }

    @Override
    public Chat getChatByProjectId(Long projectId) throws Exception {

        Project project = getProjectById(projectId);
        return project.getChat();

    }

    @Override
    public List<Project> searchProject(String keyword, User user) throws Exception {

        List<Project> projects = projectRepository.findByNameContainingAndTeamContains(keyword, user);
        return projects;

    }
}
