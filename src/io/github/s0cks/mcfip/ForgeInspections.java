package io.github.s0cks.mcfip;

import com.intellij.codeInspection.InspectionToolProvider;
import io.github.s0cks.mcfip.inspections.MissingInstanceInspection;
import org.apache.log4j.Logger;

public final class ForgeInspections
implements InspectionToolProvider{
    private static final Logger log = Logger.getLogger(ForgeInspections.class);

    @Override
    public Class[] getInspectionClasses(){
        log.info("Returning Inspection Classes");
        return new Class[]{
                MissingInstanceInspection.class
        };
    }
}