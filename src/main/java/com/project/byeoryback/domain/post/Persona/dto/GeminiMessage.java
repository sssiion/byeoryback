package com.project.byeoryback.domain.post.Persona.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class GeminiMessage {

    // --- 요청(Request) 구조 ---
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private List<Content> contents;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Content {
        private List<Part> parts;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Part {
        private String text;
    }

    // --- 응답(Response) 구조 ---
    @Data
    @NoArgsConstructor
    public static class Response {
        private List<Candidate> candidates;
    }

    @Data
    @NoArgsConstructor
    public static class Candidate {
        private Content content;
    }
}