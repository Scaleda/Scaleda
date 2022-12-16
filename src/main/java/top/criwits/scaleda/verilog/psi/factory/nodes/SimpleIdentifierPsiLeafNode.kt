package top.criwits.scaleda.verilog.psi.factory.nodes

import top.criwits.scaleda.verilog.psi.factory.VerilogPsiLeafNodeFactory
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode
import top.criwits.scaleda.verilog.parser.VerilogLexer

class SimpleIdentifierPsiLeafNode(text: CharSequence) :
    ANTLRPsiLeafNode(VerilogPsiLeafNodeFactory.getTokenIElementType(VerilogLexer.SIMPLE_IDENTIFIER), text) {

    override fun getName(): String? {
        return this.text
    }

}
