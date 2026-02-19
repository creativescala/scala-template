package objective

import cats.effect.ExitCode
import cats.effect.IO
import cats.syntax.all.*
import decline_derive.*

@Name("objective")
enum ObjectiveCommand derives CommandApplication:
  case Inspect
  case Reload

object Client extends CommandApplication.Main[ObjectiveCommand]:
  // val inspectCommand: Command[ObjectiveCommand] =
  //   Command(name = "inspect", header = "Inspect current tasks")(Opts(ObjectiveCommand.Inspect))

  // val reloadCommand: Command[ObjectiveCommand] =
  //   Command(name = "reload", header = "Reload tasks")(Opts(ObjectiveCommand.Reload))

  override def run(args: ObjectiveCommand) =
    args match
      case ObjectiveCommand.Inspect => println("Inspecting")
      case ObjectiveCommand.Reload  => println("Reloading")
