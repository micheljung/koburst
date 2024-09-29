import io.koburst.core.ScenarioDsl.rampUsers
import io.koburst.core.ScenarioDsl.scenario
import kotlin.time.Duration.Companion.seconds

object Main {
  @JvmStatic
  fun main(args: Array<String>) {
    scenario("My first scenario") {
      rampUsers(50) {
        during = 10.seconds
      }
    }
  }
}
