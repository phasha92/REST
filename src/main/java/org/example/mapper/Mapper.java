package org.example.mapper;

public interface Mapper <T,S>{
    S toDTO(T t);
    T fromDTO(S s);
}
