package scala.js.gen.js.dom

import scala.js.exp.dom.NodeListOpsExp
import scala.js.gen.js.GenEffect
import scala.js.gen.QuoteGen

trait GenNodeListOps extends GenEffect with QuoteGen {
  val IR: NodeListOpsExp
  import IR._

  override def emitNode(sym: Sym[Any], rhs: Def[Any]) = rhs match {
    case NodeListSize(ns) =>
      emitValDef(sym, quote(ns) + ".length")
    case NodeListFilter(ns, n, block) =>
      val i,l = fresh[Int]
      emitValDef(sym, "[]")
      stream.println("for (var "+ quote(i) +" = 0, "+ quote(l) +" = "+ quote(ns) +".length ; "+ quote(i) +" < "+ quote(l) +" ; "+ quote(i) +"++) {")
      emitValDef(n, quote(ns) +".item("+ quote(i) +")")
      emitBlock(block)
      stream.println("if ("+ quote(getBlockResult(block)) +") "+ quote(sym) +".push("+ quote(n) +");")
      stream.println("}")
    case NodeListForeach(ns, n, block) =>
      val i, l = fresh[Int]
      stream.println("for (var "+ quote(i) +" = 0, "+ quote(l) +" = " + quote(ns) + ".length ; "+ quote(i) +" < "+ quote(l) +" ; "+ quote(i) +"++) {")
      emitValDef(n, quote(ns)+".item("+ quote(i) +")")
      emitBlock(block)
      stream.println("}")
    case NodeListMap(ns, n, block) =>
      val i, l = fresh[Int]
      emitValDef(sym, "[]")
      stream.println(q"for (var $i = 0, $l = $ns.length ; $i < $l ; $i++) {")
      emitValDef(n, q"$ns.item($i)")
      emitBlock(block)
      stream.println(q"$sym.push(${getBlockResult(block)});")
      stream.println("}")
    case NodeListForeachWithIndex(ns, a, i, block) =>
      val l = fresh[Int]
      stream.println(s"for (var ${quote(i)} = 0, ${quote(l)} = ${quote(ns)}.length ; ${quote(i)} < ${quote(l)} ; ${quote(i)}++) {")
      emitValDef(a, s"${quote(ns)}.item(${quote(i)})")
      emitBlock(block)
      stream.println("}")
    case _ => super.emitNode(sym, rhs)
  }
}