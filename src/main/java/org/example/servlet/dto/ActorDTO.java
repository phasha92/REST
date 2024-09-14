package org.example.servlet.dto;

import java.util.List;

public record ActorDTO(int id, String name, List<String> filmTitles) {
}

