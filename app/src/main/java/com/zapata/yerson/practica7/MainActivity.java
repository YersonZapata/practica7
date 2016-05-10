package com.zapata.yerson.practica7;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Cursor c;
    private  Integer gananciatotal;
    private EditText Id,Nombre,Cantidad,Valor;
    private TextView salida;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gananciatotal=0;

        salida =(TextView) findViewById(R.id.salida);
        Id=(EditText) findViewById(R.id.ident);
        Nombre=(EditText) findViewById(R.id.nombre);
        Cantidad=(EditText) findViewById(R.id.cantidad);
        Valor=(EditText) findViewById(R.id.valor);
        IniciarBaseDeDatos();
    }

    public void agregar (View view){
        BaseDeDatos peluche = new BaseDeDatos(this, "basededatos",null,1);
        SQLiteDatabase bd=peluche.getWritableDatabase();

        String identificacion = Id.getText().toString();
        String nombre = Nombre.getText().toString();
        String cantidad = Cantidad.getText().toString();
        String valor = Valor.getText().toString();

        if((!identificacion.equals("") && !nombre.equals("") && !valor.equals("") &&  !cantidad.equals("")) ){

        ContentValues registro = new ContentValues(); //Es para guardar los datos ingresados
        registro.put("ident", identificacion);
        registro.put("nombre", nombre);
        registro.put("cantidad", cantidad );
        registro.put("valor", valor);

//----para validar nombre
        c = bd.rawQuery("select ident, cantidad, valor from peluches where nombre='" + nombre + "'", null);

        if(c.moveToFirst()==true){
            Toast.makeText(this,"El nombre ya existe",Toast.LENGTH_SHORT).show();
            bd.close();
               }else{


            // --PAra validar nombre

            bd.insert("peluches",null,registro);
            bd.close();
            c.close();

            Id.setText("");
            Nombre.setText("");
            Cantidad.setText("");
            Valor.setText("");
            Toast.makeText(this,"Se guardo el peluche",Toast.LENGTH_SHORT).show();
        }
        }else{
            Toast.makeText(this,"Rellene todos los campos",Toast.LENGTH_SHORT).show();
        }

    }

    public void buscar (View view){
        String nombre = Nombre.getText().toString();
        if(!(nombre.equals(""))) {
            BaseDeDatos estudiante = new BaseDeDatos(this, "basededatos", null, 1);
            SQLiteDatabase bd = estudiante.getWritableDatabase();


            c = bd.rawQuery("select ident, cantidad, valor from peluches where nombre='" + nombre + "'", null);

            if (c.moveToFirst() == true) {
                Id.setText(c.getString(0));
                Cantidad.setText(c.getString(1));
                Valor.setText(c.getString(2));
            } else {
                Toast.makeText(this, "No existe el peluche", Toast.LENGTH_SHORT).show();
            }
            bd.close();
            c.close();
        }else {
            Toast.makeText(this, "Ingrese campo Nombre", Toast.LENGTH_SHORT).show();
        }
    }
    public void eliminar (View view){
        String nombre = Nombre.getText().toString();

        if(!(nombre.equals(""))) {
            BaseDeDatos admin = new BaseDeDatos(this, "basededatos", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();

            int cant = bd.delete("peluches", "nombre='" + nombre+"'", null);
            bd.close();
            Nombre.setText("");
            Valor.setText("");
            Id.setText("");
            Cantidad.setText("");

            if (cant == 1) {
                Toast.makeText(this, "Se borró el peluche", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No existe ese peluche", Toast.LENGTH_SHORT).show();

            }
        }else{
            Toast.makeText(this, "Llene el campo Nombre", Toast.LENGTH_SHORT).show();
        }
    }
    public void actualizar (View view){
        String nombre = Nombre.getText().toString();
        String cantidad = Cantidad.getText().toString();
        if((!nombre.equals("") && !cantidad.equals("")) ) {
            BaseDeDatos peluche = new BaseDeDatos(this, "basededatos", null, 1);
            SQLiteDatabase bd = peluche.getWritableDatabase();


            //String nombre = Nombre.getText().toString();

            //String valor = Valor.getText().toString();

            ContentValues registro = new ContentValues();
           // registro.put("nombre", nombre);
            //registro.put("valor", valor);
            registro.put("cantidad", cantidad);

            boolean cant = bd.update("peluches", registro, "nombre='" + nombre+"'", null) > 0;
            bd.close();
            mostrardb();
            if (cant) {
                Toast.makeText(this, "Se a modificado exitosamente a "+cantidad, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No existe el peluche", Toast.LENGTH_SHORT).show();

            }
        }else{
            Toast.makeText(this, "Campo Nombre y Cantidad obligatorio", Toast.LENGTH_SHORT).show();
        }
    }

    public void mostrar (View view){
mostrardb();

    }



    public void venta (View view){
        String nombre = Nombre.getText().toString();
        int valor,cantidad;
        if(!(nombre.equals(""))) {
            BaseDeDatos estudiante = new BaseDeDatos(this, "basededatos", null, 1);
            SQLiteDatabase bd = estudiante.getWritableDatabase();
            ContentValues registro = new ContentValues();


            c = bd.rawQuery("select cantidad, valor from peluches where nombre='" + nombre + "'", null);

            if (c.moveToFirst() == true) {
                cantidad =c.getInt(0);
                valor = c.getInt(1);


                if (cantidad >0){
                    gananciatotal=gananciatotal+valor;
                    cantidad=cantidad-1;

                registro.put("cantidad", cantidad);
                bd.update("peluches", registro, "nombre='" + nombre+"'", null);
                    mostrardb();
                if (cantidad<=5){
                    doNotificacion("Pocas existencias de: "+nombre,"Reabastecimiento del inventario","Revice el inventario");
                }
                }else{
                    Toast.makeText(this, "La venta no se puede realizar \n No quedan articulos disponibles", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "No existe el peluche", Toast.LENGTH_SHORT).show();
            }
            bd.close();
            c.close();
        }else {
            Toast.makeText(this, "Ingrese el campo Nombre", Toast.LENGTH_SHORT).show();
        }

    }
    public void ganancias (View view){
        Toast.makeText(this, gananciatotal+" $", Toast.LENGTH_SHORT).show();
    }

    private void IniciarBaseDeDatos(){
        BaseDeDatos peluche = new BaseDeDatos(this, "basededatos",null,1);
        SQLiteDatabase bd=peluche.getWritableDatabase();

        ContentValues registro = new ContentValues(); //Es para guardar los datos ingresados
        registro.put("ident", "1");registro.put("nombre", "Iron Man");registro.put("cantidad", "10" );
        registro.put("valor", "15000");bd.insert("peluches",null,registro);

        registro.put("ident", "2");registro.put("nombre", "Viuda Negra");registro.put("cantidad", "10" );
        registro.put("valor", "12000");bd.insert("peluches",null,registro);

        registro.put("ident", "3");registro.put("nombre", "Capitan America");registro.put("cantidad", "10" );
        registro.put("valor", "15000");bd.insert("peluches",null,registro);

        registro.put("ident", "4");registro.put("nombre", "Hulk");registro.put("cantidad", "10" );
        registro.put("valor", "12000");bd.insert("peluches",null,registro);

        registro.put("ident", "5");registro.put("nombre", "Bruja Escarlata");registro.put("cantidad", "10" );
        registro.put("valor", "15000");bd.insert("peluches",null,registro);

        registro.put("ident", "6");registro.put("nombre", "Spiderman");registro.put("cantidad", "10" );
        registro.put("valor", "10000");bd.insert("peluches",null,registro);

        bd.close();
    }

public  void doNotificacion (String title, String content, String ticker){
    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

    builder.setContentTitle(title)
            .setContentText(content)
            .setTicker(ticker)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentInfo("Alerta!");

    Intent noIntent = new Intent(MainActivity.this,MainActivity.class);
    PendingIntent contIntent = PendingIntent.getActivity(MainActivity.this,0,noIntent,0);
    builder.setContentIntent(contIntent);
    NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    nm.notify(1,builder.build());
}

    public void mostrardb(){


        //------------------
        BaseDeDatos peluche = new BaseDeDatos(this, "basededatos", null, 1);
        SQLiteDatabase bd = peluche.getReadableDatabase();
        ArrayList<String> lista_contactos = new ArrayList<String>();
        String[] valores_recuperar = {"ident", "nombre", "cantidad", "valor"};
        Cursor c = bd.query("peluches", valores_recuperar,null, null, null, null, null, null);
        if(c.moveToFirst()) {
            do {
                lista_contactos.add("\t" + c.getString(0) + " \t " + c.getString(1) + "\t  " + c.getString(2) + "u \t  " + c.getString(3) + "$\n");

            } while (c.moveToNext());
        }else{
            Toast.makeText(this, "No hay nada en el inventario", Toast.LENGTH_SHORT).show();
        }

        bd.close();
        c.close();
        salida.setText(lista_contactos.toString());
        //________________

    }

}
