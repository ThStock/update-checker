package updater

import java.awt.event.MouseAdapter
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.effect.DropShadow
import javafx.scene.input.{KeyCode, KeyEvent, MouseButton, MouseEvent}
import javafx.scene.paint.Color
import javafx.scene.{Node, Parent, Scene}
import javafx.stage.{Stage, StageStyle}
import javax.swing.{JDialog, UIManager}


object U extends App {
  System.setProperty("apple.awt.UIElement", "true")
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
  val dialog = new JDialog()
  dialog.setVisible(true)
  dialog.setVisible(false)
  ExampleFx.run()

}
object ExampleFx {

  def run(): Unit = {
    Application.launch(classOf[ExampleFx])
  }

}

class ExampleFx extends Application {
  private var initialX: Double = 50
  private var initialY: Double = 50

  override def start(primaryStage: Stage) = {

    Tryer.apply(new MouseAdapter {})

    primaryStage.initStyle(StageStyle.TRANSPARENT)
    primaryStage.setTitle("Hello World!")
    primaryStage.setX(initialX)
    primaryStage.setY(initialY)

    val root: Parent = loadResource("test.fxml")
    root.setEffect(new DropShadow())
    applyDraggableTo(root)

    val scene = new Scene(root)
    scene.setOnKeyPressed((event: KeyEvent) => {
      if (event.getCode == KeyCode.ESCAPE) {
        System.exit(0)
      }
    })
    primaryStage.setAlwaysOnTop(true)
    scene.setFill(Color.TRANSPARENT)
    primaryStage.setScene(scene)
    primaryStage.show()
  }

  def loadResource[R](resource: String): R = FXMLLoader.load(getClass.getClassLoader.getResource(resource))

  private def applyDraggableTo(node: Node): Unit = {
    node.setOnMousePressed((me: MouseEvent) => {
      if (me.getButton ne MouseButton.MIDDLE) {
        initialX = me.getSceneX
        initialY = me.getSceneY
      }
    })
    node.setOnMouseDragged((me: MouseEvent) => {
      if (me.getButton ne MouseButton.MIDDLE) {
        node.getScene.getWindow.setX(me.getScreenX - initialX)
        node.getScene.getWindow.setY(me.getScreenY - initialY)
      }
    })
  }
}
