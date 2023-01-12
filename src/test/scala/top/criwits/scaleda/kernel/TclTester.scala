package top.criwits.scaleda
package kernel

import tcl.parser.{TclBaseListener, TclLexer, TclListener, TclParser}

import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import top.criwits.scaleda.tcl.TclLogger

import scala.jdk.javaapi.CollectionConverters

class TclTester extends AnyFlatSpec with should.Matchers {
  behavior of "Tcl"

  val sampleText1 =
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

  it should "test lexer" in {
    val lexer = new TclLexer(CharStreams.fromString(sampleText1))
    lexer.setText(sampleText1)
    println(s"lexer.getModeNames: ${lexer.getModeNames.mkString("Array(", ", ", ")")}")
    println(s"lexer.getRuleNames: ${lexer.getRuleNames.mkString("Array(", ", ", ")")}")
  }
  it should "test parser" in {
    val lexer = new TclLexer(CharStreams.fromString(sampleText1))
    val parser = new TclParser(new CommonTokenStream(lexer))
    val listener = new TclBaseListener() {
      override def enterLine(ctx: TclParser.LineContext) = {
        val children = CollectionConverters.asScala(ctx.children)
        if (children != null) {
          val names = children.map(c => c.getText).toArray
          println(s"names = ${names.mkString("Array(", ", ", ")")}")
        }
      }
    }
    parser.addParseListener(listener)
    val tree = parser.inicio()
  }
}
