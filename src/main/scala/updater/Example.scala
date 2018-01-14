package updater

import java.awt._
import java.awt.event.{MouseAdapter, MouseEvent}
import javax.swing._

object Example extends App {

  show()

  def show(): Unit = {
    System.setProperty("apple.awt.UIElement", "true")
    //  System.setProperty("apple.laf.useScreenMenuBar", "true")
    //System.setProperty("com.apple.mrj.application.apple.menu.about.name", "WikiTeX")
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
    val f = new JDialog()
    tray(f)
    f.setSize(400, 600)
    f.setTitle("update-checker")
    f.setVisible(false)
    f.setResizable(false)
    f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    val frameDragListener = new Example.FrameDragListener(f)
    f.addMouseListener(frameDragListener)
    f.addMouseMotionListener(frameDragListener)

    val card1 = new JPanel()
    val insets = card1.getInsets
    val b1 = new JButton("Button 3")
    val size = b1.getPreferredSize
    b1.setBounds(25 + insets.left, 5 + insets.top, size.width, size.height)
    card1.add(b1)

    setUndecorated(f)
    f.setAlwaysOnTop(true)
    println("done")
  }

  private def location(size: Rectangle, displayPos: DisplayPos): Point = {
    val moveDown = displayPos.down
    val moveLeft = displayPos.left

    Some(java.awt.Toolkit.getDefaultToolkit.getScreenSize)
      .map(in ⇒ (in.width, in.height))
      .map { in ⇒ println(in); in }
      .map(in ⇒ in.copy(_1 = in._1 - size.width))
      .map(in ⇒ in.copy(_2 = in._2 - size.height))
      .map(in ⇒ in.copy(_1 = (in._1 * moveLeft).toInt, (in._2 * moveDown).toInt))
      .map(in ⇒ {
        val x: Int = in._1
        val y: Int = in._2
        new Point(x, y)
      }).get
  }

  def tray(dialog: JDialog): Unit = {
    import javax.swing.ImageIcon

    def loadImage(f: String): Image = {
      val url = getClass.getClassLoader.getResource(f)
      new ImageIcon(url).getImage
    }

    val systemTray = java.awt.SystemTray.getSystemTray
    val image = loadImage("foo.png")

    val icon = new TrayIcon(image)
    val menu = new PopupMenu()
    icon.setPopupMenu(menu)
    icon.addMouseListener(new MouseAdapter {

      override def mousePressed(e: MouseEvent): Unit = {
        if (!dialog.isVisible) {
          setUndecorated(dialog)
        } else {
          dialog.setVisible(false)
        }
        println(icon.getSize)
        println(e.getPoint)
      }

    })
    systemTray.add(icon)
  }

  private def setUndecorated(dialog: JDialog): Unit = {
    dialog.dispose()
    dialog.setUndecorated(true)
    dialog.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.5f))
    dialog.setLocation(location(dialog.getBounds, DisplayPos.tr))
    dialog.setVisible(true)
  }

  private def setDecorated(dialog: JDialog): Unit = {
    dialog.dispose()
    dialog.setBackground(new Color(1.0f, 1.0f, 1.0f, 1f))
    dialog.setUndecorated(false)
    dialog.setVisible(true)
  }

  import java.awt.event.MouseAdapter

  class FrameDragListener(val dialog: JDialog) extends MouseAdapter {
    private var mouseDownCompCoords: Point = null

    override def mouseReleased(e: MouseEvent): Unit = {
      mouseDownCompCoords = null
      setDecorated(dialog)
    }

    override def mousePressed(e: MouseEvent): Unit = {
      mouseDownCompCoords = e.getPoint
    }

    override def mouseDragged(e: MouseEvent): Unit = {
      val currCoords = e.getLocationOnScreen
      dialog.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y)
    }
  }

  case class DisplayPos(name: String, down: Double, left: Double)

  object DisplayPos {
    val tr = DisplayPos("topRight", 0.1, 0.9)
    val tl = DisplayPos("topLeft", 0.1, 0.1)
    val br = DisplayPos("bottomRight", 0.9, 0.9)
    val bl = DisplayPos("bottomLeft", 0.9, 0.1)
  }

}
