package jaypaw.pm.easywatermark;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import jaypaw.pm.easywatermark.utility.Utility;


public class AboutActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        StringBuilder sb = new StringBuilder();
        sb.append("Current Version - ").append(Utility.prepareVersionString(BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
        TextView version = (TextView) findViewById(R.id.version);
        version.setText(sb.toString());


        TextView email = (TextView) findViewById(R.id.email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri data = Uri.parse("mailto:jaypawsoft@gmail.com?subject=&body=");
                Intent intent = new Intent(Intent.ACTION_SENDTO, data);

                intent.putExtra(Intent.EXTRA_EMAIL, "jaypawsoft@gmail.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Query");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });


        ImageButton shareMobileMapper = (ImageButton) findViewById(R.id.shareMobileMapper);
        shareMobileMapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //shareMobileMapperWithOthers();
                Utility.shareAppWithOthers(AboutActivity.this);
            }
        });
    }
}
