package updater

import java.awt._
import java.awt.event.{MouseAdapter, MouseEvent, WindowEvent, WindowFocusListener}
import javax.swing._

object Example extends App {

  show()

  def show(): Unit = {
    System.setProperty("apple.awt.UIElement", "true")
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)


    import java.awt.GraphicsEnvironment
    import java.awt.Toolkit
    import java.awt.geom.Area
    val gd = GraphicsEnvironment.getLocalGraphicsEnvironment.getDefaultScreenDevice
    val bounds = gd.getDefaultConfiguration.getBounds
    val zzz = Toolkit.getDefaultToolkit.getScreenInsets(gd.getDefaultConfiguration)

    val safeBounds = new Rectangle(bounds)
    safeBounds.x += zzz.left
    safeBounds.y += zzz.top
    safeBounds.width -= (zzz.left + zzz.right)
    safeBounds.height -= (zzz.top + zzz.bottom)

    System.out.println("Bounds = " + bounds)
    System.out.println("SafeBounds = " + safeBounds)

    val area = new Area(bounds)
    area.subtract(new Area(safeBounds))
    System.out.println("Area = " + area.getBounds)

    val dialog = new JDialog()
    dialog.setBackground(new Color(1.0f, 1.0f, 1.0f))
    tray(dialog)
    dialog.addWindowFocusListener(new WindowFocusListener {
      override def windowLostFocus(e: WindowEvent): Unit = {
        println("lost")
      }

      override def windowGainedFocus(e: WindowEvent): Unit = {

      }
    })
    dialog.setSize(400, 600)
    dialog.setTitle("update-checker")
    dialog.setVisible(false)
    dialog.setResizable(false)
    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    val frameDragListener = new Example.FrameDragListener(dialog)
    dialog.addMouseListener(frameDragListener)
    dialog.addMouseMotionListener(frameDragListener)

    val card1 = new JPanel()
    val insets = card1.getInsets
    val b1 = new JButton("Button 3")
    val size = b1.getPreferredSize
    b1.setBounds(25 + insets.left, 5 + insets.top, size.width, size.height)
    card1.add(b1)

    setUndecorated(dialog)
    dialog.setVisible(false)
    dialog.setAlwaysOnTop(true)
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
    icon.addMouseListener(new MouseAdapter {

      override def mousePressed(e: MouseEvent): Unit = {
        if (!dialog.isVisible) {
          setUndecorated(dialog)
          println(icon.getSize)

          println(e.getPoint)
          if (e.getPoint.y < 100) {
            dialog.setLocation(location(dialog.getBounds, DisplayPos.tr))
          } else {
            dialog.setLocation(location(dialog.getBounds, DisplayPos.br))
          }

          dialog.setVisible(true)
        } else {
          dialog.setVisible(false)
        }

      }

    })
    systemTray.add(icon)
  }

  private def setUndecorated(dialog: JDialog): Unit = {
    dialog.removeNotify()
    dialog.setUndecorated(true)
    dialog.setOpacity(0.5f)
    dialog.addNotify()
  }

  private def setDecorated(dialog: JDialog): Unit = {
    if (dialog.isUndecorated) {
      dialog.removeNotify()
      dialog.setOpacity(1f)
      dialog.setUndecorated(false)
      dialog.addNotify()
      dialog.setVisible(true)
    }
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
      dialog.setOpacity(0.8f)
      dialog.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y)
    }
  }

  case class DisplayPos(name: String, down: Double, left: Double)

  object DisplayPos {
    val tr = DisplayPos("topRight", 0.09, 0.9)
    val tl = DisplayPos("topLeft", 0.1, 0.1)
    val br = DisplayPos("bottomRight", 0.9, 0.9)
    val bl = DisplayPos("bottomLeft", 0.9, 0.1)
  }

}
