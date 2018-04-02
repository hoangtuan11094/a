package com.hoangtuan.translatechinesehwriting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.hoangtuan.translatechinesehwriting.Adapter.GoiYAdapter;
import com.hoangtuan.translatechinesehwriting.Model.GoiYModel;

import com.myscript.atk.scw.SingleCharWidgetApi;
import com.myscript.atk.text.CandidateInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity implements
        SingleCharWidgetApi.OnConfiguredListener,
        SingleCharWidgetApi.OnTextChangedListener,
        SingleCharWidgetApi.OnSingleTapGestureListener,
        SingleCharWidgetApi.OnLongPressGestureListener,
        SingleCharWidgetApi.OnBackspaceGestureListener,
        SingleCharWidgetApi.OnReturnGestureListener,
        CustomEdittext.OnSelectionChanged {


    private Toolbar tBar;
    private MaterialMenuDrawable materialMenu;
    private ImageView imgCopy;
    private CustomEdittext mTextField;
    private SingleCharWidgetApi mWidget;
    private ArrayList<GoiYModel> goiYModels;
    private GoiYAdapter goiYAdapter;
    private RecyclerView recyGoiY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgCopy = (ImageView) findViewById(R.id.imgCopy);
        tBar = (Toolbar) findViewById(R.id.tBar);
        materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.REGULAR);
        materialMenu.setIconState(MaterialMenuDrawable.IconState.BURGER);
        tBar.setNavigationIcon(materialMenu);
        tBar.setTitleTextColor(Color.WHITE);
        tBar.setTitle("");
        tBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mWidget = (SingleCharWidgetApi) findViewById(R.id.singChar);

        if (!mWidget.registerCertificate(Khoa.getBytes())) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Cần thêm chứng chỉ");
            dlgAlert.setTitle("Thiếu chứng chỉ");
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dlgAlert.create().show();
            return;
        }
        mTextField = (CustomEdittext) findViewById(R.id.edtText);


        goiYModels = new ArrayList<>();
        goiYAdapter = new GoiYAdapter(this);

        recyGoiY = (RecyclerView) findViewById(R.id.recyGoiY);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyGoiY.setHasFixedSize(true);
        recyGoiY.setLayoutManager(manager);
        recyGoiY.setAdapter(goiYAdapter);

        imgCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClipboard(getApplicationContext(), mTextField.getText().toString().trim());
                Toast.makeText(MainActivity.this, "Đã copy", Toast.LENGTH_SHORT).show();
            }
        });

        mWidget.setOnConfiguredListener(this);
        mWidget.setOnTextChangedListener(this);
        mWidget.setOnBackspaceGestureListener(this);
        mWidget.setOnReturnGestureListener(this);
        mWidget.setOnSingleTapGestureListener(this);
        mWidget.setOnLongPressGestureListener(this);

        mWidget.addSearchDir("zip://" + getPackageCodePath() + "!/assets/conf");
        mWidget.configure("zh_CN", "si_text");

        recyGoiY.addOnItemTouchListener(new RecyclerItemClickListener(this, recyGoiY, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                GoiYModel tag = goiYModels.get(position);
                mWidget.replaceCharacters(tag.getStart(), tag.getEnd(), tag.getText());
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWidget.dispose();
    }

    private void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    //Wi callback
    @Override
    public void onSelectionChanged(EditText editText, int selStart, int selEnd) {
        mWidget.setInsertIndex(selStart);
        updateCandidatePanel();
    }

    @Override
    public void onBackspaceGesture(SingleCharWidgetApi singleCharWidgetApi, int i, int i1) {

        CandidateInfo info = mWidget.getCharacterCandidates(i - 1);
        mWidget.replaceCharacters(info.getStart(), info.getEnd(), null);
    }

    @Override
    public void onConfigured(SingleCharWidgetApi singleCharWidgetApi, boolean b) {

    }

    @Override
    public boolean onLongPressGesture(SingleCharWidgetApi singleCharWidgetApi, float v, float v1) {
        return false;
    }

    @Override
    public void onReturnGesture(SingleCharWidgetApi singleCharWidgetApi, int i) {
        mWidget.replaceCharacters(i, i, "\n");
    }

    @Override
    public boolean onSingleTapGesture(SingleCharWidgetApi singleCharWidgetApi, float v, float v1) {
        return false;
    }

    @Override
    public void onTextChanged(SingleCharWidgetApi singleCharWidgetApi, String s, boolean b) {
        mTextField.setOnSelectionChangedListener(null);
        mTextField.setTextKeepState(s);
        mTextField.setSelection(mWidget.getInsertIndex());
        mTextField.setOnSelectionChangedListener(this);
        updateCandidatePanel();

    }

    public void onDelete(View view) {
        CandidateInfo info = mWidget.getCharacterCandidates(mWidget.getInsertIndex() - 1);
        mWidget.replaceCharacters(info.getStart(), info.getEnd(), null);

    }

    public void onSpace(View view) {
        mWidget.insertString(" ");
    }

    public void onClear(View view) {
        mWidget.clear();

    }

    public void onFind(View view) {
        String text = mTextField.getText().toString().trim();
        if (!text.equals("") && text != null) {
            Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
            intent.putExtra("text", mTextField.getText().toString());

            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

    }

    // update candidates bar


    // update candidates panel
    private void updateCandidatePanel() {

        goiYModels.clear();
        int index = mWidget.getInsertIndex() - 1;
        if (index < 0) {
            index = 0;
        }

        CandidateInfo[] infos = {
                // add word-level candidates
                mWidget.getWordCandidates(index),
                // add character-level candidates
                mWidget.getCharacterCandidates(index),
        };

        for (CandidateInfo info : infos) {
            int start = info.getStart();
            int end = info.getEnd();
            List<String> labels = info.getLabels();
            List<String> completions = info.getCompletions();

            for (int i = 0; i < labels.size(); i++) {
                GoiYModel tag = new GoiYModel();
                tag.setStart(start);
                tag.setEnd(end);
                tag.setText(labels.get(i) + completions.get(i));
                goiYModels.add(tag);

            }
            goiYAdapter.setCandidates(goiYModels);

        }
    }
}
