package ru.kifor4ik.ProjectsManager.Controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.kifor4ik.ProjectsManager.Configs.Security.Security;
import ru.kifor4ik.ProjectsManager.Entity.Status;
import ru.kifor4ik.ProjectsManager.Exeptions.ProjectNotFoundException;
import ru.kifor4ik.ProjectsManager.Exeptions.UserAlreadyExistExeption;
import ru.kifor4ik.ProjectsManager.Exeptions.UserNotFoundExeption;
import ru.kifor4ik.ProjectsManager.Entity.UserEntity;
import ru.kifor4ik.ProjectsManager.Exeptions.WrongStatusCodeException;
import ru.kifor4ik.ProjectsManager.Models.RegistrationModel;
import ru.kifor4ik.ProjectsManager.Models.UserModel;
import ru.kifor4ik.ProjectsManager.Service.UserService;

import java.util.List;

@RestController
@RequestMapping("/profile")
@Api(value = "profile resources", description = "Operations")
public class UserController {

    @Autowired
    public UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping
    public ResponseEntity newUser(@RequestBody RegistrationModel user){

        try {
            userService.registration(user);
            return ResponseEntity.ok("Successfully registered!");

        } catch (UserAlreadyExistExeption e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Oops.. Something wrong..");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getOneUser(@PathVariable Long id){

        try {
            return ResponseEntity.ok().body(UserModel.toModel(userService.findById(id)));
        } catch (UserNotFoundExeption e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Oops.. Something wrong..");
        }
    }

    @PutMapping
    public ResponseEntity editUser(@RequestParam Long id,
                                   @RequestBody UserEntity userNewParams){
        try {

            if (id == userNewParams.getId()) {
                return ResponseEntity.ok(UserModel.toModel(userService.editUser(id, userNewParams)).getId()
                        + " was changed.");
            } else{
                return ResponseEntity.badRequest().body("Bad params or pass not equals");
            }

        } catch (UserAlreadyExistExeption e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNotFoundExeption e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Oops.. Something wrong..");
        }
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity deleteUser(@RequestParam String email){
        try {
            userService.setProfileStatus(email, 1);
            return ResponseEntity.ok(email + " was deleted");
        } catch (UserNotFoundExeption e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Oops.. Something wrong..");
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity getAllUsers(@PageableDefault(sort = {"id","nickname"},direction = Sort.Direction.ASC,page = 0,size = 5) Pageable pageable,
                                      @RequestParam(required = false) long size, @RequestParam(required = false) long page){

        List<UserModel> list = userService.getAll(pageable);
        return ResponseEntity.ok().body(new PageImpl<>(list, pageable, list.size()));
    }

    @GetMapping("/join")
    public ResponseEntity joinToProject(@RequestParam String accessCode, @RequestParam Long userId){

        try{
            return ResponseEntity.ok().body(userService.joinToProject(userId, accessCode));
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage()  );
        } catch (UserNotFoundExeption e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/setAdmin")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity setAdmin(@RequestParam String email){

        try {
            return ResponseEntity.ok().body(userService.giveAdminRights(email));
        } catch (UserNotFoundExeption userNotFoundExeption) {
            userNotFoundExeption.printStackTrace();
            return ResponseEntity.badRequest().body(userNotFoundExeption.getMessage());
        }
    }

    @GetMapping("/setStatus")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity setStatus(@RequestParam String email, @RequestParam int status){

        try {
            return ResponseEntity.ok().body(userService.setProfileStatus(email, status));
        } catch (UserNotFoundExeption | WrongStatusCodeException userNotFoundExeption) {
            userNotFoundExeption.printStackTrace();
            return ResponseEntity.badRequest().body(userNotFoundExeption.getMessage());
        }
    }
}
