package io.github.s0cks.mcfip.util;

import java.util.HashMap;

public class AttributeMap
extends HashMap<String, String>{
    public AttributeMap add(String name, String value){
        this.put(name, value);
        return this;
    }
}