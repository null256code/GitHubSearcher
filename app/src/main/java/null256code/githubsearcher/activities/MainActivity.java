package null256code.githubsearcher.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import null256code.githubsearcher.R;
import null256code.githubsearcher.network.SearchRepositoryAsyncTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchRepositoryAsyncTask task = new SearchRepositoryAsyncTask(MainActivity.this, "test");
                task.execute(0);
            }
        });
    }
}
