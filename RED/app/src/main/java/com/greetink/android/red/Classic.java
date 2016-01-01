package com.greetink.android.red;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Classic extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classic);
    }

    public void toPlainText(View view){
        TextView textView = (TextView) findViewById(R.id.result_text);
        EditText editText = (EditText) findViewById(R.id.plain_cipher_text);
        editText.setText(textView.getText().toString());
    }

    public void toKeyText(View view){
        TextView textView = (TextView) findViewById(R.id.result_text);
        EditText editText = (EditText) findViewById(R.id.key_text);
        RadioButton radioButton = (RadioButton) findViewById(R.id.caesar_cipher_radio_button);
        if(!radioButton.isChecked()){
            editText.setText(textView.getText().toString());
        }
    }

    public void radioButtonCaesarCipher(View view){
        EditText editText = (EditText) findViewById(R.id.key_text);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setText("");
        editText.setHint("Enter Number");
    }

    public void radioButtonVigenere(View view){
        EditText editText = (EditText) findViewById(R.id.key_text);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setText("");
        editText.setHint("Enter Key");
    }

    public void encryptButton(View view){
        EditText editTextPlainText = (EditText) findViewById(R.id.plain_cipher_text);
        EditText editTextKeyText = (EditText) findViewById(R.id.key_text);
        TextView textView = (TextView) findViewById(R.id.result_text);
        Kriptografi kriptografi = new Kriptografi();
        RadioButton radioButton = (RadioButton) findViewById(R.id.caesar_cipher_radio_button);
        if(radioButton.isChecked()){
            try{
                textView.setText(kriptografi.encryptCaesarCipher(kriptografi.toCryptText(editTextPlainText.getText().toString()), Integer.parseInt(editTextKeyText.getText().toString())));
            }catch(NumberFormatException e){
                textView.setText(kriptografi.encryptCaesarCipher(kriptografi.toCryptText(editTextPlainText.getText().toString()), 0));
            }

        }else{
            textView.setText(kriptografi.encryptVigenere(kriptografi.toCryptText(editTextPlainText.getText().toString()), kriptografi.toCryptText(editTextKeyText.getText().toString())));
        }
    }

    public void decryptButton(View view){
        EditText editTextPlainText = (EditText) findViewById(R.id.plain_cipher_text);
        EditText editTextKeyText = (EditText) findViewById(R.id.key_text);
        TextView textView = (TextView) findViewById(R.id.result_text);
        Kriptografi kriptografi = new Kriptografi();
        RadioButton radioButton = (RadioButton) findViewById(R.id.caesar_cipher_radio_button);
        if(radioButton.isChecked()){
            try{
                textView.setText(kriptografi.decryptCaesarCipher(kriptografi.toCryptText(editTextPlainText.getText().toString()), Integer.parseInt(editTextKeyText.getText().toString())));
            }catch(NumberFormatException e){
                textView.setText(kriptografi.decryptCaesarCipher(kriptografi.toCryptText(editTextPlainText.getText().toString()), 0));
            }
        }else{
            textView.setText(kriptografi.decryptVigenere(kriptografi.toCryptText(editTextPlainText.getText().toString()), kriptografi.toCryptText(editTextKeyText.getText().toString())));
        }
    }
}
