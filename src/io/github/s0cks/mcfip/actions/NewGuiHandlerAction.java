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
import io.github.s0cks.mcfip.util.ParameterList;
import org.jetbrains.annotations.NotNull;

import java.io.StringWriter;

public final class NewGuiHandlerAction
extends CreateElementActionBase{
    public NewGuiHandlerAction(){
        super("New Gui Handler", "New Gui Handler", null);
    }

    @NotNull
    @Override
    protected PsiElement[] invokeDialog(Project project, PsiDirectory psiDirectory){
        String name = Messages.showInputDialog("New Gui Handler Class", "Create Gui Handler", Messages.getQuestionIcon());
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
        PsiFileFactory factory = PsiFileFactory.getInstance(psiDirectory.getProject());

        String code;
        try(StringWriter sw = new StringWriter();
           JavaWriter writer = new JavaWriter(sw)){
            writer.setCompressingTypes(false);

            writer.beginType(s, "class", StandardModifiers.PUBLIC_FINAL, "", "cpw.mods.fml.common.network.IGuiHandler")
                    .beginMethod("Object", "getServerGuiElement", StandardModifiers.PUBLIC, new ParameterList()
                            .addParameter("int", "ID")
                            .addParameter("net.minecraft.entity.player.EntityPlayer", "player")
                            .addParameter("net.minecraft.world", "World")
                            .addParameter("int", "x")
                            .addParameter("int", "y")
                            .addParameter("int", "z")
                            .build())
                        .emitStatement("return null")
                    .endMethod()
                    .beginMethod("Object", "getClientGuiElement", StandardModifiers.PUBLIC, new ParameterList()
                            .addParameter("int", "ID")
                            .addParameter("net.minecraft.entity.player.EntityPlayer", "player")
                            .addParameter("net.minecraft.world", "World")
                            .addParameter("int", "x")
                            .addParameter("int", "y")
                            .addParameter("int", "z")
                            .build())
                        .emitStatement("return null")
                    .endMethod()
                  .endType();

            code = sw.toString();
        } catch(Exception e){
            e.printStackTrace(System.err);
            code = "Error";
        }

        PsiFile newFile = factory.createFileFromText(s + ".java", JavaFileType.INSTANCE, code);
        PsiElement shortFile = JavaCodeStyleManager.getInstance(psiDirectory.getProject()).shortenClassReferences(
                newFile);
        PsiElement refFile = CodeStyleManager.getInstance(psiDirectory.getProject()).reformat(shortFile);
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
