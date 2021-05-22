package me.ixk.hoshi.ums.controller

import me.ixk.hoshi.common.util.Json
import me.ixk.hoshi.session.config.CompositeSessionIdResolver.X_AUTH_TOKEN
import me.ixk.hoshi.user.repository.RoleRepository
import me.ixk.hoshi.user.repository.UserRepository
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.*

/**
 * @author Otstar Lin
 * @date 2021/5/21 19:45
 */
@WebMvcTest(UserManagerController::class, RoleManagerController::class, UserController::class)
@OverrideAutoConfiguration(enabled = true)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserRoleTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var roleRepository: RoleRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    private var token: String = "";
    private var userId: Int = -1;

    @BeforeAll
    internal fun getToken() {
        this.token = mockMvc.post("/login") {
            contentType = APPLICATION_FORM_URLENCODED
            param("username", "admin")
            param("password", "password")
        }.andExpect {
            status { isOk() }
            header {
                exists(X_AUTH_TOKEN)
            }
        }.andReturn().response.getHeader(X_AUTH_TOKEN) as String
    }

    @Test
    @Order(1)
    fun getCurrentUser() {
        mockMvc.get("/api/users") {
            header(X_AUTH_TOKEN, token)
        }.andExpect {
            status { isOk() }
            jsonPath("$.data.username") {
                value("admin")
            }
        }
    }

    @Test
    @Order(2)
    fun getAllUser() {
        mockMvc.get("/api/admin/users") {
            header(X_AUTH_TOKEN, token)
        }.andExpect {
            status { isOk() }
            jsonPath("$.data.records") {
                isArray()
                isNotEmpty()
            }
        }
    }

    @Test
    @Order(3)
    fun addUser() {
        val response = mockMvc.post("/api/admin/users") {
            header(X_AUTH_TOKEN, token)
            content = """
               {
                 "username": "test",
                 "password": "password",
                 "nickname": "Test",
                 "email": "email@ixk.me",
                 "status": 1,
                 "avatar": null
               }
           """.trimIndent()
            contentType = APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.data.id") {
                isNumber()
            }
        }.andReturn().response
        val json = Json.parse(response.contentAsString)
        userId = json["data"]["id"].asInt();
    }

    @Test
    @Order(4)
    fun updateUser() {
        mockMvc.put("/api/admin/users") {
            header(X_AUTH_TOKEN, token)
            content = """
               {
                 "id": ${userId},
                 "nickname": "Test1"
               }
           """.trimIndent()
            contentType = APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.data.nickname") {
                value("Test1")
            }
        }
    }

    @Test
    @Order(5)
    fun addRole() {
        mockMvc.post("/api/admin/roles") {
            header(X_AUTH_TOKEN, token)
            content = """
               {
                 "name": "TEST",
                 "description": "测试权限",
                 "status": 1
               }
           """.trimIndent()
            contentType = APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    @Order(6)
    fun updateRole() {
        mockMvc.put("/api/admin/roles") {
            header(X_AUTH_TOKEN, token)
            content = """
               {
                 "name": "TEST",
                 "description": "更新权限"
               }
           """.trimIndent()
            contentType = APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.data.description") {
                value("更新权限")
            }
        }
    }

    @Test
    @Order(7)
    fun addRoleToUser() {
        mockMvc.post("/api/admin/users/role") {
            header(X_AUTH_TOKEN, token)
            content = """
               {
                 "id": ${userId},
                 "roles": ["TEST"]
               }
           """.trimIndent()
            contentType = APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.data.roles.length()") {
                value(2)
            }
            jsonPath("$.data.roles[?(@.name == 'TEST')]") {
                exists()
            }
        }
    }

    @Test
    @Order(8)
    fun updateRoleToUser() {
        mockMvc.put("/api/admin/users/role") {
            header(X_AUTH_TOKEN, token)
            content = """
               {
                 "id": ${userId},
                 "roles": ["USER"]
               }
           """.trimIndent()
            contentType = APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.data.roles.length()") {
                value(1)
            }
            jsonPath("$.data.roles[0].name") {
                value("USER")
            }
        }
    }

    @Test
    @Order(9)
    fun deleteRoleToUser() {
        mockMvc.delete("/api/admin/users/role") {
            header(X_AUTH_TOKEN, token)
            param("id", userId.toString())
            param("roles", "USER")
        }.andExpect {
            status { isOk() }
            jsonPath("$.data.roles.length()") {
                value(0)
            }
        }
    }

    @Test
    @Order(10)
    fun deleteUser() {
        mockMvc.delete("/api/admin/users") {
            header(X_AUTH_TOKEN, token)
            param("id", userId.toString())
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    @Order(11)
    fun deleteRole() {
        mockMvc.delete("/api/admin/roles") {
            header(X_AUTH_TOKEN, token)
            param("id", "TEST")
        }.andExpect {
            status { isOk() }
        }
    }

    @AfterAll
    internal fun clean() {
        userRepository.findByUsername("test").ifPresent {
            userRepository.deleteById(it.id)
        }
        roleRepository.findById("TEST").ifPresent {
            roleRepository.deleteById(it.name)
        }
    }
}
