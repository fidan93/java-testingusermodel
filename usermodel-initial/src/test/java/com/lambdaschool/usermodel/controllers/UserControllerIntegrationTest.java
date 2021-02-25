package com.lambdaschool.usermodel.controllers;

import com.lambdaschool.usermodel.UserModelApplicationTesting;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.services.RoleService;
import com.lambdaschool.usermodel.services.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.emptyArray;
import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
classes = UserModelApplicationTesting.class)
@AutoConfigureMockMvc
@WithUserDetails(value = "admin")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerIntegrationTest
{

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;
    @Before
    public void setUp() throws Exception
    {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        List<User> myList = userService.findAll();
        for (User u : myList)
        {
            System.out.println(u.getUserid()+" "+u.getUsername());
        }

        List<Role> myRole = roleService.findAll();
        for(Role r : myRole)
        {
            System.out.println(r.getRoleid()+" "+r.getName());
        }
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void a_listAllUsers()
    {
        given().when()
            .get("/users/users")
            .then()
            .statusCode(200)
            .and()
            .body(containsString("admin"));
    }

    @Test
    public void b_getUserById()
    {
        given().when()
            .get("users/user/7")
            .then()
            .statusCode(200)
            .and()
            .body(containsString("cinnamon"));
    }

    @Test
    public void c_getUserByName()
    {
        given().when()
            .get("/users/user/name/cinnamon")
            .then()
            .statusCode(200)
            .and()
            .body(containsString("cinnamon"));
    }

    @Test
    public void d_getUserLikeName()
    {
        given().when()
            .get("/users/user/name/like/da")
            .then()
            .statusCode(200)
            .and()
            .body(containsString("[]"));
    }

    @Test
    public void e_addNewUser() throws Exception
    {
       String jsonInput = "{\n" +
           "    \"username\": \"Mojo\",\n" +
           "    \"primaryemail\": \"mojo@lambdaschool.local\",\n" +
           "    \"password\" : \"Coffee123\",\n" +
           "    \"useremails\": [\n" +
           "        {\n" +
           "            \"useremail\": \"mojo@mymail.local\"\n" +
           "        },\n" +
           "        {\n" +
           "            \"useremail\": \"mojo@email.local\"\n" +
           "        }\n" +
           "        ],\n" +
           "    \"roles\": [\n" +
           "        {\n" +
           "            \"role\": {\n" +
           "                \"roleid\": 1\n" +
           "            }\n" +
           "        },\n" +
           "        {\n" +
           "            \"role\": {\n" +
           "                \"roleid\": 2\n" +
           "            }\n" +
           "        }\n" +
           "    ]\n" +
           "}";

       mockMvc.perform(MockMvcRequestBuilders.post("/users/user")
       .content(jsonInput)
       .contentType(MediaType.APPLICATION_JSON)
       .accept(MediaType.APPLICATION_JSON))
       .andDo(print())
       .andExpect(status().isCreated())
       .andExpect(MockMvcResultMatchers.header()
       .exists("location"));
    }

    @Test
    public void f_updateFullUser() throws Exception
    {
        String json = "{\n" +
            "    \"username\": \"stumps\",\n" +
            "    \"primaryemail\": \"stumps@lambdaschool.local\",\n" +
            "    \"password\" : \"EarlGray123\",\n" +
            "    \"useremails\": [\n" +
            "        {\n" +
            "            \"useremail\": \"stumps@mymail.local\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"useremail\": \"stumps@email.local\"\n" +
            "        }\n" +
            "        ],\n" +
            "    \"roles\": [\n" +
            "        {  \n" +
            "            \"role\": {\n" +
            "                \"roleid\": 3\n" +
            "            }\n" +
            "        },\n" +
            "        {  \n" +
            "            \"role\": {\n" +
            "                \"roleid\": 1\n" +
            "            }\n" +
            "        }\n" +
            "    ]\n" +
            "}";
        mockMvc.perform(MockMvcRequestBuilders.put("/users/user/14")
        .content(json)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    public void g_updateUser() throws Exception
    {
        String json = "{\n" +
            "    \"username\": \"cinabun\",\n" +
            "    \"primaryemail\": \"cinabun@lambdaschool.home\",\n" +
            "    \"useremails\": [\n" +
            "    {\n" +
            "            \"useremail\": \"cinnamon@mymail.home\"\n" +
            "    },\n" +
            "    {\n" +
            "            \"useremail\": \"hops@mymail.home\"\n" +
            "    },\n" +
            "    {\n" +
            "            \"useremail\": \"bunny@email.home\"\n" +
            "    }\n" +
            "    ]\n" +
            "}";

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/user/7")
        .content(json)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    public void h_deleteUserById()
    {
        given().when()
            .delete("/users/user/14")
            .then()
            .statusCode(200);
    }
}