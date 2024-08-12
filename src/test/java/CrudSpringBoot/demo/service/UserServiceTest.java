package CrudSpringBoot.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import CrudSpringBoot.demo.controller.dto.CreateUserDto;
import CrudSpringBoot.demo.controller.dto.UpdateUserDto;
import CrudSpringBoot.demo.entity.User;
import CrudSpringBoot.demo.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock // instancia fake
    private UserRepository userRepository;

    @InjectMocks // instancia real
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    // (A,A,A)

    // Arrange
    // Act
    // Assert

    @Nested // subclass
    class createUser {

        @Test
        @DisplayName("Should create a user")
        void shouldCreateAUser() {

            // Arrange
            User user = new User(
                UUID.randomUUID(),
                "userNameTeste",
                "emailTeste",
                "passwordTeste",
                Instant.now(),
                null
            );

            doReturn(user).when(userRepository).save(
                userArgumentCaptor.capture()
            );

            var input = new CreateUserDto(
            "Matheus Araujo de Melo",
            "matheus123@exemplo.com",
            "senhaforte123"
            );

            // Act
            var output = userService.createUser(input); // return UUID

            // Assert
            assertNotNull(output);
            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(input.username(), userCaptured.getUsername());
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.password(), userCaptured.getPassword());

        }

        @Test
        @DisplayName("Should throw exception")
        void shouldThrowException() {

             // Arrange

            doThrow(new RuntimeException()).when(userRepository).save(any());
            var input = new CreateUserDto(
            "Matheus Araujo de Melo",
            "matheus123@exemplo.com",
            "senhaforte123"
            );

            // Act & Assert
            assertThrows(RuntimeException.class, () -> userService.createUser(input)); // return UUID

        }

    }

    @Nested
    class getUserById {

        @Test
        @DisplayName("Should get by id with optional is present")
        void shouldGetById() {

            // Arrange
            User user = new User(
                UUID.randomUUID(),
                "userNameTeste",
                "emailTeste",
                "passwordTeste",
                Instant.now(),
                null
            );
            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());

            // Act
            var output = userService.getUserById(user.getUserId().toString());

            // Assert
            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());

        }

        @Test
        @DisplayName("Should get by id with optional is empty")
        void shouldGetByIdWithOptionalIsEmpty() {

            // Arrange
            var userId = UUID.randomUUID();

            doReturn(Optional.empty())
                .when(userRepository)
                .findById(uuidArgumentCaptor.capture());

            // Act
            var output = userService.getUserById(userId.toString());

            // Assert
            assertTrue(output.isEmpty());
            assertEquals(userId, uuidArgumentCaptor.getValue());

        }

    }

    @Nested
    class listUsers {

        @Test
        void shouldReturnAllUsers() {

            // Arrange
            // Arrange
            User user = new User(
                UUID.randomUUID(),
                "userNameTeste",
                "emailTeste",
                "passwordTeste",
                Instant.now(),
                null
            );
            doReturn(List.of(user)).when(userRepository).findAll();

            // Act
            var output = userService.listUsers();
            var userList = List.of(user);
            // Assert 
            assertNotNull(output);
            assertEquals(userList.size(), output.size());

        }

    }

    @Nested
    class deleteById {

        @Test
        @DisplayName("Should delete user with sucess")
        void shouldDeleteUser() {

            doReturn(true)
            .when(userRepository)
            .existsById(uuidArgumentCaptor.capture());

            doNothing()
            .when(userRepository)
            .deleteById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();

            userService.deleteById(userId.toString());
            var idList = uuidArgumentCaptor.getAllValues();
            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));

            verify(userRepository, times(1)).existsById(idList.get(0));
            verify(userRepository, times(1)).deleteById(idList.get(1));

        }

        @Test
        @DisplayName("Should not delete when user not exists")
        void shouldNotDeleteUser() {

            doReturn(false)
            .when(userRepository)
            .existsById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();

            userService.deleteById(userId.toString());
            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1)).existsById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).deleteById(uuidArgumentCaptor.getValue());

        }

    }


    @Nested
    class updateUserById {

        @Test
        @DisplayName("Should update user by id")
        void shouldUpdateUserById() {

            // Arrange
            var updateUserDto = new UpdateUserDto(
                "Son Goku",
                "senhaforteigualgoku18"
            );

            User user = new User(
                UUID.randomUUID(),
                "userNameTeste",
                "emailTeste",
                "passwordTeste",
                Instant.now(),
                null
            );

            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());

            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

            // Act
            userService.updateUserById(user.getUserId().toString(), updateUserDto);

            // Assert
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());

            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(updateUserDto.username(), userCaptured.getUsername());
            assertEquals(updateUserDto.password(), userCaptured.getPassword());

            verify(userRepository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(1)).save(user);

        }

        @Test
        @DisplayName("Should not update when user by id not exists")
        void shouldNotUpdateUserById() {

            // Arrange
            var updateUserDto = new UpdateUserDto(
                "Son Goku",
                "senhaforteigualgoku18"
            );
            var userId = UUID.randomUUID();

            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());

            // Act
            userService.updateUserById(userId.toString(), updateUserDto);

            // Assert
            assertEquals(userId, uuidArgumentCaptor.getValue());

           
            verify(userRepository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).save(any());

        }

    }
}
