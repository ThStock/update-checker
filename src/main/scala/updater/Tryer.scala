package updater

import java.awt.{Image, TrayIcon}
import java.awt.event.MouseAdapter

import javax.swing.ImageIcon

object Tryer {

  def apply(mouseAdapter: MouseAdapter): Unit = {

    def loadImage(f: String): Image = {
      val url = getClass.getClassLoader.getResource(f)
      new ImageIcon(url).getImage
    }

    val image = loadImage("foo.png")

    val icon = new TrayIcon(image)
    icon.addMouseListener(mouseAdapter)

    val systemTray = java.awt.SystemTray.getSystemTray
    systemTray.add(icon)
  }
}
