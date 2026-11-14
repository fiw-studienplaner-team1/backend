package htw.team1.studyPlanner.dto;

import htw.team1.studyPlanner.entity.ModulArt;

public record ModuleRequestDTO(String id, String name, int semester, int ects, int sws, ModulArt art, String turnus)
    //TODO welche von den attributen sollten wir vom frontend veraenderbar machen und welche nicht (die dann hier raus)
{
}
