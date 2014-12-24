package io.github.s0cks.mcfip.inspections;

import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiField;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public final class MissingInstanceInspection
extends LocalInspectionTool{
    private static final Logger log = Logger.getLogger(MissingInstanceInspection.class);

    @NotNull
    @Override
    public String getGroupDisplayName(){
        return "Forge Inspections";
    }

    @NotNull
    @Override
    public String getDisplayName(){
        return "Invalid Instance Inspection";
    }

    @NotNull
    public String getShortName(){
        String name = getClass().getSimpleName();
        return name.substring(0, name.length() - "Inspection".length());
    }

    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean onFly){
        log.info("Creating Visitor");
        return new MissingInstanceVisitor(holder);
    }

    private static final class MissingInstanceVisitor
    extends JavaElementVisitor{
        private static final Logger log = Logger.getLogger(MissingInstanceVisitor.class);

        private final ProblemsHolder holder;

        private MissingInstanceVisitor(ProblemsHolder holder){
            this.holder = holder;
        }

        @Override
        public void visitClass(PsiClass clazz){
            log.info("Inspecting");
            if(AnnotationUtil.isAnnotated(clazz, "cpw.mods.fml.common.Mod", true)){
                log.info("Found Mod Class");
                PsiField field = clazz.findFieldByName("instance", true);
                if(field == null){
                    log.info("Can't find instance field");
                    holder.registerProblem(clazz, "No Instance Available");
                } else{
                    if(!AnnotationUtil.isAnnotated(field, "cpw.mods.fml.common.Mod.Instance", true)){
                        log.info("Found instance field, no Mod.Instance annotation");
                        holder.registerProblem(field, "Instance Field Needs @Mod.Instance Annotation");
                    }
                }
            }
        }
    }
}