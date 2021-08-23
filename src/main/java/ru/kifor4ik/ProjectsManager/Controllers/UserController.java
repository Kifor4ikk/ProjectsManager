package ru.kifor4ik.ProjectsManager.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.kifor4ik.ProjectsManager.Configs.Security.Security;
import ru.kifor4ik.ProjectsManager.Exeptions.ProjectNotFoundException;
import ru.kifor4ik.ProjectsManager.Exeptions.UserAlreadyExistExeption;
import ru.kifor4ik.ProjectsManager.Exeptions.UserNotFoundExeption;
import ru.kifor4ik.ProjectsManager.Entity.UserEntity;
import ru.kifor4ik.ProjectsManager.Models.UserModel;
import ru.kifor4ik.ProjectsManager.Service.UserService;

@RestController
@RequestMapping("/profile")
public class UserController {

    @Autowired
    public UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/{id}/test")
    public ResponseEntity getOneUserOrig(@PathVariable Long id, @RequestParam String password){
        try {
            UserEntity user = userService.findById(id);
            return ResponseEntity.ok(user.getPassword().equals(passwordEncoder.encode(password)));
        } catch (UserNotFoundExeption e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Oops.. Something wrong..");
        }
    }

    @PostMapping
    public ResponseEntity newUser(@RequestBody UserEntity user){

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
            return ResponseEntity.ok(UserModel.toModel(userService.findById(id)));
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
    public ResponseEntity deleteUser(@RequestParam Long id){
        try {

            return ResponseEntity.ok(userService.delete(id).getNickname() + " was deleted");
        } catch (UserNotFoundExeption e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Oops.. Something wrong..");
        }
    }

    @GetMapping("/all")
    public ResponseEntity getAllUsers(){

        return ResponseEntity.ok(userService.getAll());
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
}
