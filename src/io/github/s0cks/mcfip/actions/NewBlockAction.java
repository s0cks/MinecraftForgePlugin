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
import io.github.s0cks.mcfip.util.StandardModifiers;
import org.jetbrains.annotations.NotNull;

import java.io.StringWriter;

public final class NewBlockAction
extends CreateElementActionBase{
    public NewBlockAction(){
        super("New Block", "New Block", null);
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

            writer.beginType("Block" + s, "class", StandardModifiers.PUBLIC_FINAL, "net.minecraft.block.Block")
                    .beginConstructor(StandardModifiers.PUBLIC)
                        .emitStatement("super(net.minecraft.block.material.Material.rock)")
                    .endConstructor()
                  .endType();

            code = sw.toString();
        }

        PsiFile newFile = factory.createFileFromText("Block" + s + ".java", JavaFileType.INSTANCE, code);
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