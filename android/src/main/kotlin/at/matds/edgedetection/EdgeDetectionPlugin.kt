package at.matds.edgedetection

import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.PluginRegistry.Registrar

class EdgeDetectionPlugin(private val registrar: Registrar, private val delegate: EdgeDetectionDelegate) : MethodCallHandler {
  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "edge_detection")

      val delegate = EdgeDetectionDelegate(registrar.activity())

      registrar.addActivityResultListener(delegate)

      channel.setMethodCallHandler(EdgeDetectionPlugin(registrar, delegate))
    }
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    when {
        registrar.activity() == null -> {
          result.error("no_activity", "edge_detection plugin requires a foreground activity.", null)
          return
        }
        call.method == "edge_detect" -> delegate.openCameraActivity(call, result)
        call.method == "getPlatformVersion" -> result.success("Android ${android.os.Build.VERSION.RELEASE}")
        else -> result.notImplemented()
    }
  }
}
