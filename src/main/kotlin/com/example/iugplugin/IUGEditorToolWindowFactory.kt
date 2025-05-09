package com.example.demo2

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class IUGEditorToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val iugEditorPanel = IUGEditorPanel()
        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(iugEditorPanel, "IUG Editor", false)
        toolWindow.contentManager.addContent(content)
    }
}


