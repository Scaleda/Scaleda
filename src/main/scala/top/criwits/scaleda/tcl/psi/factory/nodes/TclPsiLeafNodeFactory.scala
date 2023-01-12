package top.criwits.scaleda
package tcl.psi.factory.nodes

import tcl.TclLanguage
import tcl.parser.TclLexer

import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode

import java.util

object TclPsiLeafNodeFactory {
  private val tokenIElementTypeClassMap =
    new util.HashMap[IElementType, Class[_ <: ANTLRPsiLeafNode]]

  def getTokenIElementType(tokenIndex: Int) = PSIElementTypeFactory
    .getTokenIElementTypes(TclLanguage)
    .get(tokenIndex)

  def create(`type`: IElementType, text: CharSequence): ANTLRPsiLeafNode = {
    if (tokenIElementTypeClassMap.containsKey(`type`))
      try return tokenIElementTypeClassMap
        .get(`type`)
        .getConstructor(classOf[CharSequence])
        .newInstance(text)
      catch {
        case ignored: ReflectiveOperationException =>

      }
    new ANTLRPsiLeafNode(`type`, text)
  }

  def create(tokenIndex: Int, charSequence: CharSequence): ANTLRPsiLeafNode =
    create(getTokenIElementType(tokenIndex), charSequence)

  tokenIElementTypeClassMap.put(
    getTokenIElementType(TclLexer.IDENTIFICADOR),
    classOf[IdentifierPsiLeafNode]
  )
}
