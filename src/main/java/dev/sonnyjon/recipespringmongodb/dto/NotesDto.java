package dev.sonnyjon.recipespringmongodb.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Sonny on 7/8/2022.
 */
@Getter
@Setter
@NoArgsConstructor
public class NotesDto
{
    private String id;
    private String recipeNotes;
}
