package io.github.s0cks.mcfip.util;

import java.util.LinkedList;

public final class ParameterList
extends LinkedList<String>{
    public ParameterList addParameter(String name, String par){
        this.add(name);
        this.add(par);
        return this;
    }

    public String[] build(){
        return this.toArray(new String[this.size()]);
    }
}