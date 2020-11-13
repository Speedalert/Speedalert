package pwc.appdev.speedalert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {

    Button register, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);
        register = findViewById(R.id.registernewuser);
        login = findViewById(R.id.loginbutton);

        register.setOnClickListener(v -> startActivity(new Intent(login.this, Signup.class)));

    }
}



