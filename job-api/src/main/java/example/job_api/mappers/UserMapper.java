package example.job_api.mappers;

import example.job_api.dto.UserDto;
import example.job_api.entities.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user){
        return new UserDto(

                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

}
