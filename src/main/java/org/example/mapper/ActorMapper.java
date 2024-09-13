package org.example.mapper;

import org.example.dto.ActorDTO;
import org.example.entity.Actor;

public class ActorMapper implements Mapper<Actor, ActorDTO> {
    @Override
    public ActorDTO toDTO(Actor actor) {

        return null;
    }

    @Override
    public Actor fromDTO(ActorDTO actorDTO) {
        return null;
    }
}
