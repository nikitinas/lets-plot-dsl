package org.jetbrains.kotlin.letsPlot.jupyter

import jetbrains.letsPlot.GGBunch
import jetbrains.letsPlot.LetsPlot
import jetbrains.letsPlot.frontend.NotebookFrontendContext
import jetbrains.letsPlot.intern.Plot
import org.jetbrains.kotlin.letsPlot.PlotSpec
import org.jetbrains.kotlinx.jupyter.api.HTML
import org.jetbrains.kotlinx.jupyter.api.annotations.JupyterLibrary
import org.jetbrains.kotlinx.jupyter.api.libraries.JupyterIntegration

@JupyterLibrary
class Integration : JupyterIntegration() {
    override fun Builder.onLoaded() {
        import(
            "jetbrains.letsPlot.*",
            "jetbrains.letsPlot.geom.*",
            "jetbrains.letsPlot.stat.*",
            "jetbrains.datalore.plot.*",
            "jetbrains.letsPlot.intern.*",
            "jetbrains.letsPlot.scale.*",
            "org.jetbrains.kotlin.letsPlot.*",
        )

        val properties = System.getProperties()
        val apiVersion = properties.getProperty("letsplot.api.version", "2.0.1")
        val jsVersion = properties.getProperty("letsplot.js.version", "2.0.2")

        val isolatedFrame = null
        var frontendContext: NotebookFrontendContext? = null

        onLoaded {
            LetsPlot.apiVersion = apiVersion
            frontendContext = LetsPlot.setupNotebook(jsVersion, isolatedFrame) { display(HTML(it)) }
            display(HTML(frontendContext!!.getConfigureHtml()))
        }

        render<Plot> {
            HTML(frontendContext!!.getHtml(it))
        }
        render<GGBunch> {
            HTML(frontendContext!!.getHtml(it))
        }
        render<PlotSpec> {
            HTML(frontendContext!!.getDisplayHtml(it.spec))
        }
    }
}