package com.example.sql_lite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etMatricula, etNombreCompleto, etMateria, etCarrera, etBuscarMatricula, etParcial1, etParcial2, etParcial3, etPromedio;
    private Button btnGuardar, btnBuscar, btnGuardarCalificaciones;
    private baseDatos ayudabd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar los elementos de la interfaz
        etMatricula = findViewById(R.id.etMatricula);
        etNombreCompleto = findViewById(R.id.etNombre);
        etMateria = findViewById(R.id.etMateria);
        etCarrera = findViewById(R.id.etCarrera);
        etBuscarMatricula = findViewById(R.id.etBuscarMatricula);
        etParcial1 = findViewById(R.id.etParcial1);
        etParcial2 = findViewById(R.id.etParcial2);
        etParcial3 = findViewById(R.id.etParcial3);
        etPromedio = findViewById(R.id.etPromedio);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnGuardarCalificaciones = findViewById(R.id.btnGuardarCalificaciones);

        // Crear una instancia de la base de datos
        ayudabd = new baseDatos(getApplicationContext());

        // Habilitar o deshabilitar los EditText de parciales dinámicamente
        etParcial1.setEnabled(false);
        etParcial2.setEnabled(false);
        etParcial3.setEnabled(false);

        etParcial1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                etParcial2.setEnabled(s.length() > 0);
            }
        });

        etParcial2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                etParcial3.setEnabled(s.length() > 0);
            }
        });

        // Guardar un nuevo alumno
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = ayudabd.getWritableDatabase();
                ContentValues valores = new ContentValues();
                valores.put(baseDatos.Datostabla.COLUMNA_ID, etMatricula.getText().toString());
                valores.put(baseDatos.Datostabla.COLUMNA_NAME, etNombreCompleto.getText().toString());
                valores.put(baseDatos.Datostabla.COLUMNA_MATERIA, etMateria.getText().toString());
                valores.put(baseDatos.Datostabla.COLUMNA_CARRERA, etCarrera.getText().toString());

                long idGuardado = db.insert(baseDatos.Datostabla.NOMBRE_TABLE, null, valores);
                if (idGuardado != -1) {
                    Toast.makeText(getApplicationContext(), "Alumno guardado con ID: " + idGuardado, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error al guardar el alumno", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Buscar un alumno por matrícula
// Buscar un alumno por matrícula
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = ayudabd.getReadableDatabase();
                String[] proyeccion = {
                        baseDatos.Datostabla.COLUMNA_NAME,
                        baseDatos.Datostabla.COLUMNA_MATERIA,
                        baseDatos.Datostabla.COLUMNA_CARRERA,
                        baseDatos.Datostabla.COLUMNA_PARCIAL1,
                        baseDatos.Datostabla.COLUMNA_PARCIAL2,
                        baseDatos.Datostabla.COLUMNA_PARCIAL3
                };
                String seleccion = baseDatos.Datostabla.COLUMNA_ID + " = ?";
                String[] seleccionArgs = { etBuscarMatricula.getText().toString() };

                Cursor c = db.query(
                        baseDatos.Datostabla.NOMBRE_TABLE,
                        proyeccion,
                        seleccion,
                        seleccionArgs,
                        null,
                        null,
                        null
                );

                if (c != null && c.moveToFirst()) {
                    etNombreCompleto.setText(c.getString(0));
                    etMateria.setText(c.getString(1));
                    etCarrera.setText(c.getString(2));

                    // Mostrar datos de parciales si existen
                    etParcial1.setText(c.getString(3));
                    etParcial2.setText(c.getString(4));
                    etParcial3.setText(c.getString(5));

                    // Habilitar el campo para actualizar el primer parcial
                    etParcial1.setEnabled(true);
                    c.close();
                } else {
                    Toast.makeText(getApplicationContext(), "No se encontró el alumno", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Guardar calificaciones y calcular el promedio
        btnGuardarCalificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = ayudabd.getWritableDatabase();
                ContentValues valores = new ContentValues();
                valores.put(baseDatos.Datostabla.COLUMNA_PARCIAL1, etParcial1.getText().toString());
                valores.put(baseDatos.Datostabla.COLUMNA_PARCIAL2, etParcial2.getText().toString());
                valores.put(baseDatos.Datostabla.COLUMNA_PARCIAL3, etParcial3.getText().toString());

                String seleccion = baseDatos.Datostabla.COLUMNA_ID + " = ?";
                String[] seleccionArgs = { etMatricula.getText().toString() };

                int rows = db.update(baseDatos.Datostabla.NOMBRE_TABLE, valores, seleccion, seleccionArgs);
                if (rows > 0) {
                    Toast.makeText(getApplicationContext(), "Calificaciones guardadas", Toast.LENGTH_LONG).show();

                    // Deshabilitar el parcial correspondiente y habilitar el siguiente
                    if (etParcial1.isEnabled()) {
                        etParcial1.setEnabled(false);
                        etParcial2.setEnabled(true);
                    } else if (etParcial2.isEnabled()) {
                        etParcial2.setEnabled(false);
                        etParcial3.setEnabled(true);
                    } else {
                        etParcial3.setEnabled(false);

                        // Calcular el promedio solo si todos los parciales tienen valores
                        if (!etParcial1.getText().toString().isEmpty() &&
                                !etParcial2.getText().toString().isEmpty() &&
                                !etParcial3.getText().toString().isEmpty()) {

                            double promedio = (Double.parseDouble(etParcial1.getText().toString())
                                    + Double.parseDouble(etParcial2.getText().toString())
                                    + Double.parseDouble(etParcial3.getText().toString())) / 3;
                            etPromedio.setText(String.valueOf(promedio));
                        } else {
                            etPromedio.setText("");
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error al guardar las calificaciones", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
