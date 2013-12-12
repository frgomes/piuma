
package ch.usi.inf.l3.seal

import ch.usi.inf.l3.lombrello.transform.dsl._
import ch.usi.inf.l3.lombrello.util._

class ScalaSealPlugin(override val global: TGlobal) extends TransformerPlugin(global) {

  val name: String = "test"
  override val description: String = """A compiler plugin!""";

  val pluginComponents: List[TransformerPluginComponent] = List(new ScalaSealTyper(this))
}
