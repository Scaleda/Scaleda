package top.scaleda
package bluespec.psi.nodes.module

import bluespec.psi.nodes.SimpleIdentifierOwnerPsiNode

import com.intellij.lang.ASTNode
import org.jetbrains.annotations.NotNull

class ModuleIdentifierPsiNode(@NotNull node: ASTNode) extends SimpleIdentifierOwnerPsiNode(node)
