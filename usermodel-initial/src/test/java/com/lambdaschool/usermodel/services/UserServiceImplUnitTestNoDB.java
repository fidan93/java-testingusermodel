package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplicationTesting;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.repository.UserRepository;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplicationTesting.class,
properties = "command.line.runner.enabled=false")
public class UserServiceImplUnitTestNoDB
{
    @MockBean
    private UserRepository userrepos;

    @Autowired
    private UserService userService;

    @MockBean
    private RoleService roleService;

    private List<User> userList = new ArrayList<>();

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
        u1.getUseremails().get(0).setUseremailid(11);
        u1.getUseremails()
            .add(new Useremail(u1,
                "admin@mymail.local"));
        u1.getUseremails().get(1).setUseremailid(12);

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
        u2.getUseremails().get(0).setUseremailid(21);
        u2.getUseremails()
            .add(new Useremail(u2,
                "hops@mymail.local"));
        u2.getUseremails().get(1).setUseremailid(22);
        u2.getUseremails()
            .add(new Useremail(u2,
                "bunny@email.local"));
        u2.getUseremails().get(2).setUseremailid(23);
        userList.add(u2);

        // user
        User u3 = new User("barnbarn",
            "ILuvM4th!",
            "barnbarn@lambdaschool.local");
        u3.setUserid(30);
        u3.getRoles()
            .add(new UserRoles(u3,
                r2));
        u3.getUseremails()
            .add(new Useremail(u3,
                "barnbarn@email.local"));
        u3.getUseremails().get(0).setUseremailid(31);
        userList.add(u3);

    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void findUserById()
    {
        Mockito.when(userrepos.findById(2L))
            .thenReturn(Optional.of(userList.get(0)));

        assertEquals("admin",userService.findUserById(2L).getUsername());
    }

    @Test
    public void findByNameContaining()
    {
        Mockito.when(userrepos.findByUsernameContainingIgnoreCase("min"))
            .thenReturn(userList);
        assertEquals(3,userService.findByNameContaining("min").size());
    }

    @Test
    public void findAll()
    {
        Mockito.when(userrepos.findAll())
            .thenReturn(userList);

        assertEquals(3,userService.findAll().size());
    }

    @Test
    public void delete()
    {
        Mockito.when(userrepos.findById(4L))
            .thenReturn(Optional.of(userList.get(0)));

        Mockito.doNothing()
            .when(userrepos)
            .deleteById(4L);
        userService.delete(4);
        assertEquals(3,userList.size());
    }

    @Test
    public void findByName()
    {
        Mockito.when(userrepos.findByUsername("barnbarn"))
            .thenReturn(userList.get(0));

        assertEquals("admin",userService.findByName("barnbarn").getUsername());
    }

    @Test
    public void save()
    {
        Role r2 = new Role("user");
        User u3 = new User("barnbarn",
            "ILuvM4th!",
            "barnbarn@lambdaschool.local");

        u3.getRoles()
            .add(new UserRoles(u3,
                r2));
        u3.getUseremails()
            .add(new Useremail(u3,
                "barnbarn@email.local"));


        Mockito.when(userrepos.save(any(User.class)))
            .thenReturn(u3);
        Mockito.when(roleService.findRoleById(2L))
            .thenReturn(r2);

        User addUser = userService.save(u3);
        assertNotNull(addUser);
        assertEquals("barnbarn",addUser.getUsername());
    }

//    @Test
//    public void update()
//    {
//        Role r2 = new Role("user");
//        Role r3 = new Role("data"); // data, user
//
//        r2.setRoleid(2);
//        r3.setRoleid(3);
//
//         User u2 = new User("cinnamon",
//            "1234567",
//            "cinnamon@lambdaschool.local");
//        u2.setUserid(20);
//        u2.getRoles()
//            .add(new UserRoles(u2,
//                r2));
//        u2.getRoles()
//            .add(new UserRoles(u2,
//                r3));
//
//        u2.getUseremails()
//            .add(new Useremail(u2,
//                "cinnamon@mymail.local"));
//        u2.getUseremails()
//            .add(new Useremail(u2,
//                "hops@mymail.local"));
//        u2.getUseremails()
//            .add(new Useremail(u2,
//                "bunny@email.local"));
//
////        ObjectMapper objectMapper = new ObjectMapper();
////     User u3 = objectMapper.readValue(objectMapper.write)
//
//        Mockito.when(userrepos.findById(20L))
//            .thenReturn(r2);
//
//    }

    @Test
    public void deleteAll()
    {
        Mockito.doNothing()
            .when(userrepos)
            .deleteAll();
        userService.deleteAll();
        assertEquals(3,userList.size());

    }
}