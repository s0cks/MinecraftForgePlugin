package io.github.s0cks.mcfip.util;

import java.util.EnumSet;
import javax.lang.model.element.Modifier;

public class StandardModifiers{
    public static final EnumSet<Modifier> PUBLIC_FINAL = EnumSet.of(Modifier.PUBLIC, Modifier.FINAL);
    public static final EnumSet<Modifier> PUBLIC = EnumSet.of(Modifier.PUBLIC);
}