package com.greetink.android.red;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Modern extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modern);
        //Mirza
        result = (TextView) findViewById(R.id.result_text);
        save = (Button) findViewById(R.id.save_file);
        buttonSearch = (Button) findViewById(R.id.search_file);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(CUSTOM_DIALOG_ID);
            }
        });

        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        curFolder = root;
        //
    }

    public void toPlainText(View view) {
        TextView textView = (TextView) findViewById(R.id.result_text);
        EditText editText = (EditText) findViewById(R.id.plain_cipher_text);
        editText.setText(textView.getText().toString());
    }

    public void encryptButton(View view) throws UnsupportedEncodingException {
        EditText editTextPlainText = (EditText) findViewById(R.id.plain_cipher_text);
        EditText editTextKeyText = (EditText) findViewById(R.id.key_text);
        EditText editTextBlockText = (EditText) findViewById(R.id.block_text);
        EditText editTextShiftText = (EditText) findViewById(R.id.shift_text);
        Kriptografi kriptografi = new Kriptografi();
        TextView textView = (TextView) findViewById(R.id.result_text);
        String plainTextValue = editTextPlainText.getText().toString();
        String keyTextValue = editTextKeyText.getText().toString();
        int blockValue;
        int shiftValue;
        try {
            blockValue = Integer.parseInt(editTextBlockText.getText().toString());
        } catch (NumberFormatException e) {
            blockValue = 0;
        }
        try {
            shiftValue = Integer.parseInt(editTextShiftText.getText().toString());
        } catch (NumberFormatException e) {
            shiftValue = 0;
        }
        textView.setText(kriptografi.cbcEncryption(plainTextValue, keyTextValue, blockValue, shiftValue));
    }

    public void decryptButton(View view) throws UnsupportedEncodingException {
        EditText editTextPlainText = (EditText) findViewById(R.id.plain_cipher_text);
        EditText editTextKeyText = (EditText) findViewById(R.id.key_text);
        EditText editTextBlockText = (EditText) findViewById(R.id.block_text);
        EditText editTextShiftText = (EditText) findViewById(R.id.shift_text);
        Kriptografi kriptografi = new Kriptografi();
        TextView textView = (TextView) findViewById(R.id.result_text);
        String plainTextValue = editTextPlainText.getText().toString();
        String keyTextValue = editTextKeyText.getText().toString();
        int blockValue;
        int shiftValue;
        try {
            blockValue = Integer.parseInt(editTextBlockText.getText().toString());
        } catch (NumberFormatException e) {
            blockValue = 0;
        }
        try {
            shiftValue = Integer.parseInt(editTextShiftText.getText().toString());
        } catch (NumberFormatException e) {
            shiftValue = 0;
        }
        textView.setText(kriptografi.cbcDecryption(plainTextValue, keyTextValue, blockValue, shiftValue));
    }

    //Mirza
    Button buttonSearch;
    Button buttonBack;
    TextView textFolder;
    TextView result;
    public Button save;

    String KEY_TEXTPSS = "TEXTPSS";
    static final int CUSTOM_DIALOG_ID = 0;
    ListView dialog_ListView;

    File root;
    File curFolder;

    private List<String> fileList = new ArrayList<String>();
    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Kripto";


    //Method

    @Override
    protected Dialog onCreateDialog(int id) {

        Dialog dialog = null;

        switch (id) {
            case CUSTOM_DIALOG_ID:
                dialog = new Dialog(Modern.this);
                dialog.setContentView(R.layout.dialoglayout);
                dialog.setTitle("File Explorer");
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                //final EditText result = (EditText) findViewById(R.id.result);
                textFolder = (TextView) dialog.findViewById(R.id.folder);
                buttonBack = (Button) dialog.findViewById(R.id.up);
                buttonBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListDir(curFolder.getParentFile());
                    }
                });

                dialog_ListView = (ListView) dialog.findViewById(R.id.dialoglist);
                dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String SelectedItem = textFolder.getText().toString();
                        File selected = new File(fileList.get(position));

                        if(selected.isDirectory()) {
                            ListDir(selected);
                        } else {
                            Toast.makeText(Modern.this, selected.toString() + " selected",
                                    Toast.LENGTH_LONG).show();
                            dismissDialog(CUSTOM_DIALOG_ID);

                        }
                        String path = selected.toString();
                        EditText editPlainText = (EditText) findViewById(R.id.plain_cipher_text);
                        String finalText = readFromFile(path);
                        String textBuffer = "";
                        for(int l=0;l<finalText.length()-1;l++){
                             textBuffer += finalText.charAt(l);
                        }
                        editPlainText.setText(textBuffer);
                    }
                });

                break;
        }
        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case CUSTOM_DIALOG_ID:
                ListDir(curFolder);
                break;
        }
    }

    void ListDir(File f) {
        if(f.equals(root)) {
            buttonBack.setEnabled(false);
        } else {
            buttonBack.setEnabled(true);
        }

        curFolder = f;
        textFolder.setText(f.getPath());

        File[] files = f.listFiles();
        fileList.clear();

        for(File file : files) {
            fileList.add(file.getPath());
        }

        ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, fileList);
        dialog_ListView.setAdapter(directoryList);
    }

    public void saveFile(View view)
    {
        File dir = new File(path);
        dir.mkdirs();
        File file = new File (path + "/result.txt");
        String [] saveText = String.valueOf(result.getText()).split(System.getProperty("line.separator"));

        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();

        Save(file, saveText);
    }
    public static void Save(File file, String[] data)
    {
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        try
        {
            try
            {
                for (int i = 0; i<data.length; i++)
                {
                    fos.write(data[i].getBytes());
                    if (i < data.length-1)
                    {
                        fos.write("\n".getBytes());
                    }
                }
            }
            catch (IOException e) {e.printStackTrace();}
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (IOException e) {e.printStackTrace();}
        }
    }
    public String readFromFile(String path) {

        //Get the text file
        File file = new File(path);
        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //This is the text from the file
        return text.toString();
    }
    //
    //
}
