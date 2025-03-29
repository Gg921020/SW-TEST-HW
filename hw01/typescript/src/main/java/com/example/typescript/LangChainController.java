package com.example.typescript;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LangChainController {

    @Autowired
    private langchain langchain;

    @RequestMapping("/langchain")
    public String langChain() {
        return langchain.langchain4j();
    }


}
