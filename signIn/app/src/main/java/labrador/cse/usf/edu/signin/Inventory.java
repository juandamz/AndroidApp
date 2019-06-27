package labrador.cse.usf.edu.signin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Inventory extends AppCompatActivity {

    Button scan_btn;
    Button low_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        scan_btn = findViewById(R.id.scanner);
        low_btn = findViewById(R.id.low);

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Scanner.class));
            }


        });

        low_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LowInventory.class));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_search:openSearch();
                return true;
            case R.id.action_settings:openSettings();
                return true;

            case R.id.action_logout:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;
            default:return super.onOptionsItemSelected(item);
        }



    }



    public void openSearch() {}
    public void openSettings() {

    }

}
