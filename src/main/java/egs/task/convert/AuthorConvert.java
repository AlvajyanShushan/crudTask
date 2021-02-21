package egs.task.convert;

import egs.task.models.dtos.author.AuthorDto;
import egs.task.models.entities.Author;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorConvert {
    public static List<Author> convertToEntityList(List<AuthorDto> authorDtoList) {
        return authorDtoList.stream().map(AuthorConvert::convertToEntity)
                .collect(Collectors.toList());
    }

    private static Author convertToEntity(AuthorDto authorDto) {
        return Author.builder()
                .firstName(authorDto.getFirstName())
                .lastName(authorDto.getLastName())
                .patronymic(authorDto.getPatronymic())
                .build();
    }

    public static List<AuthorDto> convertToDtoList(List<Author> authors) {
        return authors.stream().map(AuthorConvert::convertToDto)
                .collect(Collectors.toList());
    }

    private static AuthorDto convertToDto(Author author) {
        return AuthorDto.builder()
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .patronymic(author.getPatronymic())
                .build();
    }
}
