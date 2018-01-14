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
    f.setUndecorated(true)
    f.setSize(400, 150)
    f.setSize(400,600)
    f.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.5f))
    f.setTitle("release")
    f.setVisible(false)

    f.setResizable(false)
    f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    val frameDragListener = new Example.FrameDragListener(f)
    f.addMouseListener(frameDragListener)
    f.addMouseMotionListener(frameDragListener)


    val contentPane = f.getContentPane

    //Create the "cards".
    val card1 = new JPanel()
    val color = new Color(59, 63, 66)
    //card1.setBackground(color)
    val insets = card1.getInsets
    val b1 = new JButton("Button 3")
    val size = b1.getPreferredSize
    b1.setBounds(25 + insets.left, 5 + insets.top, size.width, size.height)
    card1.add(b1)

   // contentPane.add(card1)

    def location(size: Rectangle, displayPos: DisplayPos): Point = {
      val moveDown = displayPos.down
      val moveLeft = displayPos.left

      Some(java.awt.Toolkit.getDefaultToolkit.getScreenSize)
        .map(in ⇒ (in.height, in.width))
        .map(in ⇒ in.copy(_1 = in._1 - size.height))
        .map(in ⇒ in.copy(_2 = in._2 - size.width))
        .map(in ⇒ in.copy(_1 = (in._1 * moveDown).toInt, (in._2 * moveLeft).toInt))
        .map(in ⇒ {
          val x: Int = in._2
          val y: Int = in._1
          new Point(x, y)
        }).get
    }

    f.setLocation(location(f.getBounds, DisplayPos.tr))
    f.setVisible(true)
    f.setAlwaysOnTop(true)
    println("done")
  }

  def tray(dialog:JDialog): Unit = {
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

          dialog.setVisible(true)
        } else {
          dialog.setVisible(false)
        }
        println(icon.getSize)
        println(e.getPoint)
      }

    })
    systemTray.add(icon)

  }

  import java.awt.event.MouseAdapter

  class FrameDragListener(val frame: JDialog) extends MouseAdapter {
    private var mouseDownCompCoords:Point = null

    override def mouseReleased(e: MouseEvent): Unit = {
      mouseDownCompCoords = null
      if (frame.isUndecorated) {
        frame.dispose()
        frame.setBackground(new Color(1.0f, 1.0f, 1.0f, 1f))
        frame.setUndecorated(false)
        frame.setVisible(true)
      }
    }

    override def mousePressed(e: MouseEvent): Unit = {
      mouseDownCompCoords = e.getPoint
    }

    override def mouseDragged(e: MouseEvent): Unit = {
      val currCoords = e.getLocationOnScreen
      frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y)
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
