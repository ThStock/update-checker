package updater

import javafx.application.Application
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.input.{MouseButton, MouseEvent}
import javafx.scene.paint.Color
import javafx.scene.{Node, Parent, Scene}
import javafx.stage.{Stage, StageStyle}

object ExampleFx {

  def main(args: Array[String]): Unit = {
    System.setProperty("apple.awt.UIElement", "true")
    Application.launch(classOf[ExampleFx])
  }

}

class ExampleFx extends Application {
  private var initialX: Double = 30
  private var initialY: Double = 30

  override def start(primaryStage: Stage) = {

    primaryStage.initStyle(StageStyle.TRANSPARENT)
    primaryStage.setTitle("Hello World!")
    primaryStage.setX(initialX)
    primaryStage.setY(initialY)

    val root: Parent = FXMLLoader.load(getClass.getClassLoader.getResource("test.fxml"))
    addDraggableNode(root)
    val scene = new Scene(root)
    primaryStage.setAlwaysOnTop(true)
    scene.setFill(Color.TRANSPARENT)
    primaryStage.setScene(scene)
    primaryStage.show()
  }

  private def addDraggableNode(node: Node): Unit = {
    node.setOnMousePressed(new EventHandler[MouseEvent]() {
      override def handle(me: MouseEvent): Unit = {
        if (me.getButton ne MouseButton.MIDDLE) {
          initialX = me.getSceneX
          initialY = me.getSceneY
        }
      }
    })
    node.setOnMouseDragged(new EventHandler[MouseEvent]() {
      override def handle(me: MouseEvent): Unit = {
        if (me.getButton ne MouseButton.MIDDLE) {
          node.getScene.getWindow.setX(me.getScreenX - initialX)
          node.getScene.getWindow.setY(me.getScreenY - initialY)
        }
      }
    })
  }
}
