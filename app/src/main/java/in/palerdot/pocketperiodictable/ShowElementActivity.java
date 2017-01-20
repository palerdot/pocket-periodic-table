package in.palerdot.pocketperiodictable;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ShowElementActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide the notification bar (should be done before setting content)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // first set the ShowElement Layout (then manipulate things there)
        setContentView(R.layout.activity_show_element);

        // this activity is invoked as an intent; get it
        Intent intent = getIntent();
        // get the element details
        String elem_details = intent.getStringExtra(MainTableActivity.ELEMENT_MESSAGE);

        // enable the up home button
        assert getActionBar() != null;
        getActionBar().setDisplayHomeAsUpEnabled(true);

        ElementXMLParser elementsDataParser = ElementXMLParser.getInstance();
        // display the Element info
        displayElementInfo(elementsDataParser, elem_details);

    }

    private void displayElementInfo(ElementXMLParser elementParser, String element) {

        // display the parsed xml details
        Map<String, ArrayList<HashMap<String, String>>> elementDetails = elementParser.getElementsData().get(element);
        // loop through element details and display appropriate sections
        for (Map.Entry entry : elementDetails.entrySet()) {
            String section = entry.getKey().toString();
            switch (section) {
                case "general":
                    displayGeneralInfo( (ArrayList) entry.getValue() );
                    break;
                case "history":
                    displayHistoryInfo( (ArrayList) entry.getValue() );
                    break;
                case "physical-properties":
                    displayPhysicalPropertiesInfo((ArrayList) entry.getValue());
                    break;
                case "atomic-properties":
                    displayAtomicPropertiesInfo((ArrayList) entry.getValue());
                    break;
                case "miscellaneous":
                    displayMiscellaneousInfo((ArrayList) entry.getValue());
                    break;
                case "meta":
                    displayMetaInfo( (ArrayList) entry.getValue(), element );
                    break;
                case "links":
                    displayLinks( (ArrayList) entry.getValue() );
                    break;
            }
        }

    }

    // ---------------------------------------------------------------------------------------------------------
    // START: display functions

    // display the links
    private void displayLinks( ArrayList info ) {

        LinearLayout holder = (LinearLayout) findViewById(R.id.info_links);
        // clear the view contents
        holder.removeAllViews();

        // loop through the array list
        for (int i = 0; i < info.size(); i++) {
            HashMap<String, String> property = (HashMap<String, String>) info.get(i);

            // get property name and value
            String propertyKey = new String();
            String propertyValue = new String();
            for (Map.Entry<String, String> e : property.entrySet()) {
                propertyKey = e.getKey();
                propertyValue = e.getValue();
            }

            // create a linear layout for this property
            LinearLayout linkDiv = new LinearLayout(this);
            linkDiv.setPadding(0, 3, 0, 3);
            // key view
            TextView linkView = new TextView(this);
            // linkView.setTextSize(12);
            linkView.setTextSize(getResources().getDimensionPixelSize(R.dimen.element_info_links));
            linkView.setTypeface(Typeface.DEFAULT_BOLD);
            linkView.setMovementMethod(LinkMovementMethod.getInstance());
            linkView.setText( Html.fromHtml(makeHyperLinks(formatPropertyName(propertyKey), propertyValue)) );

            // add link to the link div
            linkDiv.addView(linkView);

            // add the link div to holder
            holder.addView(linkDiv);

        }

    }

    // display the general info
    private void displayGeneralInfo( ArrayList info ) {

        LinearLayout holder = (LinearLayout) findViewById(R.id.general_properties);
        // clear the view contents
        holder.removeAllViews();


        // loop through the array list
        for (int i = 0; i < info.size(); i++) {
            HashMap<String, String> property = (HashMap<String, String>) info.get(i);

            // get property name and value
            String propertyKey = new String();
            String propertyValue = new String();
            for (Map.Entry<String, String> e : property.entrySet()) {
                propertyKey = e.getKey();
                propertyValue = e.getValue();
            }

            // create a linear layout for this property
            LinearLayout propertyDiv = new LinearLayout(this);
            propertyDiv.setPadding(0, 5, 0, 5);
            // key view
            TextView keyView = new TextView(this);
            keyView.setTypeface(Typeface.DEFAULT_BOLD); // make it bold
            keyView.setText( formatPropertyName(propertyKey) + ": ");

            TextView valueView = new TextView(this);
            valueView.setText( Html.fromHtml(propertyValue) );
            valueView.setPadding(0, 5, 0, 0);

            // set the font size
            keyView.setTextSize(getResources().getDimension(R.dimen.element_info_property_name_size));
            valueView.setTextSize(getResources().getDimension(R.dimen.element_info_property_value_size));

            // add the text views
            propertyDiv.addView(keyView);
            propertyDiv.addView(valueView);

            // add the holder view
            holder.addView(propertyDiv);

        }

    }

    // display history info
    private void displayHistoryInfo(ArrayList info) {

        LinearLayout holder = (LinearLayout) findViewById(R.id.history_properties);
        // clear the view contents
        holder.removeAllViews();


        // loop through the array list
        for (int i = 0; i < info.size(); i++) {
            HashMap<String, String> property = (HashMap<String, String>) info.get(i);

            // get property name and value
            String propertyKey = new String();
            String propertyValue = new String();
            for (Map.Entry<String, String> e : property.entrySet()) {
                propertyKey = e.getKey();
                propertyValue = e.getValue();
            }

            // create a linear layout for this property
            LinearLayout propertyDiv = new LinearLayout(this);
            propertyDiv.setPadding(0, 5, 0, 5);
            // key view
            TextView keyView = new TextView(this);
            keyView.setTypeface(Typeface.DEFAULT_BOLD); // make it bold
            keyView.setText(formatPropertyName(propertyKey) + ": ");

            TextView valueView = new TextView(this);
            valueView.setText(propertyValue);
            valueView.setPadding(0, 5, 0, 0);

            // set the font size
            keyView.setTextSize(getResources().getDimension(R.dimen.element_info_property_name_size));
            valueView.setTextSize(getResources().getDimension(R.dimen.element_info_property_value_size));

            // add the text views
            propertyDiv.addView(keyView);
            propertyDiv.addView(valueView);

            // add the holder view
            holder.addView(propertyDiv);

        }

    }

    // display physical properties info
    private void displayPhysicalPropertiesInfo(ArrayList info) {

        LinearLayout holder = (LinearLayout) findViewById(R.id.physical_properties);
        // clear the view contents
        holder.removeAllViews();


        // loop through the array list
        for (int i = 0; i < info.size(); i++) {
            HashMap<String, String> property = (HashMap<String, String>) info.get(i);

            // get property name and value
            String propertyKey = new String();
            String propertyValue = new String();
            for (Map.Entry<String, String> e : property.entrySet()) {
                propertyKey = e.getKey();
                propertyValue = e.getValue();
            }

            // create a linear layout for this property
            LinearLayout propertyDiv = new LinearLayout(this);
            propertyDiv.setPadding(0, 5, 0, 5);
            // key view
            TextView keyView = new TextView(this);
            keyView.setTypeface(Typeface.DEFAULT_BOLD); // make it bold
            keyView.setText(formatPropertyName(propertyKey) + ": ");

            TextView valueView = new TextView(this);
            valueView.setText( Html.fromHtml(propertyValue) );
            valueView.setPadding(0, 5, 0, 0);

            // set the font size
            keyView.setTextSize(getResources().getDimension(R.dimen.element_info_property_name_size));
            valueView.setTextSize(getResources().getDimension(R.dimen.element_info_property_value_size));

            // add the text views
            propertyDiv.addView(keyView);
            propertyDiv.addView(valueView);

            // add the holder view
            holder.addView(propertyDiv);

        }

    }

    // display Atomic properties info
    private void displayAtomicPropertiesInfo(ArrayList info) {

        LinearLayout holder = (LinearLayout) findViewById(R.id.atomic_properties);
        // clear the view contents
        holder.removeAllViews();


        // loop through the array list
        for (int i = 0; i < info.size(); i++) {
            HashMap<String, String> property = (HashMap<String, String>) info.get(i);

            // get property name and value
            String propertyKey = new String();
            String propertyValue = new String();
            for (Map.Entry<String, String> e : property.entrySet()) {
                propertyKey = e.getKey();
                propertyValue = e.getValue();
            }

            // create a linear layout for this property
            LinearLayout propertyDiv = new LinearLayout(this);
            propertyDiv.setPadding(0, 5, 0, 5);
            // key view
            TextView keyView = new TextView(this);
            keyView.setTypeface(Typeface.DEFAULT_BOLD); // make it bold
            keyView.setText(formatPropertyName(propertyKey) + ": ");

            TextView valueView = new TextView(this);
            valueView.setText( Html.fromHtml(propertyValue) );
            valueView.setPadding(0, 5, 0, 0);

            // set the font size
            keyView.setTextSize(getResources().getDimension(R.dimen.element_info_property_name_size));
            valueView.setTextSize(getResources().getDimension(R.dimen.element_info_property_value_size));

            // add the text views
            propertyDiv.addView(keyView);
            propertyDiv.addView(valueView);

            // add the holder view
            holder.addView(propertyDiv);

        }

    }

    // display miscellaneous info
    private void displayMiscellaneousInfo(ArrayList info) {

        LinearLayout holder = (LinearLayout) findViewById(R.id.miscellaneous_properties);
        // clear the view contents
        holder.removeAllViews();


        // loop through the array list
        for (int i = 0; i < info.size(); i++) {
            HashMap<String, String> property = (HashMap<String, String>) info.get(i);

            // get property name and value
            String propertyKey = new String();
            String propertyValue = new String();
            for (Map.Entry<String, String> e : property.entrySet()) {
                propertyKey = e.getKey();
                propertyValue = e.getValue();
            }

            // create a linear layout for this property
            LinearLayout propertyDiv = new LinearLayout(this);
            propertyDiv.setPadding(0, 5, 0, 5);
            // key view
            TextView keyView = new TextView(this);
            keyView.setTypeface(Typeface.DEFAULT_BOLD); // make it bold
            keyView.setText(formatPropertyName(propertyKey) + ": ");

            TextView valueView = new TextView(this);
            valueView.setText( Html.fromHtml(propertyValue) );
            valueView.setPadding(0, 5, 0, 0);

            // set the font size
            keyView.setTextSize(getResources().getDimension(R.dimen.element_info_property_name_size));
            valueView.setTextSize(getResources().getDimension(R.dimen.element_info_property_value_size));

            // add the text views
            propertyDiv.addView(keyView);
            propertyDiv.addView(valueView);

            // add the holder view
            holder.addView(propertyDiv);

        }

    }

    private void displayMetaInfo (ArrayList info, String symbol) {

        // update the element symbol
        TextView tv = (TextView) findViewById(R.id.element_symbol);
        tv.setText(symbol);
        // holds the group period and block constructed string
        String group_period_block_info = new String();
        String bg_color = new String();

        for (int i = 0; i < info.size(); i++) {
            HashMap<String, String> property = (HashMap<String, String>) info.get(i);

            // get property name and value
            String propertyKey = new String();
            String propertyValue = new String();
            for (Map.Entry<String, String> e : property.entrySet()) {
                propertyKey = e.getKey();
                propertyValue = e.getValue();
            }

            switch (propertyKey) {

                case "classification":
                    bg_color = getColorFromClassification(propertyValue);
                    // set the subtitle
                    assert getActionBar() != null;
                    getActionBar().setSubtitle(propertyValue);
                    updateBGColors( bg_color );
                    break;

                case "name":
                    // set the activity title
                    assert getActionBar() != null;
                    getActionBar().setTitle(propertyValue);
                    break;

                case "atomic-number":
                    tv = (TextView) findViewById(R.id.info_atomic_number);
                    tv.setText(propertyValue);
                    break;

                case "atomic-weight":
                    tv = (TextView) findViewById(R.id.info_atomic_weight);
                    tv.setText(propertyValue);
                    break;

                case "electron-distribution":
                    tv = (TextView) findViewById(R.id.info_electron_distribution);
                    tv.setText(propertyValue);
                    break;

                case "group":
                case "block":
                case "period":
                    group_period_block_info += formatPropertyName(propertyKey) + ": " + propertyValue + " ";
                    break;

            }

        }

        tv = (TextView) findViewById(R.id.info_group_period_block);
        tv.setText(group_period_block_info);

    }

    // END: display functions
    // ---------------------------------------------------------------------------------------------------------

    // START: Utility Functions

    // title cases the property names
    private String formatPropertyName(String name) {
        String[] splitted = name.split("-");
        String[] exclude_list = new String[] { "and", "as", "by", "a", "of", "de", "for" };
        for (int i = 0; i < splitted.length; i++) {
            if (!Arrays.asList(exclude_list).contains(splitted[i])) {
                // transform only if it is not in the exclude list
                splitted[i] = splitted[i].substring(0, 1).toUpperCase() + splitted[i].substring(1).toLowerCase();
            }
        }
        // sniff exceptional cases and return correct values
        if (name.equals("cas-registry-number")) {
            return "CAS Registry Number";
        }
        // default return value
        return TextUtils.join(" ", splitted);
    }

    // makes hyperlinks from the url
    private String makeHyperLinks(String text, String url) {
        return "<a href='" + url + "'>" + text + "</a>";
    }

    // returns appropriate color from the element's classification type
    private String getColorFromClassification(String classification) {
        switch (classification) {
            case "Diatomic NonMetal":
            case "Polyatomic NonMetal":
                return "#"+Integer.toHexString( getResources().getColor(R.color.color_other_nonmetals) );
            case "Metalloid":
                return "#"+Integer.toHexString(getResources().getColor(R.color.color_metalloids) );
            case "Halogen":
                return "#"+Integer.toHexString(getResources().getColor(R.color.color_halogens) );
            case "Noble Gas":
                return "#"+Integer.toHexString(getResources().getColor(R.color.color_noble_gases) );
            case "Alkali Metal":
                return "#"+Integer.toHexString(getResources().getColor(R.color.color_alkali_metals) );
            case "Alkaline Earth Metal":
                return "#"+Integer.toHexString(getResources().getColor(R.color.color_alkaline_earth_metals) );
            case "Lanthanide":
                return "#"+Integer.toHexString(getResources().getColor(R.color.color_lanthanoids) );
            case "Actinide":
                return "#"+Integer.toHexString(getResources().getColor(R.color.color_actinoids) );
            case "Transition Metal":
                return "#"+Integer.toHexString(getResources().getColor(R.color.color_transition_metals) );
            case "Post-transition Metal":
                return "#"+Integer.toHexString(getResources().getColor(R.color.color_post_transition_metals) );
        }
        // should not come to this point
        return "#00ff00";
    }

    private void updateBGColors(String bg_color) {
        // set Action Bar color
        assert getActionBar() != null;
        getActionBar().setBackgroundDrawable( new ColorDrawable( Color.parseColor(bg_color) ) );

        // update the main info window bg
        LinearLayout ll = (LinearLayout) findViewById(R.id.info_window);
        ll.setBackgroundColor(Color.parseColor(bg_color));

        // update the general header
        TextView tv = (TextView) findViewById(R.id.general_header);
        tv.setBackgroundColor( Color.parseColor(bg_color) );
        // update the history header
        tv = (TextView) findViewById(R.id.history_header);
        tv.setBackgroundColor( Color.parseColor(bg_color) );
        // update the physical properties header
        tv = (TextView) findViewById(R.id.physical_properties_header);
        tv.setBackgroundColor( Color.parseColor(bg_color) );
        // update the atomic properties header
        tv = (TextView) findViewById(R.id.atomic_properties_header);
        tv.setBackgroundColor( Color.parseColor(bg_color) );
        // update the miscellaneous  header
        tv = (TextView) findViewById(R.id.miscellaneous_header);
        tv.setBackgroundColor( Color.parseColor(bg_color) );

    }

    // END: Utility Functions

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_element, menu);
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
}
