package top.criwits.scaleda.verilog.psi.factory.nodes

import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode
import top.criwits.scaleda.verilog.parser.VerilogLexer
import top.criwits.scaleda.verilog.psi.factory.VerilogPsiLeafNodeFactory

class SimpleIdentifierPsiLeafNode(text: CharSequence) :
    ANTLRPsiLeafNode(VerilogPsiLeafNodeFactory.getTokenIElementType(VerilogLexer.SIMPLE_IDENTIFIER), text) {

    override fun getName(): String {
        return this.text
    }

}
