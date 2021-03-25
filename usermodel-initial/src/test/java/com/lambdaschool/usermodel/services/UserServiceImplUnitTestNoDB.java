package com.lambdaschool.usermodel.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.UserModelApplicationTesting;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplicationTesting.class,
properties = "command.line.runner.enabled=false")
public class UserServiceImplUnitTestNoDB
{

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleService roleService;

    @MockBean
    private HelperFunctions helperFunctions;

    List<User> userList = new ArrayList<>();

    @Before
    public void setUp() throws Exception
    {
        Role r1 = new Role("admin");
        Role r2 = new Role("user");
        Role r3 = new Role("data");

        r1.setRoleid(1);
        r2.setRoleid(2);
        r3.setRoleid(3);

        // admin, data, user
        User u1 = new User("admin",
            "password",
            "admin@lambdaschool.local");
        u1.setUserid(10);
        u1.getRoles()
            .add(new UserRoles(u1,
                r1));
        u1.getRoles()
            .add(new UserRoles(u1,
                r2));
        u1.getRoles()
            .add(new UserRoles(u1,
                r3));
        u1.getUseremails()
            .add(new Useremail(u1,
                "admin@email.local"));
        u1.getUseremails()
            .add(new Useremail(u1,
                "admin@mymail.local"));

         userList.add(u1);
        // data, user
        User u2 = new User("cinnamon",
            "1234567",
            "cinnamon@lambdaschool.local");
        u2.setUserid(20);
        u2.getRoles()
            .add(new UserRoles(u2,
                r2));
        u2.getRoles()
            .add(new UserRoles(u2,
                r3));
        u2.getUseremails()
            .add(new Useremail(u2,
                "cinnamon@mymail.local"));
        u2.getUseremails()
            .add(new Useremail(u2,
                "hops@mymail.local"));
        u2.getUseremails()
            .add(new Useremail(u2,
                "bunny@email.local"));

        userList.add(u2);

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void findUserById()
    {
        Mockito.when(userRepository.findById(3L))
            .thenReturn(Optional.of(userList.get(0)));

        assertEquals("admin",userService.findUserById(3L).getUsername());
    }

    @Test
    public void findByNameContaining()
    {
        Mockito.when(userRepository.findByUsernameContainingIgnoreCase("min"))
            .thenReturn(userList);
        assertEquals(2,userService.findByNameContaining("min").size());
    }

    @Test
    public void findAll()
    {
        Mockito.when(userRepository.findAll())
            .thenReturn(userList);
        assertEquals(2,userService.findAll().size());
    }

    @Test
    public void delete()
    {
        Mockito.when(userRepository.findById(1L))
            .thenReturn(Optional.of(userList.get(0)));

        Mockito.doNothing()
            .when(userRepository)
            .deleteById(1L);

        userService.delete(1);
        assertEquals(2,userList.size());
    }

    @Test
    public void findByName()
    {
        Mockito.when(userRepository.findByUsername("admin"))
            .thenReturn(userList.get(0));
        assertEquals("admin",userService.findByName("admin").getUsername());
    }

    @Test
    public void save()
    {
        Role r1 = new Role("admin");
        Role r2 = new Role("user");
        Role r3 = new Role("data");

        r1.setRoleid(1);
        r2.setRoleid(2);
        r3.setRoleid(3);

        // admin, data, user
        User u1 = new User("admin",
            "password",
            "admin@lambdaschool.local");
        u1.getRoles()
            .add(new UserRoles(u1,
                r1));
        u1.getRoles()
            .add(new UserRoles(u1,
                r2));
        u1.getRoles()
            .add(new UserRoles(u1,
                r3));
        u1.getUseremails()
            .add(new Useremail(u1,
                "admin@email.local"));
        u1.getUseremails()
            .add(new Useremail(u1,
                "admin@mymail.local"));

       Mockito.when(userRepository.save(any(User.class)))
           .thenReturn(u1);
       Mockito.when(roleService.findRoleById(1L))
           .thenReturn(r1);

       User addUser = userService.save(u1);
       assertNotNull(addUser);
       assertEquals("admin",addUser.getUsername());
    }

    @Test
    public void saveput()
    {
        Role r1 = new Role("admin");
        Role r2 = new Role("user");
        Role r3 = new Role("data");

        r1.setRoleid(1);
        r2.setRoleid(2);
        r3.setRoleid(3);

        // admin, data, user
        User u1 = new User("admin",
            "password",
            "admin@lambdaschool.local");
        u1.setUserid(1);
        u1.getRoles()
            .add(new UserRoles(u1,
                r1));
        u1.getRoles()
            .add(new UserRoles(u1,
                r2));
        u1.getRoles()
            .add(new UserRoles(u1,
                r3));
        u1.getUseremails()
            .add(new Useremail(u1,
                "admin@email.local"));
        u1.getUseremails()
            .add(new Useremail(u1,
                "admin@mymail.local"));

        Mockito.when(userRepository.findById(1L))
            .thenReturn(Optional.of(u1));

        Mockito.when(roleService.findRoleById(1L))
            .thenReturn(r1);

        Mockito.when(userRepository.save(any(User.class )))
            .thenReturn(u1);

        assertEquals(1L,userService.save(u1).getUserid());
    }
    @Test
    public void update() throws Exception
    {
        Role r1 = new Role("admin");
        Role r2 = new Role("user");
        Role r3 = new Role("data");

        r1.setRoleid(1);
        r2.setRoleid(2);
        r3.setRoleid(3);

        // admin, data, user
        User u1 = new User("admin",
            "password",
            "admin@lambdaschool.local");
        u1.setUserid(10);
        u1.getRoles()
            .add(new UserRoles(u1,
                r1));
        u1.getRoles()
            .add(new UserRoles(u1,
                r2));
        u1.getRoles()
            .add(new UserRoles(u1,
                r3));
        u1.getUseremails()
            .add(new Useremail(u1,
                "admin@email.local"));
        u1.getUseremails()
            .add(new Useremail(u1,
                "admin@mymail.local"));

        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper
            .readValue(objectMapper.writeValueAsString(u1),User.class);

        Mockito.when(helperFunctions.isAuthorizedToMakeChange(u1.getUsername()))
        .thenReturn(true);
        Mockito.when(userRepository.findById(10L))
            .thenReturn(Optional.of(u1));
        Mockito.when(roleService.findRoleById(1L))
            .thenReturn(r1);

        Mockito.when(userRepository.save(any(User.class)))
            .thenReturn(u1);
        User updateUser = userService.update(u1,10);
        assertNotNull(updateUser);
        assertEquals("admin",updateUser.getUsername());
    }

    @Test
    public void deleteAll()
    {
        Mockito.doNothing()
            .when(userRepository)
            .deleteAll();

        userService.deleteAll();
        assertEquals(2,userList.size());
    }
}