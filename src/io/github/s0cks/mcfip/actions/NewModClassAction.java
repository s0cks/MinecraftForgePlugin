package io.github.s0cks.mcfip.actions;

import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.squareup.javawriter.JavaWriter;
import io.github.s0cks.mcfip.util.AttributeMap;
import io.github.s0cks.mcfip.util.StandardModifiers;
import org.jetbrains.annotations.NotNull;

import java.io.StringWriter;
import java.util.EnumSet;
import javax.lang.model.element.Modifier;

public final class NewModClassAction
extends CreateElementActionBase{
    public NewModClassAction(){
        super("New Mod Class", "New Mod Class", null);
    }

    @NotNull
    @Override
    protected PsiElement[] invokeDialog(Project project, PsiDirectory psiDirectory){
        String name = Messages.showInputDialog("New Block Class", "Create Block Class", Messages.getQuestionIcon());
        if(name != null){
            MyInputValidator validator = new MyInputValidator(project, psiDirectory);
            validator.canClose(name);
            return validator.getCreatedElements();
        }

        return PsiElement.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    protected PsiElement[] create(String s, PsiDirectory psiDirectory)
    throws Exception{
        Project project = psiDirectory.getProject();
        PsiFileFactory factory = PsiFileFactory.getInstance(project);

        String code;
        try(StringWriter sw = new StringWriter();
            JavaWriter writer = new JavaWriter(sw)){
            writer.setCompressingTypes(false);

            writer.emitAnnotation("cpw.mods.fml.common.Mod", new AttributeMap()
                    .add("modid", "modid")
                    .add("name", "name")
                    .add("version", "version"))
                  .beginType(s, "class", StandardModifiers.PUBLIC_FINAL)
                    .emitAnnotation("cpw.mods.fml.common.Mod.Instance", "modid")
                    .emitField(s, "instance", EnumSet.of(Modifier.PUBLIC, Modifier.STATIC))
                  .endType();

            code = sw.toString();
        }
        PsiFile newFile = factory.createFileFromText(s + ".java", JavaFileType.INSTANCE, code);
        PsiElement shortFile = JavaCodeStyleManager.getInstance(project).shortenClassReferences(newFile);
        PsiElement refFile = CodeStyleManager.getInstance(project).reformat(shortFile);
        psiDirectory.add(refFile);
        return new PsiElement[]{
                refFile
        };
    }

    @Override
    protected String getErrorTitle(){
        return null;
    }

    @Override
    protected String getCommandName(){
        return null;
    }

    @Override
    protected String getActionName(PsiDirectory psiDirectory, String s){
        return null;
    }
}