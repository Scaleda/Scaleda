package top.scaleda
package idea.settings.auth

import idea.ScaledaBundle
import idea.utils.MainLogger
import kernel.net.user.{ScaledaAuthorizationProvider, UserTokenBean}

import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.ui.components.JBList
import com.intellij.ui.{ColoredListCellRenderer, SimpleTextAttributes, ToolbarDecorator}

import javax.swing._
import scala.jdk.javaapi.CollectionConverters

class AuthorizationEditor extends SearchableConfigurable {
  override def getId: String = AuthorizationEditor.SETTINGS_ID

  override def getDisplayName: String = ScaledaBundle.message("settings.auth.name")

  private var original: Seq[UserTokenBean]               = Seq()
  private val dataModel: DefaultListModel[UserTokenBean] = new DefaultListModel[UserTokenBean]()
  private val dataList: JBList[UserTokenBean]            = new JBList[UserTokenBean](dataModel)

  private var selected: Option[UserTokenBean] = None

  dataList.addListSelectionListener(e => {
    if (dataModel.size() > e.getFirstIndex)
      selected = Some(dataModel.get(e.getFirstIndex))
  })

  private def reload(): Unit = {
    dataModel.clear()
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
      append(" - ")
      append(value.username)
    }
  })

  private var canApply = true

  override def isModified: Boolean = canApply && !dataModel.toArray.sameElements(original.toArray[UserTokenBean])

  private def cloneModel = dataModel.toArray.toIndexedSeq.map(_.asInstanceOf[UserTokenBean])

  private def hostnames = cloneModel.map(_.host)

  override def apply(): Unit = {
    if (canApply) {
      val data = cloneModel
      MainLogger.info("apply data model", data)
      ScaledaAuthorizationProvider.saveTokenPairs(data)
      reload()
    } else MainLogger.warn("Cannot apply data now!")
  }

  override def createComponent(): JComponent = ToolbarDecorator
    .createDecorator(dataList)
    .setAddAction(e => {
      val v = new LoginDialog(save = false, ignoredHosts = hostnames)
      v.pack()
      // center display
      v.setLocationRelativeTo(null)
      canApply = false
      // stuck here
      v.setVisible(true)
      v.getUserToken.foreach(dataModel.addElement)
      v.setUserToken(None)
      canApply = true
    })
    .setEditAction(e => {
      selected.foreach(selected => {
        val dataClonedOld = cloneModel
        val selectedIndex = dataClonedOld.indexOf(selected)
        val v =
          new LoginDialog(save = false, data = Some(selected), ignoredHosts = hostnames.filter(_ != selected.host))
        v.pack()
        // center display
        v.setLocationRelativeTo(null)
        canApply = false
        // stuck here
        v.setVisible(true)
        v.getUserToken.foreach(t => {
          MainLogger.info("modifying selected", selected, "=>", t)
          dataModel.setElementAt(t, selectedIndex)
        })
        canApply = true
      })
    })
    .setRemoveAction(e => {
      selected.foreach(t => {
        val copy  = cloneModel
        val index = copy.indexWhere(_.host == t.host)
        if (index >= 0) {
          MainLogger.info("removing selected", t, "at", index)
          dataModel.removeElementAt(index)
        }
        selected = None
      })
    })
    .disableUpDownActions()
    .createPanel()
}

object AuthorizationEditor {
  val SETTINGS_ID = "top.scaleda.idea.settings.auth.AuthorizationEditor"
}
