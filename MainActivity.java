package com.aimassistpro.sharpen;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.content.SharedPreferences;

public class MainActivity extends Activity {
    private final int bg = Color.rgb(10, 11, 16);
    private final int panel = Color.rgb(25, 27, 36);
    private final int red = Color.rgb(255, 55, 75);
    private LinearLayout root;
    private TextView status;
    private SharedPreferences prefs;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences("sharpen_profile", MODE_PRIVATE);
        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);

        ScrollView scroll = new ScrollView(this);
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(22, 28, 22, 24);
        root.setBackgroundColor(bg);
        scroll.addView(root);
        setContentView(scroll);

        TextView title = new TextView(this);
        title.setText("AIM ASSIST PRO");
        title.setTextColor(red);
        title.setTextSize(27);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        root.addView(title);

        TextView sub = new TextView(this);
        sub.setText("SHARPEN EDITION  •  ULTRA HD");
        sub.setTextColor(Color.LTGRAY);
        sub.setTextSize(12);
        sub.setPadding(0, 5, 0, 20);
        root.addView(sub);

        addSection("PERFIL DE IMAGEM");
        Button ultra = makeButton("ULTRA HD • ANDROID POTENTE");
        ultra.setOnClickListener(v -> {
            setValue("Nitidez", 90);
            setValue("Contraste", 68);
            setValue("Saturação", 58);
            setValue("Brilho", 52);
            status.setText("Perfil Ultra HD selecionado. Salve e use os valores como referência.");
        });
        root.addView(ultra);

        addSection("AJUSTES");
        addSlider("Nitidez", 0, 100, prefs.getInt("Nitidez", 90));
        addSlider("Contraste", 0, 100, prefs.getInt("Contraste", 68));
        addSlider("Saturação", 0, 100, prefs.getInt("Saturação", 58));
        addSlider("Brilho", 0, 100, prefs.getInt("Brilho", 52));

        Button save = makeButton("SALVAR PERFIL");
        save.setOnClickListener(v -> saveProfile());
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.topMargin = 18;
        root.addView(save, p);

        status = new TextView(this);
        status.setText("Perfil Ultra HD pronto para personalização.");
        status.setTextColor(Color.WHITE);
        status.setTextSize(12);
        status.setPadding(12, 16, 12, 12);
        root.addView(status);

        addSection("COMO USAR COM BLOOD STRIKE");
        TextView info = new TextView(this);
        info.setText("Este app guarda suas preferências e mostra ajustes sugeridos. Android comum não permite que um app altere diretamente a nitidez da imagem renderizada por outro jogo. Para melhorar a imagem no Blood Strike, ajuste também os gráficos e a resolução nas opções do próprio jogo e use os controles de tela do fabricante, se disponíveis.");
        info.setTextColor(Color.rgb(195, 198, 208));
        info.setTextSize(12);
        info.setPadding(12, 10, 12, 12);
        info.setBackgroundColor(panel);
        root.addView(info);

        TextView warning = new TextView(this);
        warning.setText("Não injeta código, não modifica arquivos do jogo, não aplica filtros sobre outros aplicativos e não aumenta FPS ou resolução por conta própria.");
        warning.setTextColor(Color.rgb(255, 170, 170));
        warning.setTextSize(11);
        warning.setPadding(4, 16, 4, 8);
        root.addView(warning);
    }

    private Button makeButton(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextColor(Color.WHITE);
        b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(red));
        return b;
    }

    private void addSection(String name) {
        TextView t = new TextView(this);
        t.setText(name);
        t.setTextColor(red);
        t.setTextSize(13);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setPadding(0, 18, 0, 9);
        root.addView(t);
    }

    private void addSlider(String name, int min, int max, int value) {
        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(10, 5, 10, 5);
        row.setBackgroundColor(panel);
        TextView label = new TextView(this);
        label.setText(name);
        label.setTextColor(Color.WHITE);
        label.setTextSize(13);
        label.setMinWidth(94);
        SeekBar bar = new SeekBar(this);
        bar.setMax(max - min);
        bar.setProgress(Math.max(0, Math.min(100, value)));
        bar.setProgressTintList(android.content.res.ColorStateList.valueOf(red));
        bar.setThumbTintList(android.content.res.ColorStateList.valueOf(red));
        row.addView(label, new LinearLayout.LayoutParams(94, -2));
        row.addView(bar, new LinearLayout.LayoutParams(0, -2, 1));
        row.setTag(name);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.bottomMargin = 7;
        root.addView(row, p);
    }

    private void setValue(String name, int value) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof LinearLayout && name.equals(v.getTag())) {
                LinearLayout row = (LinearLayout) v;
                ((SeekBar) row.getChildAt(1)).setProgress(value);
            }
        }
    }

    private void saveProfile() {
        SharedPreferences.Editor edit = prefs.edit();
        StringBuilder summary = new StringBuilder("Perfil salvo neste aparelho:\\n");
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof LinearLayout && v.getTag() instanceof String) {
                LinearLayout row = (LinearLayout) v;
                String name = (String) v.getTag();
                int value = ((SeekBar) row.getChildAt(1)).getProgress();
                edit.putInt(name, value);
                summary.append(name).append(": ").append(value).append("%\\n");
            }
        }
        edit.apply();
        status.setText(summary.toString());
    }
}
