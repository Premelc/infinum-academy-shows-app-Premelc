import android.app.Activity
import android.view.View
import com.premelc.shows_dominik_premelc.shows.ShowDetailsActivity.Companion.buildShowDetailsActivityIntent

class CustomOnClickHandler(var id: String, var username: String) : View.OnClickListener {
    override fun onClick(v: View?) {
        if (v != null) {
            ClickHandler(v.context as Activity)
        }
    }

    private fun ClickHandler(context: Activity) {
        val intent = buildShowDetailsActivityIntent(context)
        intent.putExtra("id", id)
        intent.putExtra("username", username)
        context.startActivity(intent)
    }
}