package com.madebydragons.elocalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Vector;

public class TournamentActivity extends AppCompatActivity {

    private int insertionRow = 0;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tournament, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.tournament:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);
        EditText yourtext = (EditText)findViewById(R.id.first_match);
        yourtext.addTextChangedListener(new RowAwareTextEditWatcher((TableRow)findViewById(R.id.first_row)) {
            @Override
            public void afterTextChanged(Editable s) {

                Vector<Integer> elos = new Vector<Integer>();
                //TODO: stop being lazy and change to an enum.
                ArrayList<Double> results = new ArrayList<Double>();
                TableLayout tl = (TableLayout) findViewById(R.id.tournament_table);
                for(int i=3;i < tl.getChildCount()-1;i++){
                    assert(tl.getChildAt(i) instanceof TableRow );
                    TableRow tr = (TableRow) tl.getChildAt(i);
                    assert(tr.getChildAt(0) instanceof EditText);
                    elos.add(new Integer(((EditText)tr.getChildAt(0)).getText().toString()).intValue());
                    results.add(new Double(((EditText) tr.getChildAt(0)).getText().toString()).doubleValue()/2);
                }
                int finalScore = 0;
                try {
                    EloCalculator.tournament(
                            new Integer(((EditText) findViewById(R.id.your_rating)).getText().toString()).intValue(), elos.toArray(new Integer[elos.size()]), results.toArray(new Double[elos.size()]));
                } catch (InvalidTournamentData invalidTournamentData) {
                    invalidTournamentData.printStackTrace();
                }

                ((EditText) findViewById(R.id.your_rating)).setText(new Integer(finalScore).toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        TableLayout tl = (TableLayout) findViewById(R.id.tournament_table);
        // The last row (child of table) is the add button, therefore when a row is added is immediately before it
        insertionRow = tl.getChildCount()-1;
    }

    public int findRowIndex(TableLayout tl, TableRow tr){
        int result = -1;
        for(int i=0; result==-1 && i < tl.getChildCount();i++)
            if(tl.getChildAt(i)==tr)
                result = i;
        return result;
    }

    public void deleteRow(View view)
    {
        ImageButton b = null;
        if(view instanceof ImageButton){
            b = (ImageButton) view;
            if(b.getParent() instanceof TableRow){
                TableRow tr = (TableRow)b.getParent();
                TableLayout tl = (TableLayout) findViewById(R.id.tournament_table);
                if(findRowIndex(tl,tr)!=-1)
                    tl.removeViewAt(findRowIndex(tl,tr));
                insertionRow--;
            }
        }
    }

    public void insertRow(View view)
    {
        TableLayout tl = (TableLayout) findViewById(R.id.tournament_table);
        TableRow tr =  new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        populateWithNewContents(tr);
        tl.addView(tr,insertionRow,new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        //TODO: fine tune enable button for last match standing
        insertionRow++;

    }


    public void populateWithNewContents(TableRow tr){
        EditText opponent_score = new EditText(this);
        opponent_score.setEnabled(true);
        opponent_score.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.addView(opponent_score);

        ImageView loose = new ImageView(this);
        loose.setImageResource(R.drawable.ic_loose_48dp);
        loose.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.addView(loose);

        SeekBar sb = new SeekBar(this);
        sb.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        sb.setMinimumWidth(80);
        sb.setProgress(1);
        sb.setIndeterminate(false);
        sb.setMax(2);
        sb.setBottom(0);
        //android:layout_height="wrap_content"
            //TODO?  android:layout_gravity="bottom"
      //  android:layout_marginLeft="0dp"
      //  android:layout_marginRight="0dp"

        tr.addView(sb);

        ImageView win = new ImageView(this);
        win.setImageResource(R.drawable.ic_win_48dp);
        win.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.addView(win);

        ImageButton b = new ImageButton(this);
        b.setImageResource(android.R.drawable.ic_input_delete);
        b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        b.setBackgroundColor(android.R.color.transparent);
        tr.addView(b);

    }
}
