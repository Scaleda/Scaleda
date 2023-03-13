package top.criwits.scaleda
package idea.settings.auth

import idea.ScaledaBundle
import kernel.net.user.{ScaledaAuthorizationProvider, UserTokenBean}

import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.ui.components.JBList
import com.intellij.ui.{ColoredListCellRenderer, SimpleTextAttributes, ToolbarDecorator}

import javax.swing._
import scala.jdk.javaapi.CollectionConverters

class AuthorizationEditor extends SearchableConfigurable {
  override def getId: String = "top.criwits.scaleda.idea.settings.auth.AuthorizationEditor"

  override def getDisplayName: String = ScaledaBundle.message("settings.auth.name")

  private var original: Seq[UserTokenBean]               = Seq()
  private val dataModel: DefaultListModel[UserTokenBean] = new DefaultListModel[UserTokenBean]()
  private val dataList: JBList[UserTokenBean]            = new JBList[UserTokenBean](dataModel)

  private def reload(): Unit = {
    original = ScaledaAuthorizationProvider.loadTokenPair.filter(!UserTokenBean.TEST_MODE_PAIR.equals(_))
    dataModel.addAll(CollectionConverters.asJava(original))
  }
  reload()

  dataList.setCellRenderer(new ColoredListCellRenderer[UserTokenBean] {
    override def customizeCellRenderer(
        list: JList[_ <: UserTokenBean],
        value: UserTokenBean,
        index: Int,
        selected: Boolean,
        hasFocus: Boolean
    ) = {
      append(value.host, SimpleTextAttributes.LINK_ATTRIBUTES)
      append(value.username)
    }
  })

  override def isModified: Boolean = !dataModel.toArray.sameElements(original.toArray[UserTokenBean])

  override def apply(): Unit = {
    ScaledaAuthorizationProvider.saveTokenPairs(dataModel.toArray.toIndexedSeq.map(_.asInstanceOf[UserTokenBean]))
  }

  override def createComponent(): JComponent = ToolbarDecorator
    .createDecorator(dataList)
    .setAddAction(e => {
      val v = new LoginDialog()
      v.pack()
      // center display
      v.setLocationRelativeTo(null)
      // stuck here
      v.setVisible(true)
      // refresh
      reload()
    })
    .setRemoveAction(e => {})
    .disableUpDownActions()
    .createPanel()
}
