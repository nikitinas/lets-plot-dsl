package org.jetbrains.kotlin.letsPlot.jupyter

import jetbrains.datalore.plot.PlotHtmlHelper
import jetbrains.letsPlot.GGBunch
import jetbrains.letsPlot.LetsPlot
import jetbrains.letsPlot.frontend.NotebookFrontendContext
import jetbrains.letsPlot.intern.Plot
import org.jetbrains.kotlin.letsPlot.PlotSpec
import org.jetbrains.kotlinx.jupyter.api.HTML
import org.jetbrains.kotlinx.jupyter.api.Notebook
import org.jetbrains.kotlinx.jupyter.api.libraries.JupyterIntegration

class Integration : JupyterIntegration() {
    override fun Builder.onLoaded(notebook: Notebook<*>?) {
        import("jetbrains.letsPlot.*",
            "jetbrains.letsPlot.geom.*",
            "jetbrains.letsPlot.stat.*",
            "jetbrains.datalore.plot.*",
            "jetbrains.letsPlot.intern.*",
            "jetbrains.letsPlot.scale.*",
            "org.jetbrains.kotlin.letsPlot.*")

        val api = "1.2.0"
        val isolatedFrame = ""
        val js = "1.5.6"
        val isolatedFrameParam = if(isolatedFrame.isNotEmpty()) isolatedFrame.toBoolean() else null
        var frontendContext: NotebookFrontendContext? = null

        onLoaded {
            LetsPlot.apiVersion = api
            frontendContext = LetsPlot.setupNotebook(js, isolatedFrameParam) { display(HTML(it)) }
            display(HTML(frontendContext!!.getConfigureHtml()))
        }

        render<Plot> {
            HTML(frontendContext!!.getHtml(it))
        }
        render<GGBunch> {
            HTML(frontendContext!!.getHtml(it))
        }
        render<PlotSpec> {
            HTML(PlotHtmlHelper.getDynamicDisplayHtmlForRawSpec(it.spec))
        }
    }
}