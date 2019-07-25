package com.example.imtpmd;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private EditText Name;

    private EditText Password;
    private TextView Info;
    private Button Login;
    private int counter = 5;
    EditText mEditText;
    private String FILE_NAME = "example.txt";
    ArrayList<String> arrai = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEditText = findViewById(R.id.edit_text);
        arrai.add("nig");
        arrai.add("ger");
        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPassword);
        Info = (TextView)findViewById(R.id.tvInfo);
        Login = (Button)findViewById(R.id.btnLogin);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(),Password.getText().toString());
            }
        });
        modus("donker");


    }
    private void validate(String userName, String userPassword) {
            if ((userName.equals("")) && (userPassword.equals(""))) {

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                counter--;
                Info.setText("No of attempts remaining " + String.valueOf(counter));
                if (counter <= 0)
                    Login.setEnabled(false);
            }
        }

    private void modus(String mode){
        if(mode == "donker"){
            this.getWindow().getDecorView().setBackgroundResource(R.color.colorPrimaryDark);
            Info.setTextColor(Color.WHITE);
        }
        else{
            this.getWindow().getDecorView().setBackgroundResource(R.color.colorPrimary);
        }
    }

    public void save(View v){
       //String text= mEditText.getText().toString();
   /*     String text = arrai.toString();

        FileOutputStream fos = null;
        try{
            fos = openFileOutput(FILE_NAME,MODE_PRIVATE);
            fos.write(text.getBytes());
            mEditText.getText().clear();
            Toast.makeText(this,"saved to "+getFilesDir()+FILE_NAME,Toast.LENGTH_LONG).show();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(fos!=null){
                try{
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        } */
    }
 public void load(View v){
        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            mEditText.setText(sb.toString());

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            try{
                fis.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}
