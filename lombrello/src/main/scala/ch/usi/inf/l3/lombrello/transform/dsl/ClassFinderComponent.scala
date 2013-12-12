package ch.usi.inf.l3.lombrello.transform.dsl

import scala.tools.nsc.plugins.PluginComponent
import scala.tools.nsc.Phase

abstract class ClassFinderComponent(val plugin: TransformerPlugin) 
	extends PluginComponent {
  import plugin._
  
  val global: plugin.global.type = plugin.global
  val phaseName = "classFinder"

  import plugin.global._
  def newPhase(_prev: Phase) = new ClassFinderPluginComponent(_prev)

  class ClassFinderPluginComponent(prev: Phase) extends StdPhase(prev) {
    override def name = ClassFinderComponent.this.phaseName

    def apply(unit: CompilationUnit) {
      for (tree <- unit.body) {
        (tree.symbol != null && tree.symbol != NoSymbol) match {
          case true =>
            plugin.addClassTree(tree.symbol, tree)
          case _ =>
        }
      }
    }
  }
}