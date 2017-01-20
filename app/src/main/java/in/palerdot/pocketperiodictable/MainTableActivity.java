package in.palerdot.pocketperiodictable;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MainTableActivity extends Activity {

    // define the intent payload message when ShowElementActivity is showed
    public final static String ELEMENT_MESSAGE = "in.palerdot.He";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide the notification bar (should be done before setting content)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_table);

        ElementXMLParser elementsXMLData = ElementXMLParser.getInstance();

        // remove the title bar in the main periodic table activity
        assert getActionBar() != null;
        getActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_table, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // necessary to finish the shared element transition in L
                    finishAfterTransition();
                    return true;
                }
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.action_help:
                // show the help page
                Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // show the element details
    public void showElement(View view) {
        // create a new intent for ShowElement Activity
        String tag_name = view.getTag().toString();
        //Toast.makeText(this, "Hello Universe! " + tag_name, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, ShowElementActivity.class);
        intent.putExtra(ELEMENT_MESSAGE, tag_name);
        // start the new Activity; add the second parameter to start with animation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            String transitionName = getString(R.string.elementTransitionName);
            // current clicked/touched view is shared with the activity
            View sharedView = view;

            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this, sharedView, transitionName);
            startActivity(intent, transitionActivityOptions.toBundle());

        } else {
            startActivity(intent);
        }

    }

}
