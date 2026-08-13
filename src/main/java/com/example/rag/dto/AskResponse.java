package com.example.rag.dto;
import java.util.List;
public record AskResponse(String answer,String pageTitle,String url,List<Source> sources){public record Source(String title,String url){}}
