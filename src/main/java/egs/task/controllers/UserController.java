package egs.task.controllers;

import egs.task.exceptions.EntityNotFoundException;
import egs.task.facade.user.UserFacadeBuilder;
import egs.task.models.ResponseModel;
import egs.task.models.dtos.user.UserSearchDto;
import egs.task.utils.Configuration;
import egs.task.utils.FileUtilForTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @ApiOperation(value = "Api for admin to get all users.")
    @PostMapping("getAllUsers/{page}/{size}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseModel getAllUsers(@PathVariable("page") int page, @PathVariable("size") int size,
                                     @RequestBody UserSearchDto userSearchDto) {
        try {
            return createResult(facadeBuilder.getAllUsers(PageRequest.of(page, size, Sort.Direction.DESC, "createdDate"), userSearchDto),
                    "Users were retrieved successfully.");
        } catch (Exception e) {
            return createErrorResult(e);
        }
    }
}
