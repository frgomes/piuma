package com.googlecode.avro
package plugin

// import scala.tools.nsc._

import ch.usi.inf.l3.piuma.neve.NeveDSL._
import scala.reflect.internal.Flags._
import org.apache.avro.Schema



@checker("unionclosure") class UnionClosure {
  rightAfter("uniondiscover")
  plugin ScalaAvroPlugin

  private var unit: CompilationUnit = _
  private def check(_unit: CompilationUnit) = {
    unit = _unit
    debug("Union for comp unit: " + unit)
    debug(retrieveUnions(unit))
    newTraverser().traverse(unit.body)
  }   

  private def newTraverser(): Traverser = new ForeachTreeTraverser(check1)

  private def check1(tree: Tree): Unit = tree match {
    case cd @ ClassDef(_, _, _, _) if (cd.symbol.tpe.parents.contains(avroRecordTrait.tpe)) =>
      debug("Running union closure on class: " + cd.symbol.fullName)
      val unitUnions = retrieveUnions(unit)
      unitUnions.
        filter(unionSym => {
          debug("Comparing cd.symbol.tpe " + cd.symbol.tpe + " to " + unionSym.tpe)
          cd.symbol.tpe <:< unionSym.tpe }).
        foreach(unionSym => addUnionRecord(unionSym, cd.symbol))
    case cd @ ClassDef(mods,_,_,_) =>
      debug("Skipped class: " + cd.symbol.fullName)
    case _ => ()
  }
}
