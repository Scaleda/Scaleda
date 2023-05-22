package top.scaleda
package kernel

import tcl.parser.{TclBaseListener, TclLexer, TclParser}

import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.jdk.javaapi.CollectionConverters

class TclTester extends AnyFlatSpec with should.Matchers {
  behavior of "Tcl"

  val sampleText1 =
    """
      |set a 1
      |b c""".stripMargin

  val sampleText2 =
    """
      |set top top
      |set top_file /home/chiro/programs/scaleda-sample-project/src/top.v
      |
      |set top_sim_file src/top_tb.v
      |
      |set project_name $top
      |puts "Vivado project $project_name"
      |set work_dir /home/chiro/programs/scaleda-sample-project/.synth
      |
      |set device    xc7a100t
      |set package   fgg484
      |set speed     1
      |set part      $device$package-$speed
      |
      |set jobs 20
      |set project_dir [file normalize ./$project_name]
      |
      |""".stripMargin

  def parseTree(source: String) = {
    val lexer = new TclLexer(CharStreams.fromString(source))
    val parser = new TclParser(new CommonTokenStream(lexer))
    val listener = new TclBaseListener() {
      override def exitLine(ctx: TclParser.LineContext) = {
        val children = CollectionConverters.asScala(ctx.children)
        if (children != null) {
          val names = children.map(c => c.getText).toArray
          println(s"line names = ${names.mkString("Array(", ", ", ")")}")
        }
      }

      override def enterFunc_proc(ctx: TclParser.Func_procContext) = {
        println("enterFunc_proc")
      }

      override def exitFunc_internal(ctx: TclParser.Func_internalContext) = {
        println(s"exit func internal: ${ctx.getText}")
      }

      override def exitFunc_call(ctx: TclParser.Func_callContext) = {
        println(s"exit func call: ${ctx.func_name().getText} ${ctx.func_args().getText}")
      }
    }
    parser.addParseListener(listener)
    val tree = parser.inicio()
    tree
  }

  it should "test lexer" in {
    val lexer = new TclLexer(CharStreams.fromString(sampleText2))
    lexer.setText(sampleText2)
    println(s"lexer.getModeNames: ${lexer.getModeNames.mkString("Array(", ", ", ")")}")
    println(s"lexer.getRuleNames: ${lexer.getRuleNames.mkString("Array(", ", ", ")")}")
  }
  it should "test parser" in {
    val tree = parseTree(sampleText1)
  }
}
