package egs.task.controllers;

import egs.task.exceptions.EntityNotFoundException;
import egs.task.facade.userFacade.UserFacadeBuilder;
import egs.task.models.ResponseModel;
import egs.task.utils.Configuration;
import egs.task.utils.FileUtilForTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("user/")
@Api(value = "user")
public class UserController extends BaseController {

    @Lazy
    private final UserFacadeBuilder facadeBuilder;

    public UserController(UserFacadeBuilder facadeBuilder) {
        this.facadeBuilder = facadeBuilder;
    }

    @ApiOperation(value = "Api for user to upload image.")
    @PostMapping("uploadImage")
    @PreAuthorize("hasRole('USER')")
    public ResponseModel uploadImage(@Valid @RequestParam("file") MultipartFile file, Authentication authentication) {
        try {
            return createResult(facadeBuilder.uploadImage(file, authentication), "Photo of user was uploaded successfully");
        } catch (EntityNotFoundException e) {
            return createErrorResult(e);
        }
    }

    @GetMapping("getImage/{filename:.+}")
    public byte[] serveFile(@PathVariable String filename) throws IOException {
        return FileUtilForTask.loadFile(filename, Configuration.USER_IMAGE_DIR_IN_PROJECT, -1, -1);
    }
}
