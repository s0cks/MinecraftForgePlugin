package io.github.s0cks.mcfip.actions;

import com.intellij.openapi.actionSystem.DefaultActionGroup;

public final class MCFActionGroup
extends DefaultActionGroup{
    public MCFActionGroup(){
        super("Minecraft Forge", true);
        this.getTemplatePresentation().setDescription("Forge Templates");
        this.getTemplatePresentation().setVisible(true);
    }
}